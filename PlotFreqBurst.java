import graphtoolkit.*;
import java.util.*;
import java.io.*;
import java.net.Socket;
import java.awt.Color;

public class PlotFreqBurst {

  private GraphModelShapes phaseModel;
  private GraphModelShapes freqModel;
  private GraphModelShapes rfModel;
  private GraphView phaseWindow;
  private GraphView freqWindow;
  private GraphView rfWindow;
	private Socket socket;


	public static double multReal (double aR, double aI, double bR, double bI) {
		return aR*bR - aI*bI;
	}

	public static double multImag (double aR, double aI, double bR, double bI) {
		return aR*bI + aI*bR;
	}
	

	public PlotFreqBurst () throws Exception {
		phaseModel = new GraphModelShapes(200.0, 200.0, -200.0, -200.0, new Color( 0, 127, 255 ));
		freqModel = new GraphModelShapes(200.0, 200.0, -200.0, -200.0, new Color( 0, 127, 255 ));
		rfModel = new GraphModelShapes(200.0, 200.0, -200.0, -200.0, new Color( 0, 127, 255 ));

		phaseWindow = new GraphViewGUI( phaseModel, 600, 600, new Color( 0, 255, 0 ), "Phase" );
		freqWindow = new GraphViewGUI( freqModel, 600, 600, new Color( 255, 0, 255 ), "Frequency" );
		rfWindow = new GraphViewGUI( rfModel, 600, 600, new Color( 0, 255, 255 ), "RF DIV" );

		socket = new Socket( "localhost", 9090 );
	}

	public void plotGraph ( int rampStart, int rampEnd, int preSamples, int stepSize, int postSamples) throws Exception {

			FreqBurst aBurst = new FreqBurst(
				socket.getInputStream(),
				new PrintStream(socket.getOutputStream()),
				rampStart, // ramp start
				rampEnd, // ramp end
				0, // cycles-1
				preSamples, // pre samples
				stepSize, // step samples
				postSamples // post samples
			);
			

		  double x=0;
		  double iVal = 0;
		  double qVal = 0;
		  double iValPrev = 0;
		  double qValPrev = 0;
		  double divVal = 0;
		  double fVal = 0;
		  double pVal = 0;
		  
		  SortedMap<Integer,SortedMap<Integer,Map<String,Integer>>> rx = aBurst.rxData();
		  
			//Thread.sleep(500);
	  	phaseModel.clear();
	  	freqModel.clear();
	  	rfModel.clear();

		  for (SortedMap<Integer,Map<String,Integer>> cycle : rx.values()) {
		  	for (Map<String,Integer> sample : cycle.values()) {
					x++;

					iValPrev = iVal;
					qValPrev = qVal;
				  iVal = (double) sample.get("I").intValue();
				  qVal = (double) sample.get("Q").intValue();
				  divVal = (double) sample.get("RFDIV").intValue();
				  //phase
				  pVal = Math.atan2(iVal, qVal);
				  // frequency
				  //fVal = Math.log(
				  //	Math.atan2(
					//		multReal(iVal, qVal, iValPrev, -qValPrev),
					//		multImag(iVal, qVal, iValPrev, -qValPrev)
				  //	)
				  //)*100;
				  fVal = Math.atan2(
							multReal(iVal, qVal, iValPrev, -qValPrev),
							multImag(iVal, qVal, iValPrev, -qValPrev)
				  	);

				  //phaseModel.plotPoint( x, iVal/100 );
				  //freqModel.plotPoint( x, qVal/100 );
				  phaseModel.plotPoint( x-180, pVal*50 );
				  freqModel.plotPoint( x-180, fVal*100 );
				  rfModel.plotPoint( x, divVal*3 );
				  
				  //System.out.println("I: "+iVal+", Q: "+qVal+", RFDIV: "+divVal);
				  //System.out.println(x+", "+fVal);

				  //phaseWindow.refresh();
				  //freqWindow.refresh();
				  //rfWindow.refresh();
				  
				  //Thread.sleep(100);
				  //System.out.println(x);
				  
				}
			}
		  phaseWindow.refresh();
		  freqWindow.refresh();
		  rfWindow.refresh();
	  	
	}

  public static void main (String[] args) throws Exception {
  
  	PlotFreqBurst pfb = new PlotFreqBurst();

		while(true) {
		
		  //Thread.sleep(1000);
		  //System.out.println("up");
			pfb.plotGraph( 0, 15, 0, 5, 300 );
		  //Thread.sleep(1000);
		  //System.out.println("down");
			//pfb.plotGraph( 9, 8, 0, 5, 0 );

		}
  }
  
}
