import graphtoolkit.*;
import java.util.*;
//import java.io.*;
import java.awt.Color;

public class PlotFreqBurst {

  public static void main (String[] args) throws Exception {

    GraphModelShapes iModel = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));
    GraphModelShapes qModel = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));

    GraphView iWindow = new GraphViewGUI( iModel, 1200, 400, new Color( 0, 255, 0 ), "Phase" );
    GraphView qWindow = new GraphViewGUI( qModel, 1200, 400, new Color( 255, 0, 255 ), "Frequency" );

		while(true) {
		
//			InputStream in = new ByteArrayInputStream(
//				new byte[]{
//					'r','x',0x00,(byte)0x80,0x00,(byte)0x80,0x01,0x00,0x00,0x00,(byte)0xeb,
//					'r','x',0x01,0x00,0x01,0x00,0x01,0x00,0x00,0x01,(byte)0xee,
//					'r','x',0x02,0x00,0x02,0x00,0x01,0x00,0x00,0x02,(byte)0xf1,
//					'r','x',0x04,0x00,0x04,0x00,0x01,0x00,0x00,0x03,(byte)0xf6,
//					'r','x',0x08,0x00,0x08,0x00,0x01,0x00,0x00,0x04,(byte)0xff,
//					'r','x',0x10,0x00,0x10,0x00,0x01,0x00,0x00,0x05,(byte)0x10
//				}
//			);
			
			Thread.sleep(2000);

			FreqBurst aBurst = new FreqBurst(
				System.in,
				System.out,
				4, // ramp start
				7, // ramp end
				0, // cycles-1
				10, // pre samples
				10, // step samples
				10 // post samples
			);

		  double x=0;
		  double iVal = 0;
		  double qVal = 0;
		  double divVal = 0;
		  
		  SortedMap<Integer,SortedMap<Integer,Map<String,Integer>>> rx = aBurst.rxData();
		  
		  for (SortedMap<Integer,Map<String,Integer>> cycle : rx.values()) {
		  	for (Map<String,Integer> sample : cycle.values()) {
					x++;

				  iVal = (double) sample.get("I").intValue();
				  qVal = (double) sample.get("Q").intValue();
				  divVal = (double) sample.get("RFDIV").intValue();

				  //iModel.plotPoint( x, Math.atan2(phase_re, phase_im)*10 );
				  //qModel.plotPoint( x, Math.atan2(freq_re, freq_im)*10 );
				  iModel.plotPoint( x*100, iVal );
				  qModel.plotPoint( x*100, qVal );
				  
				  System.out.println("I: "+iVal+", Q: "+qVal+", RFDIV: "+divVal);

				  iWindow.refresh();
				  qWindow.refresh();
				  
				  Thread.sleep(100);
				  
				}
			}
			
			Thread.sleep(2000);
	  	iModel.clear();
	  	qModel.clear();

		}
  }
  
}
