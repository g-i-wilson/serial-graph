import java.util.*;
import java.time.LocalDateTime;

public class Packet {
	
	private int[] header;
	private int packetLength;
	
  private List<Integer> packet;
  private LocalDateTime time;
  
  // Known length -- likely used for an RX packet
  public Packet ( int[] header, int dataLength ) {
  	this.header = header;
  	this.packetLength = header.length + dataLength + 1;
    time = LocalDateTime.now();
    packet = new ArrayList<Integer>();
  }
    
  // Known data -- likely used for a TX packet
  public Packet ( int[] header, int[] data ) {
  	this.header = header;
  	this.packetLength = header.length + data.length + 1;
    time = LocalDateTime.now();
    packet = new ArrayList<Integer>();
    for (int headerVal : header) {
    	packet.add(headerVal);
    }
    for (int dataVal : data) {
    	packet.add(dataVal);
    }
    packet.add(checksum());
  }
  
  // Add a byte to the packet (will shift after packetLength reached)
  public void add ( int a ) {
  	packet.add(a);
  	if (packet.size() > packetLength) packet.remove(0);
  }
  
  // Calculate a checksum (simple byte summation allowing overflow)
  public int checksum () {
  	int checksum = 0;
		// sum remaining bytes (but not last byte, which is the checksum)
		for (int i=0; i<packetLength-1; i++) {
			checksum += packet.get(i).intValue();
		}
		return checksum;
  }

	// Validate the packet has correct header and checksum is correct
  public boolean valid () {
  	// check size
  	if (packet.size() < packetLength)
  		return false;
  	// check header bytes
		for (int i=0; i<header.length; i++) {
			if (packet.get(i).intValue() != header[i]) {
				//System.out.println("header failed: "+toString());
				return false;
			}
		}
		// check checksum
		if (packet.get(packet.size()-1).intValue() == (checksum() & 0x000000ff)) {
			return true;
		} else {
			//System.out.println("checksum failed: "+packet.get(packet.size()-1));
			return false;
		}
  }
  
  // Print integer values (bytes are stored in Java as 32bit ints)
  private String printInt (int i) {
  	String binStr = "";
    for (int a=31; a>=0; a--) {
      if ((i & 0x80000000)==0x80000000) {
        binStr += "1";
      } else {
        binStr += "0";
      }
      i = i << 1;
    }
    return binStr;
  }
  
  public int data8 (int theByte) {
  	return packet.get(theByte).intValue();
  }

	// Combine the 8bit values from two ints into equivalent 16bit value int
  public int data16 (int high, int low) {
    int combined = ((packet.get(high).intValue() << 8) + packet.get(low).intValue());
    if ((0xffff7fff | combined) == 0xffff7fff) { // positive value
      combined = (0x0000ffff & combined); // make sure all higher bits are 0s
    } else if ((0x00008000 & combined) == 0x00008000) { // negative value
      combined = (0xffff0000 | combined); // make sure all higher bits are 1s
    }
    return combined;
  }

	// Access the whole packet
  public List<Integer> packet () {
    return packet;
  }
  
  // Access byte array of packet
  public byte[] bytes () {
  	byte[] byteArray = new byte[packet.size()];
  	for (int i=0; i<packet.size(); i++) {
  		byteArray[i] = (byte) packet.get(i).intValue();
  	}
  	return byteArray;
  }
  
  // Access the LocalDateTime object
  public LocalDateTime time () {
  	return time;
  }
  
  // Display the packet
  public String toString () {
  	// print timestamp
    String p = time.toString();
  
    // print binary
    p += "\nBinary: ";
    for (Integer i : packet) p += "\n"+printInt(i);

    // print decimal
    p += "\nDecimal: ";
    for (Integer i : packet) p += i+",";

    // print hex
    p += "\n8-bit hex: ";
    for (Integer i : packet) p += String.format("%02x", i)+",";

    // print decimal with high-low bits combined
    p += "\n16-bit hex: ";
    for (int i=0; i<packet.size()-1; i+=2) p+= String.format("%04x", data16(i, i+1))+",";

    p += "\n";
    return p;
  }

	// Test
  public static void main(String[] args) {
  	// Test received packet
    Packet rx = new Packet( new int[]{ 1, 2 }, 2 );
    rx.add(1);
    rx.add(2);
    rx.add(1);
    rx.add(4);
    System.out.println("rx valid: "+rx.valid());
    rx.add(8);
    System.out.println("rx valid: "+rx.valid());
    System.out.println(rx);
  	// Test data16 method -- indices 2, 3 contain 0x01, 0x04
    System.out.println("0x0001, 0x0004 -> 0x0104: "+rx.data16( 2, 3 )+"\n");
    // Test transmitted packet
    Packet tx = new Packet( new int[]{ 1, 2 }, new int[]{1, 4} );
    System.out.println("tx valid: "+tx.valid());
    System.out.println(tx);
  }

}
