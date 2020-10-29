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


  public static void main (String[] args) throws Exception {

    GraphModelShapes phaseModel = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));
    GraphModelShapes phaseDerModel = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));

    // GraphView textView = new GraphViewText( phaseModel, Integer.valueOf(args[0]), Integer.valueOf(args[1]) );
    GraphView phaseWindow = new GraphViewGUI( phaseModel, 1200, 300, new Color( 0, 255, 0 ) );
    GraphView phaseDerWindow = new GraphViewGUI( phaseDerModel, 1200, 300, new Color( 255, 0, 255 ) );

    double x=0;
    double y1 = 0;
    double y0 = 0;
    double yDer = 0;
    while(true) {
      x++;
      while(true) {
        int b = System.in.read();
        if (b != -1) {
          y0 = y1;
          y1 = byteToDouble(b);
          break;
        }
      }
      yDer = y1-y0;
      if (yDer > 50 || yDer < -50) yDer = y1;
      phaseModel.plotPoint( x*10, y1*20 );
      phaseDerModel.plotPoint( x*10, yDer*20 );
      // textView.refresh();
      phaseWindow.refresh();
      phaseDerWindow.refresh();
      Thread.sleep(50);
    }


  }
}
