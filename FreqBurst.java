import graphtoolkit.*;
import java.util.*;
import java.io.*;

public class FreqBurst {


	private Packet txData;
	private SortedMap<Integer,SortedMap<Integer,Packet>> rxRawData;
	private SortedMap<Integer,SortedMap<Integer,Map<String,Integer>>> rxData;

  public FreqBurst (
  	InputStream in, 
  	PrintStream out, 
  	int stepStart, 
  	int stepEnd, 
  	int cycles, 
  	int preSamples, 
  	int stepSamples, 
  	int postSamples
  ) throws Exception {

		rxData = new TreeMap<>();		
		rxRawData = new TreeMap<>();		
		
		int totalSamples = (cycles+1) * (preSamples + (stepEnd-stepStart+1)*stepSamples + postSamples);

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
		
		//System.out.println(txData);
		for (Integer val : txData.packet()) {
			//System.out.println(val);
			out.write(val);
		}
		out.write('\n'); // in case the output interface requires a return; may be optional
		
		//out.println("total:"+totalSamples);
		for (int i=0; i<totalSamples; i++) {
		  Packet rxPacket = new Packet( new int[]{ 'r','x' }, 8 ); // 0x72, 0x78
		  while(true) {
		    int b = in.read();
		    //out.write(b);
		    if (b != -1) {
		    	rxPacket.add(b);
		    	//out.write(b);
		    	if (rxPacket.valid()) {
		    		//System.out.println("valid!"+i);
		    		int cycle = rxPacket.data8(7);
		    		int sample = rxPacket.data16(8, 9);
		    		if (! rxData.containsKey(cycle))
		    			rxData.put(cycle, new TreeMap<Integer,Map<String,Integer>>());
		    			rxRawData.put(cycle, new TreeMap<Integer,Packet>());
		    		if (! rxData.get(cycle).containsKey(sample))
		    			rxData.get(cycle).put(sample, new LinkedHashMap<String,Integer>());
	    			rxData.get(cycle).get(sample).put("I",rxPacket.data16(2,3));
	    			rxData.get(cycle).get(sample).put("Q",rxPacket.data16(4,5));
	    			rxData.get(cycle).get(sample).put("RFDIV",rxPacket.data8(6));
	    			rxRawData.get(cycle).put(sample, rxPacket);
		      	break;
				  }
				}
			}
		}
		
  }
  
  public Packet txData () {
  	return txData;
  }
  
  public SortedMap<Integer,SortedMap<Integer,Map<String,Integer>>> rxData () {
  	return rxData;
  }
  
  public SortedMap<Integer,SortedMap<Integer,Packet>> rxRawData () {
  	return rxRawData;
  }
  
  public String toString () {
  	return rxData.toString();
  }
  
  // main method for testing
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
		// keep thread alive so the piped output isn't broken at exit...
		//while(true) Thread.sleep(1);
		System.out.println( aBurst );
	}
		  
}
