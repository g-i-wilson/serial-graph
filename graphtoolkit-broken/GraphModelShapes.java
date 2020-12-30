package graphtoolkit;

import graphtoolkit.shapes.*;
import java.awt.Color;

public class GraphModelShapes extends GraphModel {

  private Point lastPoint;
  private Color color;

  public GraphModelShapes ( double x0, double y0, double x1, double y1, Color c ) {
    super( x0, y0, x1, y1 );
    color = c;
  }

  public GraphModelShapes () {
    this( 0.0, 0.0, 0.0, 0.0, new Color( 255, 255, 0 ) );
  }

  public void setColor ( Color c ) {
    color = c;
  }

  // Override the addLine method in the parent
  public void addLine ( Point p0, Point p1 ) {
    addLine( new ColorLine( p0, p1, color ) );
  }

  /*
    - Shapes start out at location 0,0.
    - We can translate the Shape (acting as a Point) either before
      or after we plot it.
  */
  public void plotPoint ( double x, double y, Shape shape ) {
    shape.translate( x, y );
    plotPoint( shape );
    addShape( shape );
  }

  /*
    - addShape() simply adds all of the Lines to the GraphModel.
  */
  public void addShape ( Shape shape ) {
    for (ColorLine l : shape.getLines()) addLine( l );
  }

}
