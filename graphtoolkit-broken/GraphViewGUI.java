package graphtoolkit;

import graphtoolkit.shapes.*;
import java.util.*;
import java.awt.Color;

public class GraphViewGUI extends GraphView {

  // private double xGrid;
  // private double yGrid;
  private Color gridColor;

  // constructor
  // public GraphViewGUI ( GraphModel model, int xSize, int ySize, double xGrid, double yGrid ) {
  public GraphViewGUI ( GraphModel model, int xSize, int ySize, Color gridColor, String windowTitle ) {
    super(
      model,
      new JPanelPixelMap(
        xSize, ySize,
        new Color(255,255,0),
        new Color(0,0,0),
        windowTitle
      ),
      xSize, ySize
    );
    // this.xGrid = xGrid;
    // this.yGrid = yGrid;
    this.gridColor = gridColor;
  }

  // draw grid lines
  public List<Line> grid () {
    ArrayList<Line> gridLines = new ArrayList<>();
    // // X grid lines
    // for (double x=(-xGrid); x>model().minX(); x-=xGrid)
    //   gridLines.add( new ColorLine( gridY( x ), new Color(0,50,0) ) );
    // for (double x=xGrid; x<model().maxX(); x+=xGrid)
    //   gridLines.add( new ColorLine( gridY( x ), new Color(0,50,0) ) );
    // // Y grid lines
    // for (double y=(-yGrid); y>model().minY(); y-=yGrid)
    //   gridLines.add( new ColorLine( gridX( y ), new Color(0,50,0) ) );
    // for (double y=yGrid; y<model().maxY(); y+=yGrid)
    //   gridLines.add( new ColorLine( gridX( y ), new Color(0,50,0) ) );
    // Axes
    gridLines.add( new ColorLine( gridX(0), gridColor ) );
    gridLines.add( new ColorLine( gridY(0), gridColor ) );
    return gridLines;
  }

  // Override transformLine(Line) method in GraphView
  public Line transformLine ( Line line ) {
    try {
      ColorLine colorLine = (ColorLine) line;
      Line newLine = new ColorLine(
        translatePoint( colorLine.firstPoint() ),
        translatePoint( colorLine.secondPoint() ),
        colorLine.color()
      );
      return newLine;
    } catch (ClassCastException e) {
      return super.transformLine( line );
    }
  }

}
