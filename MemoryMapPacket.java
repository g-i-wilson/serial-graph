import graphtoolkit.*;
import java.util.*;
import java.io.*;

public class MemoryMapClient {

	private int[] serverId;
	private int controlLength;
	private int addrLength;
	private int dataLength;

	private InputStream in;
	private PrintStream out;
	
  public MemoryMapClient (
  	int[] serverId,
  	int controlLength // bytes
  	int addrLength, // bytes
  	int dataLength, // bytes
  	InputStream in, 
  	PrintStream out
  ) throws Exception {
		this.in = in;
		this.out = out;
		this.serverId = serverId;
		this.controlLength = controlLength;
		this.addrLength = addrLength;
		this.dataLength = dataLength;
  }
  
  public int read (int addr) throws Exception {
  	if (id > 0x0000007F) throw new Exception("Value of int id must be in range 0..127);
  	int[] frame = new int[controlLength + addr.length + data.length];
  	int[0] = 0x00000080 | id; // set READ bit and ID
  	for (int i=addr.length; i>0; i--) {
  		frame[i] = 0x000000ff & addr;
  		addr >> 8;
  	}
  	transmit( frame );
  	return receive();
  }
  
  public void write (int addr, int data) {
  	// TODO: create packet frame
  	transmit( frame );
  }
  
  private void transmit (int[] frame) {
  	Packet tx = new Packet( serverId, frame );
  	for (int txVal : tx.packet()) {
  		out.write(txVal);
  	}
  }
  
  private int receive () {
  	// TODO: receive loop
  }
  
  public String toString () {

  }
  
  // main method for testing
  public static void main (String[] args) throws Exception {

	}
		  
}
