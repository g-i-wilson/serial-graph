import java.util.*;
import java.io.*;

public class PacketRxClient extends Thread{

	private int idLen;
	private int dataLen;
	private InputStream in;
	
	private SortedMap<String, List<Packet>> inbox;
	private boolean busy;

  public PacketRxClient ( 
  	int idLen,
  	int dataLen,
  	InputStream in
  ) throws Exception {
  	this.idLen = idLen;
  	this.dataLen = dataLen;
		this.in = in;
		this.out = out;
		this.inbox = new TreeMap<>();
		start()
  }
  
  public void run (String serverId) throws Exception {
  	Packet rxPacket = new Packet( 
  	while( ! rxPacket.valid() ) {
  		int nextByte = in.read();
  		if (nextByte != -1) {
  			rxPacket.add( nextByte );
  		} else {
  			Thread.sleep(1);
  		}
  	}
  	return txPacket;
  }
  
  public Packet rx () {
  	
  }
  
  public void tx (Packet txPacket) {
  
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
