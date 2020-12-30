package graphtoolkit;

import graphtoolkit.shapes.*;
import java.util.List;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.*;

public class JPanelPixelMap extends JPanel implements PixelMap {

  private List<Line> lines;
  private JFrame window;
  private Color foregroundColor;
  private Color backgroundColor;

  // constructor
  public JPanelPixelMap ( int x, int y, Color fc, Color bc, String windowTitle ) {
    // set default foreground color
    foregroundColor = fc;
    // set default background color
    backgroundColor = bc;
    // setBackground() method is inherited from JPanel
    setBackground( backgroundColor );
    // create a JFrame object (window in the OS)
    window = new JFrame( windowTitle );
    // set default resolution
    setSize( x, y );
    // set initial window settings
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.getContentPane().add( this ); // add a reference to "this" in the window's content
    window.setLocation( 100, 100 ); // start position on screen
    window.setVisible( true ); // show the window
  }

  // REQUIRED BY PixelMap
  public void setLines ( List<Line> l ) {
    lines = l;
  }

  // REQUIRED BY PixelMap
  public void setSize ( int x, int y ) {
    window.setSize( x , y + 22 ); // add 22 pixels to account for the top bar
  }

  // REQUIRED BY PixelMap
  // public void repaint() is already inherited from JPanel


  /*
    - We are required to override this method in JPanel.
    - This method is called by the window environment whenever
      the window is resized.
    - The Graphics argument is passed by the window environment.
  */
  protected void paintComponent ( Graphics g ) {
    // Graphics object is passed to this object by the operating system.

    // Call the paintComponent() method in the parent class (JPanel).
    super.paintComponent( g );
    // Typecast the Graphics object like a Graphics2D object.
    Graphics2D g2 = (Graphics2D) g;
    // Turn on antialiasing to make the lines look better.
    g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

    // Draw lines.
    drawLines( g2 );
  }


  /*
    - This method adds all the Lines or ColorLines to a Graphics2D object.
  */
  private void drawLines (Graphics2D g2) {
    if (lines == null) return;
    for (Line line : lines) {
      try {
        // Try to cast the Line object as a ColorLine object
        ColorLine colorLine = (ColorLine) line;
        g2.setColor( colorLine.color() );
        g2.draw( convertLine( colorLine ) );
      } catch (ClassCastException e) {
        // Line is not actually a ColorLine
        g2.setColor( foregroundColor );
        g2.draw( convertLine( line ) );
      } catch (Exception e) {
        // Any unknown exception
        e.printStackTrace();
      }
    }
  }


  /*
    - Create a Line2D.Double from a Line.
  */
  private Line2D.Double convertLine ( Line line ) {
    Line2D.Double awtLine = new Line2D.Double(
      new Point2D.Double( line.firstPoint().getX(), line.firstPoint().getY() ),
      new Point2D.Double( line.secondPoint().getX(), line.secondPoint().getY() )
    );
    return awtLine;
  }


}
