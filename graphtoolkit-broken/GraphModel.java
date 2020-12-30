package graphtoolkit;

import graphtoolkit.shapes.*;
import java.util.*;

public class GraphModel {

  // holds a reference to the last plotted Point
  Point lastPoint;
  // List (interface) of Line objects
  ArrayList<Line> allLines;
  // the extrema of all the Points
  double maxX;
  double maxY;
  double minX;
  double minY;

  // constructor
  public GraphModel () {
    // initialize variables
    this(0.0, 0.0, 0.0, 0.0);
  }

  public GraphModel ( double x0, double y0, double x1, double y1 ) {
    // initialize variables
    allLines = new ArrayList<Line>();
    maxX = x0;
    maxY = y0;
    minX = x1;
    minY = y1;
  }

  // compare the extrema to the point, and resize extrema if needed
  private void checkExtrema ( Point point ) {
    if (point.getX() > maxX) maxX = point.getX();
    if (point.getX() < minX) minX = point.getX();
    if (point.getY() > maxY) maxY = point.getY();
    if (point.getY() < minY) minY = point.getY();
  }

  // add a new Line object to the allLines List
  public void addLine ( Line line ) {
    allLines.add( line );
    checkExtrema( line.firstPoint() );
    checkExtrema( line.secondPoint() );
  }

  // add a new Line object from 2 Point objects
  public void addLine ( Point p0, Point p1 ) {
    addLine( new Line( p0, p1 ) );
  }

  // line segment from the currentPoint to a new Point
  public void plotPoint ( Point point ) {
    if (lastPoint != null) addLine( lastPoint, point );
    lastPoint = point;
  }

  // line segment from the currentPoint to a new Point
  public void plotPoint ( double x, double y ) {
    Point point = new Point( x, y );
    plotPoint( point );
  }

  public void endPlot () {
    lastPoint = null;
  }

  public ArrayList<Line> getLines () {
    return allLines;
  }

  public void clear () {
  	allLines = new ArrayList<Line>();
  	lastPoint = null;
  }

  public double maxX () {
    return maxX;
  }
  public double minX () {
    return minX;
  }
  public double maxY () {
    return maxY;
  }
  public double minY () {
    return minY;
  }

  public String toString() {
    String output = "";
    for (Line l : allLines) {
      output += l + "\n";
    }
    return output;
  }

}
