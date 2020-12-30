package graphtoolkit.shapes;

import java.util.*;
import java.awt.Color;

public class RegularShape extends Shape {

  private static double sin ( double angle ) {
    return Math.sin( Math.toRadians(angle) );
  }

  private static double cos ( double angle ) {
    return Math.cos( Math.toRadians(angle) );
  }

  private static List<Point> pointsList ( double radius, int sides, double angleOffset ) {
    List<Point> points = new ArrayList<Point>();
    for (int i=0; i<sides; i++) {
      double angle = 360.0 * (double)i/(double)sides;
      double x = cos(angle + angleOffset) * radius;
      double y = sin(angle + angleOffset) * radius;
      points.add( new Point(x,y) );
    }
    return points;
  }

  public RegularShape ( double radius, int sides, double angleOffset, Color color ) {
    // super( new ArrayList<Point>() );
    super( pointsList( radius, sides, angleOffset ), color );
  }

}
