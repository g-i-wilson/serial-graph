package graphtoolkit.shapes;

import java.util.*;
import java.awt.Color;

public class Shape extends Point {

  private List<ColorLine> lines;

  public Shape ( List<Point> pList, Color color ) {
    super(0.0, 0.0);
    lines = new ArrayList<ColorLine>();
    for (int i=0; i<pList.size()-1; i++) {
      lines.add( new ColorLine( pList.get(i), pList.get(i+1), color ) );
    }
    lines.add( new ColorLine( pList.get(pList.size()-1), pList.get(0), color ) );
  }

  public Shape ( Point[] pArray, Color color ) {
    super(0.0, 0.0);
    lines = new ArrayList<ColorLine>();
    for (int i=0; i<pArray.length-1; i++) {
      lines.add( new ColorLine( pArray[i], pArray[i+1], color ) );
    }
    lines.add( new ColorLine( pArray[pArray.length-1], pArray[0], color ) );
  }

  public List<ColorLine> getLines () {
    return lines;
  }

  public void translate ( double addX, double addY ) {
    super.translate( addX, addY );
    for (Line l : lines) {
      l.firstPoint().translate( addX, addY );  // each line shares a point
      // so we don't need to translate both points
    }
  }

  public void moveTo ( double x, double y ) {
    translate( x-getX(), y-getY() );
  }

  public String toString() {
    if (lines.size() < 1) return "[Empty Shape]";
    String output = this.getClass() + ":\n";
    for (Line line : lines) {
      output += line + "\n";
    }
    return output;
  }

}
