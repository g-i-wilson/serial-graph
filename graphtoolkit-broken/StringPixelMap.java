package graphtoolkit;

import graphtoolkit.shapes.*;
import java.util.*;

public class StringPixelMap implements PixelMap {

  private String[][] pixels;
  private List<Line> lines;
  private final String BACKGROUND_PIXEL = "."; //"\033[48;5;232m  ";
  private final String LINE_PIXEL = "#"; //"\033[48;5;11m  ";
  private final String NEWLINE = "\n"; //"\033[0m\n";
  private final String CLEAR = "\033[2J";


  public StringPixelMap ( int x, int y ) {
    setSize( x, y );
  }

  // set the size of the pixels 2D array
  // REQUIRED BY INTERFACE
  public void setSize ( int x, int y ) {
    pixels = new String[y][x];
  }

  // set a reference to the GraphModel object
  // REQUIRED BY INTERFACE
  public void setLines ( List<Line> l ) {
    lines = l;
  }

  // called whenever the screen needs to be refreshed
  // REQUIRED BY INTERFACE
  public void repaint () {
    // checkPixelSize();
    // blank the pixels 2D array
    for (int y=0; y<pixels.length; y++) {
      for (int x=0; x<pixels[0].length; x++) {
        pixels[y][x] = BACKGROUND_PIXEL;
      }
    }
    // draw each line in the pixels 2D array
    if (lines != null)
      for (Line l : lines)
        drawLine(l);
    // clear the terminal
    System.out.print( CLEAR );
    // print the pixels 2D array to the standard output
    for (String[] row : pixels) {
      for (String pixel : row) {
        System.out.print( pixel );
      }
      System.out.print( NEWLINE );
    }
  }

  // plot a line using pixels
  private void drawLine ( Line line ) {
    double x0 = line.firstPoint().getX();
    double y0 = line.firstPoint().getY();
    double x1 = line.secondPoint().getX();
    double y1 = line.secondPoint().getY();
    double deltaX = x1-x0;
    double deltaY = y1-y0;
    int xSign = (int) Math.signum(deltaX);
    int ySign = (int) Math.signum(deltaY);
    // plot the line
    if (Math.abs(deltaX) > Math.abs(deltaY)) {
      for ( int x=(int)Math.round(x0); x!=(int)Math.round(x1); x+=xSign ) {
        int y = (int)Math.round( deltaY/deltaX * (x-x0) + y0 );
        drawPixel(x,y);
      }
    } else {
      for ( int y=(int)Math.round(y0); y!=(int)Math.round(y1); y+=ySign ) {
        int x = (int)Math.round( deltaX/deltaY * (y-y0) + x0 );
        drawPixel(x,y);
      }
    }
  }

  // draw the pixel only if it's inside the bounds of the pixels 2D array
  private void drawPixel ( int x, int y ) {
    if (
      x > -1 &&
      x < pixels[0].length &&
      y > -1 &&
      y < pixels.length
    ) {
      pixels[y][x] = LINE_PIXEL;
    }
  }


}
