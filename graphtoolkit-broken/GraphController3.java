package graphtoolkit;

import graphtoolkit.shapes.*;
import java.util.*;
import java.awt.Color;

public class GraphController3 {
  public static void main (String[] args) throws Exception {

    GraphModelShapes model = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));

    GraphView view = new GraphViewGUI( model, Integer.valueOf(args[2]), Integer.valueOf(args[3]), new Color( 0, 255, 0 ) );


    for (double x=-50; x<=150; x+=10) {
      double y = 0.03*x*x - 3*x - 50;
      model.plotPoint( x, y, new EquilateralTriangle( 5, new Color( 255, 255, 0 ) ) );
      view.refresh();
      Thread.sleep(100);
    }

    // animate a hexegon
    RegularShape shape = new RegularShape( 50, 6, 30.0, new Color(127, 100, 255) );
    model.addShape( shape );
    for (double x=-50; x<150; x++) {
      double y = -0.03*x*x + 3*x + 50;
      shape.moveTo( x, y );
      view.refresh();
      Thread.sleep(200);
    }

  }
}
