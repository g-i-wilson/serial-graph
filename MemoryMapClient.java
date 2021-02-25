import graphtoolkit.*;
import java.util.*;
import java.io.*;

public class MemoryMapClient {

	private int[] serverId;
	private int addrLength;
	private int dataLength;

	private InputStream in;
	private PrintStream out;
	
	private SortedMap<Integer, Integer> map;
	private boolean busy;

  public MemoryMapClient (
  	int[] serverId,
  	int addrLength, // bytes
  	int dataLength, // bytes
  	InputStream in, 
  	PrintStream out
  ) throws Exception {
		this.in = in;
		this.out = out;
		this.serverId = serverId;
		this.addrLength = addrLength;
		this.dataLength = dataLength;
  }
  
  public int read (int addr) throws Exception {
  	int[] frame = new int[addr.length + data.length];
  	for (int i=addr.length; i>0; i--) {
  		frame[i] = 0x000000ff & addr;
  		addr >> 8;
  	}
  	transmit( frame );
  	return receive();
  }
  
  public void write (int addr, int data) {
  	
  	transmit( frame );
  }
  
  private void transmit (int[] frame) {
  	Packet tx = new Packet( serverId, frame );
  	for (int txVal : tx.packet()) {
  		out.write(txVal);
  	}
  }
  
  private int receive () {
  	Packet rx = new Packet( serverId, (addrLength + dataLength) );
  	while( ! rx.valid() ) {
  		int nextByte = in.read();
  		if (nextByte != -1) {
  			rx.add( nextByte );
  		} else {
  			Thread.sleep(1);
  		}
  	}
  	for (int) {
  		
  	}
  	return 
  }
  
  public String toString () {

  }
  
  // main method for testing
  public static void main (String[] args) throws Exception {

	}
		  
}
