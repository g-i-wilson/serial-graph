package graphtoolkit;

import graphtoolkit.shapes.*;
import java.util.*;

public class GraphViewText extends GraphView {

  // constructor
  public GraphViewText ( GraphModel model, int x, int y ) {
    super( model, new StringPixelMap(x,y), x, y );
  }

  // draw grid lines
  public List<Line> grid () {
    List<Line> gridLines = new ArrayList<>();
    gridLines.add( gridX(0) );
    gridLines.add( gridY(0) );
    return gridLines;
  }

}
