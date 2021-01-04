import graphtoolkit.*;
import java.util.*;
import java.io.*;

public class FreqBurst {


	private Packet txData;
	private SortedMap<Integer,SortedMap<Integer,Packet>> rxData;

  public FreqBurst (
  	InputStream in, 
  	PrintStream out, 
  	int stepStart, 
  	int stepEnd, 
  	int cycles, 
  	int preSamples, 
  	int stepSamples, 
  	int postSamples
  ) throws IOException {

		rxData = new TreeMap<>();		
		
		int totalSamples = (cycles+1) * (preSamples + (stepEnd-stepStart)*stepSamples + postSamples);

		txData = new Packet( new int[]{ 't','x' }, // 0x74, 0x78
			new int[]{ 
				(stepStart << 4) | stepEnd,
				cycles,
				preSamples >> 8,
				preSamples,
				stepSamples >> 8,
			 	stepSamples,
				postSamples >> 8,
			 	postSamples
			}
		);
		
		out.write('\n'); // in case the output interface requires a return; may be optional
		for (Integer val : txData.packet()) {
			out.write(val);
		}
		out.write('\n'); // in case the output interface requires a return; may be optional
		
		for (int i=0; i<totalSamples; i++) {
		  Packet rxPacket = new Packet( new int[]{ 'r','x' }, 8 ); // 0x72, 0x78
		  while(true) {
		    int b = in.read();
		    out.write(b);
		    if (b != -1) {
		    	rxPacket.add(b);
		    	//out.write(b);
		    	if (rxPacket.valid()) {
		    		System.out.println("!"+i);
		    		int cycle = rxPacket.data8(5);
		    		int sample = rxPacket.data16(6, 7);
		    		if (! rxData.containsKey(cycle))
		    			rxData.put(cycle, new TreeMap<Integer,Packet>());
	    			rxData.get(cycle).put(sample, rxPacket);
		      	break;
				  }
				}
			}
		}
		
  }
  
  public Packet txData () {
  	return txData;
  }
  
  public SortedMap<Integer,SortedMap<Integer,Packet>> rxData () {
  	return rxData;
  }
  
  public static void main (String[] args) throws Exception {
  	InputStream in = new ByteArrayInputStream(
  		new byte[]{
  			'r','x',0x00,(byte)0x80,0x00,(byte)0x80,0x01,0x00,0x00,0x00,(byte)0xeb,
  			'r','x',0x01,0x00,0x01,0x00,0x01,0x00,0x00,0x01,(byte)0xee,
  			'r','x',0x02,0x00,0x02,0x00,0x01,0x00,0x00,0x02,(byte)0xf1,
  			'r','x',0x04,0x00,0x04,0x00,0x01,0x00,0x00,0x03,(byte)0xf6,
  			'r','x',0x08,0x00,0x08,0x00,0x01,0x00,0x00,0x04,(byte)0xff,
  			'r','x',0x10,0x00,0x10,0x00,0x01,0x00,0x00,0x05,(byte)0x10
  		}
  	);
  	//for (int i=0; i<100; i++) {
  	//	int b = in.read();
  	//	if (b != -1)
  	//		System.out.write( in.read() );
  	//}
		FreqBurst aBurst = new FreqBurst(
			in,
			System.out,
			4, // ramp start
			7, // ramp end
			0, // cycles-1
			1, // pre samples
			1, // step samples
			1 // post samples
		);
	}
		  
}
