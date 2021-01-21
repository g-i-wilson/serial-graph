//import graphtoolkit.*;
import java.util.*;
import java.io.*;
import java.net.Socket;

public class LoopbackTest {


	private Packet txData;
	private Packet rxData;

  public LoopbackTest ( InputStream in, PrintStream out ) throws Exception {
		
		// TX
		
		txData = new Packet( new int[]{ 't','x' }, // 0x74, 0x78
			new int[]{ 
				'a','b','c','d','e','f','g','h'
			}
		);
		System.out.println("txData:\n"+txData);
		for (Integer val : txData.packet()) {
			//System.out.println(val);
			out.write(val);
		}
		out.write('\n'); // in case the output interface requires a return; may be optional
		
		
		// RX

	  rxData = new Packet( new int[]{ 'r','x' }, 8 ); // 0x72, 0x78
	  while(true) {
	    int b = in.read();
	    if (b != -1) {
	    	rxData.add(b);
	    	System.out.println("received: "+b);
	    	if (rxData.valid()) {
	      	break;
			  }
			}
		}
		System.out.println("rxData:\n"+rxData);
		
  }
  

  // main method for testing
  public static void main (String[] args) throws Exception {
  	Socket socket = new Socket( "localhost", 9090 );
  	new LoopbackTest(
  		socket.getInputStream(),
			new PrintStream(socket.getOutputStream())
		);
	}
	
}
