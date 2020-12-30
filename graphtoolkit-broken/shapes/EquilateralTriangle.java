package graphtoolkit.shapes;

import java.awt.Color;

public class EquilateralTriangle extends RegularShape {

  private static double calculateRadius ( double length ) {
    return length * 1/Math.sqrt(3);
  }

  public EquilateralTriangle ( double length, Color color ) {
    super( calculateRadius(length), 3, 90.0, color );
  }

}
