import graphtoolkit.*;
import java.util.*;
import java.awt.Color;

public class PlotFreqBurst {

  public static void main (String[] args) throws Exception {

    GraphModelShapes iModel = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));
    GraphModelShapes qModel = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));

    GraphView iWindow = new GraphViewGUI( iModel, 1200, 400, new Color( 0, 255, 0 ), "Phase" );
    GraphView qWindow = new GraphViewGUI( qModel, 1200, 400, new Color( 255, 0, 255 ), "Frequency" );

		FreqBurst aBurst = new FreqBurst(
			4, // ramp start
			7, // ramp end
			
		int stepSamples = 10
		int stepStart = 4;
		int stepEnd	= 7;
		int postSamples = 20;
    double x=0;
    double iVal = 0;
    double qVal = 0;
    while(true) {
      x++;
      Packet tx = new Packet( new int[]{ 't','x' }, 8 ); // 0x74, 0x78
      Packet rx = new Packet( new int[]{ 'r','x' }, new int[]{ ); // 0x72, 0x78
      while(true) {
        int b = System.in.read();
        if (b != -1) {
        	p.add(b);
        	if (p.valid()) {
		        iVal = (double) p.data16( 0, 1 );
		        qVal = (double) p.data16( 2, 3 );
		        freq_re = (double) p.data16( 6, 7 );
		        freq_im = (double) p.data16( 8, 9 );
		        adc_in = (double) p.data16( 10, 11 );
		        break;
		      }
        }
      }
      System.out.println("phase_re: "+phase_re+", phase_im: "+phase_im+", freq_re: "+freq_re+", freq_im: "+freq_im+", adc_in: "+adc_in);
      if (x > 300) {
      	iModel.clear();
      	qModel.clear();
      	x = 0;
      }
      iModel.plotPoint( x, Math.atan2(phase_re, phase_im)*10 );
      qModel.plotPoint( x, Math.atan2(freq_re, freq_im)*10 );
      //shiftLines( iModel, x, Math.atan2(phase_re, phase_im)*10, 50 );
      //shiftLines( qModel, x, Math.atan2(freq_re, freq_im)*10, 50 );
      // textView.refresh();
      iWindow.refresh();
      qWindow.refresh();
      Thread.sleep(50);
    }


  }
}
