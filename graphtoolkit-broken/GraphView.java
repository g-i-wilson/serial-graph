package graphtoolkit;

import graphtoolkit.shapes.*;
import graphtoolkit.shapes.Point;
import java.util.*;

public abstract class GraphView {

  // data structure
  private GraphModel model;
  private PixelMap pixels;
  private int xResolution;
  private int yResolution;
  private double scaleFactor;

  // constructor
  public GraphView ( GraphModel m, PixelMap p, int x, int y ) {
    model = m;
    pixels = p;
    setResolution( x, y );
  }

  public GraphModel model () {
    return model;
  }

  /*
    - refresh() is a method to refresh the PixelMap object.
    - Method should be called whenever the GraphModel object changes.
    - The child class that extends this abstract class may cause graphics to
      be written to a PixelMap or JPanel object by calling the repaint() method.
      The repaint() method can be called either by the GraphView child object
      (when the GraphModel object has changed) or by user events (such as
      resizing the window).
  */
  public void refresh () {
    calculateScaleFactor();
    List<Line> lines = new ArrayList<>();
    for (Line l : grid()) lines.add( l );
    for (Line l : model.getLines()) lines.add( transformLine(l) );
    pixels.setLines( lines );
    pixels.repaint();
  }

  /*
    - grid() is an abstract method to draw the background grid Lines.
  */
  public abstract List<Line> grid ();


  // set the screen resolution
  public void setResolution ( int x, int y ) {
    xResolution = x;
    yResolution = y;
    calculateScaleFactor();
    pixels.setSize(xResolution, yResolution);
  }

  // recalculate the scaleFactor
  public void calculateScaleFactor () {
    // model deltas
    double deltaX = model.maxX()-model.minX();
    double deltaY = model.maxY()-model.minY();
    // guard against a divide-by-zero
    if (deltaX == 0 || deltaY == 0) {
      scaleFactor = 0;
    } else {
      double xFactor = (double)(xResolution-1) / deltaX;
      double yFactor = (double)(yResolution-1) / deltaY;
      scaleFactor = Math.min( xFactor, yFactor );
    }
  }

  public Line gridX ( double yIntercept ) {
    return new Line( 0, translateY(yIntercept), xResolution, translateY(yIntercept) );
  }

  public Line gridY ( double xIntercept ) {
    return new Line( translateX(xIntercept), 0, translateX(xIntercept), yResolution );
  }

  // String displaying all lines
  public String toString() {
    String output = "";
    for (Line l : model.getLines()) {
      output += l + "\n";
    }
    return output;
  }

  // shift x axis to the right (and scale)
  public double translateX (double x) {
    return (-model.minX() + x) * scaleFactor;
  }

  // invert y axis (and scale)
  public double translateY (double y) {
    return (model.maxY() - y) * scaleFactor;
  }

  // apply x and y translations to a Point
  public Point translatePoint ( Point modelPoint ) {
    return new Point(
      translateX( modelPoint.getX() ),
      translateY( modelPoint.getY() )
    );
  }

  // apply Point translations to create a Line transformation
  public Line transformLine ( Line modelLine ) {
    Line newLine = new Line(
      translatePoint( modelLine.firstPoint() ),
      translatePoint( modelLine.secondPoint() )
    );
    return newLine;
  }

}
