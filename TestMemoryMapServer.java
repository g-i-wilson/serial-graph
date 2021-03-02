import java.io.*;
import java.net.*;

public class TestMemoryMapServer {
  public static void main (String[] args) throws Exception {

		Packet writeData = new Packet(
			new int[]{ 'm','0' },
			new int[]{ 0, 4, 7 }
		);
		System.out.println( writeData );
		
		Packet readData = new Packet(
			new int[]{ 'm','0' },
			new int[]{ 128, 4, 0 }
		);
		System.out.println( readData );
		
		Packet receiveData = new Packet(
			new int[]{ 'm','0' },
			3
		);
		System.out.println( receiveData );
		
		
		Socket socket = new Socket( "localhost", 9090 );
		InputStream input = socket.getInputStream();
		PrintStream output = new PrintStream(socket.getOutputStream());

		for (Integer val : writeData.packet()) {
			output.write(val);
		}
		output.write('\n'); // in case the output interface requires a return; may be optional

		for (Integer val : readData.packet()) {
			output.write(val);
		}
		output.write('\n'); // in case the output interface requires a return; may be optional


		System.out.println("here!");
		
	  while(true) {
	    int b = input.read();
	    if (b != -1) {
	    	System.out.println( b );
	    	receiveData.add(b);
	    	if (receiveData.valid()) break;
	    }
	  }
	  
	  System.out.println( receiveData );
	  
	}
}