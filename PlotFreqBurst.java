import graphtoolkit.*;
import java.util.*;
import java.io.*;
import java.net.Socket;
import java.awt.Color;

public class PlotFreqBurst {

	public static double multReal (double aR, double aI, double bR, double bI) {
		return aR*bR - aI*bI;
	}

	public static double multImag (double aR, double aI, double bR, double bI) {
		return aR*bI + aI*bR;
	}

  public static void main (String[] args) throws Exception {

    GraphModelShapes phaseModel = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));
    GraphModelShapes freqModel = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));

    GraphView phaseWindow = new GraphViewGUI( phaseModel, 1200, 400, new Color( 0, 255, 0 ), "Phase" );
    GraphView freqWindow = new GraphViewGUI( freqModel, 1200, 400, new Color( 255, 0, 255 ), "Frequency" );

		Socket socket = new Socket( "localhost", 9090 );

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
			
			//Thread.sleep(2000);

			FreqBurst aBurst = new FreqBurst(
				socket.getInputStream(),
				new PrintStream(socket.getOutputStream()),
				15, // ramp start
				0, // ramp end
				0, // cycles-1
				10, // pre samples
				5, // step samples
				300 // post samples
			);
			

		  double x=0;
		  double iVal = 0;
		  double qVal = 0;
		  double iValPrev = 0;
		  double qValPrev = 0;
		  double divVal = 0;
		  
		  SortedMap<Integer,SortedMap<Integer,Map<String,Integer>>> rx = aBurst.rxData();
		  
		  for (SortedMap<Integer,Map<String,Integer>> cycle : rx.values()) {
		  	for (Map<String,Integer> sample : cycle.values()) {
					x++;

					iValPrev = iVal;
					qValPrev = qVal;
				  iVal = (double) sample.get("I").intValue();
				  qVal = (double) sample.get("Q").intValue();
				  divVal = (double) sample.get("RFDIV").intValue();

				  //phaseModel.plotPoint( x, iVal/100 );
				  //freqModel.plotPoint( x, qVal/100 );
				  phaseModel.plotPoint( x, Math.atan2(iVal, qVal)*10 );
				  freqModel.plotPoint( x, Math.atan2(
				  	multReal(iVal, qVal, iValPrev, -qValPrev),
				  	multImag(iVal, qVal, iValPrev, -qValPrev)
				  )*10 );
				  
				  //System.out.println("I: "+iVal+", Q: "+qVal+", RFDIV: "+divVal);

				  phaseWindow.refresh();
				  freqWindow.refresh();
				  
				  //Thread.sleep(100);
				  
				}
			}
			
			//Thread.sleep(500);
	  	phaseModel.clear();
	  	freqModel.clear();

		}
  }
  
}
