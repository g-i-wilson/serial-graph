package graphtoolkit.shapes;

import java.awt.Color;

public class Trapezoid extends Shape {

  public Trapezoid ( double lengthTop, double lengthBase, double height, double offsetTop, Color color ) {
    super(
      new Point[]{
        new Point( -lengthTop/2+offsetTop/2, height/2 ), // top left corner
        new Point( lengthTop/2+offsetTop/2, height/2 ), // top right corner

        new Point( lengthBase/2-offsetTop/2, -height/2 ), // botton right corner
        new Point( -lengthBase/2-offsetTop/2, -height/2 ) // bottom left corner
      },
      color
    );
  }

}
