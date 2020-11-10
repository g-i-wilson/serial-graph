import graphtoolkit.*;
import java.util.*;
import java.awt.Color;

public class SerialGraphController {

  private static double byteToDouble (int b) {
    double d = 0;
    if ((0xffffff7f | b) == 0xffffff7f) { // positive value
      d = (double)b;
    } else if ((0x00000080 & b) == 0x00000080) { // negative value
      d = (double)(0xffffff00 | b); // make sure all higher bits are 1s
    }
    return d;
  }
  
  
  public static void shiftLines( GraphModel g, double x, double y, int maxLines ) {
  	g.plotPoint( x, y );
  	if (g.getLines().size() > maxLines) g.getLines().remove(0);
  }


  public static void main (String[] args) throws Exception {

    GraphModelShapes phaseModel = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));
    GraphModelShapes phaseDerModel = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));

    // GraphView textView = new GraphViewText( phaseModel, Integer.valueOf(args[0]), Integer.valueOf(args[1]) );
    GraphView phaseWindow = new GraphViewGUI( phaseModel, 1200, 400, new Color( 0, 255, 0 ), "Phase" );
    GraphView phaseDerWindow = new GraphViewGUI( phaseDerModel, 1200, 400, new Color( 255, 0, 255 ), "Frequency" );

    double x=0;
    double phase_re = 0;
    double phase_im = 0;
    double freq_re = 0;
    double freq_im = 0;
    double adc_in = 0;
    while(true) {
      x++;
      PacketRx p = new PacketRx( new int[]{ 1, 2 }, 13 );
      while(true) {
        int b = System.in.read();
        if (b != -1) {
        	p.add(b);
        	if (p.valid()) {
		        phase_re = (double) p.data16( 2, 3 );
		        phase_im = (double) p.data16( 4, 5 );
		        freq_re = (double) p.data16( 6, 7 );
		        freq_im = (double) p.data16( 8, 9 );
		        adc_in = (double) p.data16( 10, 11 );
		        break;
		      }
        }
      }
      System.out.println("phase_re: "+phase_re+", phase_im: "+phase_im+", freq_re: "+freq_re+", freq_im: "+freq_im+", adc_in: "+adc_in);
      if (x > 300) {
      	phaseModel.clear();
      	phaseDerModel.clear();
      	x = 0;
      }
      phaseModel.plotPoint( x, Math.atan2(phase_re, phase_im)*10 );
      phaseDerModel.plotPoint( x, Math.atan2(freq_re, freq_im)*10 );
      //shiftLines( phaseModel, x, Math.atan2(phase_re, phase_im)*10, 50 );
      //shiftLines( phaseDerModel, x, Math.atan2(freq_re, freq_im)*10, 50 );
      // textView.refresh();
      phaseWindow.refresh();
      phaseDerWindow.refresh();
      Thread.sleep(50);
    }


  }
}
