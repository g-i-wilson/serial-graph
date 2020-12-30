package graphtoolkit.shapes;

import java.awt.Color;

public class Triangle extends Shape {

  private Point centroid;

  public Triangle ( double x0, double y0, double x1, double y1, double x2, double y2, Color color ) {
    super( new Point[]{ new Point(x0,y0), new Point(x1,y1), new Point(x2,y2) }, color );
  }

}
