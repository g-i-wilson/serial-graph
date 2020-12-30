package graphtoolkit.shapes;

public class Point {

  // data structure
  private double x;
  private double y;


  // constructor
  public Point ( double initX, double initY ) {
    x = initX;
    y = initY;
  }

  // method to access x variable
  public double getX () {
    return x;
  }

  // method to access y variable
  public double getY() {
    return y;
  }

  // method to change x and y variables
  public void translate ( double addX, double addY ) {
    x += addX;
    y += addY;
  }

  // Overrides the toString() method inherited from Object
  public String toString () {
    return "(" + x + ", " + y + ")";
  }

}
