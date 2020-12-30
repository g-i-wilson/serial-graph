package graphtoolkit;

import graphtoolkit.shapes.*;
import java.util.*;
import java.awt.Color;

public class GraphController1 {
  public static void main (String[] args) throws Exception {

    GraphModelShapes model = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));

    GraphView textView = new GraphViewText( model, Integer.valueOf(args[0]), Integer.valueOf(args[1]) );
    GraphView guiView = new GraphViewGUI( model, Integer.valueOf(args[2]), Integer.valueOf(args[3]), new Color( 0, 255, 0 ) );


    for (double x=-50; x<=150; x+=10) {
      double y = 0.03*x*x - 3*x - 50;
      model.plotPoint( x, y );
      textView.refresh();
      guiView.refresh();
      Thread.sleep(500);
    }

  }
}
