import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.*;
import java.io.File;
import paddle.*;


public class ServePulses {
  public static void main(String[] args) throws Exception {

    PulseData c = new PulseData();

    Server displayData = new Server( c, 9000 );
    Server receiveData = new Server( c, 9001 );
    //Server p3 = new Server( c, Integer.valueOf(args[2]) );


    while(true) {
      Thread.sleep(1000);
    }


  }

}


class PulseData extends ServerState {

  private List<List<Integer>> pulses;
  private File dataFile;
  
  public PulseData () {
  	this(
			LocalDateTime.now().format(
		  	DateTimeFormatter.ofPattern("dd-MM-yyyy_HHmmss")
		  )+".csv"
    );
  }

  public PulseData ( String fileName ) {
  	dataFile = new File( fileName );
  	pulses = new ArrayList<>();
    System.out.println( "PulseData initialized!" );
  }
  
  public void addData ( String data ) {
  	System.out.println( data );
  	if (data.equals("")) return;
  	String[] dataPoints = data.split(",");
  	List<Integer> subList = new ArrayList<>();
		for (String point : dataPoints) {
			System.out.println( "> "+point );
			if (!point.equals("")) subList.add( Integer.valueOf( point ) );
		}
  	try {
			Files.write(dataFile.toPath(), (data+",").getBytes(), StandardOpenOption.APPEND);
			Files.write(dataFile.toPath(), "\n".getBytes(), StandardOpenOption.APPEND);
		} catch (Exception e) {
			System.out.println( "ERROR: couldn't write to "+dataFile );
			e.printStackTrace();
		}
  	pulses.add( 0, subList ); // add at beginning
  }

  public void createResponse ( Request req, Response res ) {
  	if( req.socket().getLocalPort() == 9000 ) {
  		//System.out.println( req.data() );
  		addData( req.data() );
  		res.setBody( "<h1>Thanks!</h1><br><br>"+pulses );
  	} else {
//  		String body = "<h1>Data:</h1><br>"+pulses;
  		String body =
  		"<!DOCTYPE html>\n" +
			"<head>\n" +
			"<meta charset=\"utf-8\" />\n" +
			"</head>\n" +
			"<body>\n" +
			"<div id='plot_div'></div>\n" +
			"<script src='https://cdn.plot.ly/plotly-latest.min.js'></script>\n" +
			"<script>\n" +
			"Plotly.newPlot( 'plot_div', [ \n";
  		if (pulses.size() > 0) {
				for (List<Integer> pulse : pulses) {
					body += "{type:\"bar\", opacity:\"0.2\", color:\"blue\", y:"+pulse+"},\n";
				}
			}
			body += " ], {\"barmode\":\"overlay\"} );\n" +
			"</script>\n" +
			"</body>\n" +
			"</html>\n";
  		res.setBody( body );
  	}
  }

}
