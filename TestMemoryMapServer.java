import java.io.*;
import java.net.*;

public class TestMemoryMapServer {

	public static void write (PrintStream output, int addrHigh, int addrLow, int data) throws Exception {
		Packet writeData = new Packet(
			new int[]{ 'm','0' },
			new int[]{ addrHigh, addrLow, data }
		);
		for (Integer val : writeData.packet()) {
			output.write(val);
		}
		output.write('\n'); // in case the output interface requires a return; may be optional		
	}
	
	public static int read (PrintStream output, int addrHigh, int addrLow, InputStream input) throws Exception {
		write( output, addrHigh & 0x80, addrLow, 0 );
		Packet receiveData = new Packet(
			new int[]{ 'm','0' },
			3
		);
		while(true) {
		  int b = input.read();
		  if (b != -1) {
		  	System.out.println( b );
		  	receiveData.add(b);
		  	if (receiveData.valid()) return receiveData.data8(5);
		  }
		}
	}


  public static void main (String[] args) throws Exception {
		
		Socket socket = new Socket( "localhost", 9090 );
		
		InputStream in = socket.getInputStream();
		PrintStream out = new PrintStream(socket.getOutputStream());

		write( out, 0, 2,   5 );
		write( out, 0, 3,   7 );
		System.out.println(
			read ( out, 0, 2, in )
		);
	  
	}
}