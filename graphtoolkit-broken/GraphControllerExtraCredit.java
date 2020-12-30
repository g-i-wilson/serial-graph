package graphtoolkit;

import graphtoolkit.shapes.*;
import java.util.*;
import java.awt.Color;

public class GraphControllerExtraCredit {
  public static void main (String[] args) throws Exception {

    //GraphModelShapes model;
	GraphModelShapes model = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));
    //GraphView view;
    GraphView view = new GraphViewGUI( model, Integer.valueOf(args[0]), Integer.valueOf(args[1]), new Color( 0, 255, 0 ) );

	double[] coef = new double[]{1.0,1.2,1.3,1.4};


	for (double i=0; i<4; i+=.5) {

		System.out.println( "i = "+i );
		//model = new GraphModelShapes(50.0, 50.0, -50.0, -50.0, new Color( 0, 127, 255 ));
    	//view = new GraphViewGUI( model, Integer.valueOf(args[0]), Integer.valueOf(args[1]), new Color( 0, 255, 0 ) );

		for (double x=-100; x<100; x++) {
		  double y = 10*(
		  	Math.sin(x * 1/(coef[0]+i)) +
		  	Math.sin(x * 1/(coef[1]+i)) +
		  	Math.sin(x * 1/(coef[2]+i)) +
		  	Math.sin(x * 1/(coef[3]+i))
		  );
		  model.plotPoint( x, y );
		  view.refresh();

		}

		Thread.sleep(1000);
		model.clear();

    }

  }
}
