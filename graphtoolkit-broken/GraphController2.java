package graphtoolkit;

import graphtoolkit.shapes.*;
import java.util.*;
import java.awt.Color;

public class GraphController2 {
  public static void main (String[] args) throws Exception {

    GraphModelShapes model = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));

    GraphView textView = new GraphViewText( model, Integer.valueOf(args[0]), Integer.valueOf(args[1]) );
    GraphView guiView = new GraphViewGUI( model, Integer.valueOf(args[2]), Integer.valueOf(args[3]), new Color( 0, 255, 0 ) );


    Color shapeColor = new Color( 255, 255, 0 ); // yellow

    Shape s0 = new Parallelogram( 100, 80, 50, shapeColor );
    s0.translate(-100,100);
    model.addShape(s0);

    Shape s1 = new Trapezoid( 100, 150, 80, 0, shapeColor );
    s1.translate(100,100);
    model.addShape(s1);

    RightTriangle s2 = new RightTriangle( -120, 80, shapeColor );
    s2.translate(100,-100);
    model.addShape(s2);

    EquilateralTriangle s3 = new EquilateralTriangle( 100, shapeColor );
    s3.translate(-100,-100);
    model.addShape(s3);

    textView.refresh();
    guiView.refresh();

  }
}
