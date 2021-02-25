import java.util.*;
import java.time.LocalDateTime;

public class MemoryMapPacket {
	
	private int[] serverId;
	private int addrLen;
	private int dataLen;
	

  // Known length [bytes] -- likely used for an RX packet
  public MemoryMapPacket ( int[] serverId, int addrLen, int dataLen ) {
  	super( serverId, (addrLen + dataLen) );
  	this.addrLen = addrLen;
  	this.dataLen = dataLen;
  }
    
  // Known data [int value] -- likely used for a TX packet
  public MemoryMapPacket ( int[] serverId,  int addrLen, int dataLen, int addrVal, int dataVal ) {
		super( serverId, createFrame(addrLen, dataLen, addrVal, dataVal) );
  	this.addrLen = addrLen;
  	this.dataLen = dataLen;
  }
  
	// addrVal and dataVal are treated as unsigned integers
	private static int[] createFrame( int addrLen, int dataLen, int addrVal, int dataVal ) {
  	int[] frame = new int[addrLen + dataLen];
  	for (int i=addrLen-1; i>=0; i--) {
  		frame[i] = 0x000000ff & addrVal;
  		addrVal >> 8;
  	}
  	for (int i=(addrLen+dataLen)-1; i>=addrLen; i--) {
  		frame[i] = 0x000000ff & dataVal;
  		dataVal >> 8;
  	}
  	return frame;
  }
  
  private static byteSum (int leftByteIndex, int rightByteIndex ) {
  	int sum = 0;
  	int byteMultiplier = 1
  	for (int i=rightByteIndex-1; i>=leftByteIndex; i--) {
  		sum += (packet().get(i).intVal() * byteMultiplier);
  		byteMultiplier << 8;
  	}
  	return sum;
  }
	
  public int addr () {
		return byteSum(0, addrLen);
	}
  
	public int data () {
		return byteSum(addrLen, dataLen);
	}
  
	// Test
  public static void main(String[] args) {
  	// Test received packet
    MemoryMapPacket rx = new MemoryMapPacket( new int[]{ 1, 2 }, 1, 1 );
    rx.add(1);
    rx.add(2);
    rx.add(1);
    rx.add(4);
    System.out.println("rx valid: "+rx.valid());
    rx.add(8);
    System.out.println("rx valid: "+rx.valid());
    System.out.println("rx addr: "+rx.addr());
    System.out.println("rx data: "+rx.data());
    // Test transmitted packet
    Packet tx = new Packet( new int[]{ 1, 2 }, 1, 1, 1, 4);
    System.out.println("tx valid: "+tx.valid());
    System.out.println("tx addr: "+tx.addr());
    System.out.println("tx data: "+tx.data());
    System.out.println(tx);
  }

}
