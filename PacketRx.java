import java.util.*;
import java.time.LocalDateTime;

public class PacketRx {
	
	private int[] header;
	private int packetLength;
	
  private List<Integer> packet;
  private LocalDateTime time;
  
  

  public PacketRx ( int[] header, int packetLength ) {
  	this.header = header;
  	this.packetLength = packetLength;
    time = LocalDateTime.now();
    packet = new ArrayList<Integer>();
  }
  
  
  public void add ( int a ) {
  	packet.add(a);
  	if (packet.size() > packetLength) packet.remove(0);
  }

  public boolean valid () {
  	// check size
  	if (packet.size() < packetLength)
  		return false;
  	// packet counters
   	int checksum = 0;
  	int i = 0;
  	// check header bytes
		for (; i<header.length; i++) {
			if (packet.get(i).intValue() != header[i]) {
				//System.out.println("header failed: "+toString());
				return false;
			} else {
				checksum += packet.get(i).intValue();
			}
		}
		// sum remaining bytes (but not last byte, which is the checksum)
		for (; i<packetLength-1; i++) {
			checksum += packet.get(i).intValue();
		}
		// check checksum
		if (packet.get(packet.size()-1).intValue() == (checksum & 0x000000ff)) {
			return true;
		} else {
			//System.out.println("checksum failed: "+packet.get(packet.size()-1));
			return false;
		}
  }
  
  private void printInt (int i) {
    System.out.print( i+": " );
    for (int a=31; a>=0; a--) {
      if ((i & 0x80000000)==0x80000000) {
        System.out.print( "1 " );
      } else {
        System.out.print( "0 " );
      }
      i = i << 1;
    }
    System.out.println();
  }

  public int data16 (int high, int low) {
    int combined = ((packet.get(high).intValue() << 8) + packet.get(low).intValue());
    if ((0xffff7fff | combined) == 0xffff7fff) { // positive value
      combined = (0x0000ffff & combined); // make sure all higher bits are 0s
    } else if ((0x00008000 & combined) == 0x00008000) { // negative value
      combined = (0xffff0000 | combined); // make sure all higher bits are 1s
    }
    return combined;
  }

  public List<Integer> packet () {
    return packet;
  }
  
  public String toString () {
    // print decimal
    String p = "\nDec: ";
    for (Integer i : packet) p += i+",";

    // print hex
    p += "\nHex: ";
    for (Integer i : packet) p += String.format("%02x", i)+" ";

    // print decimal with high-low bits combined
    p += "\n16-bit dec: ";
    for (int i=0; i<packet.size()-1; i+=2) p+= data16(i, i+1)+",";
    p += "\n";

    return p;
  }

  public static void main(String[] args) {
    PacketRx p = new PacketRx( new int[]{ 1, 2 }, 5 );
    // test valid()
    p.add(1);
    p.add(2);
    p.add(1);
    p.add(4);
    p.add(8);
    p.valid();
    System.out.println(p);
    System.out.println("0x0001, 0x0004 -> 0x0104: "+p.data16( 2, 3 ));
  }

}
