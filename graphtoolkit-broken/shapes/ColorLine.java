package graphtoolkit.shapes;

import java.awt.*;

public class ColorLine extends Line {

  private Color color;

  public ColorLine ( Line l, Color color ) {
    this( l.firstPoint(), l.secondPoint(), color );
  }

  public ColorLine ( Point p1, Point p2, Color color ) {
    super( p1, p2 );
    this.color = color;
  }

  public ColorLine ( double x0, double y0, double x1, double y1, Color color ) {
    super( x0, y0, x1, y1 );
    this.color = color;
  }

  public void color ( Color c ) {
    color = c;
  }

  public Color color () {
    return color;
  }

}
