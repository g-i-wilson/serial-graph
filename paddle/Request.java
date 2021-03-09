package paddle;

import java.util.*;
import java.io.*;
import java.net.*;


public class Request {

	// Connection
	Socket socket;
	int sessionId;

	// HTTP request elements
	String type = "";
	String firstLine = "";
	StringMap1D<String> header = new StringMap1D<>();

	// Query data sent with the request (first line of header or body)
	String data = "";

	// Path requested (first line of header)
	String path = "";


	public Request ( Socket s, int id ) {

		socket = s;
		sessionId = id;

		try {

			// open an input stream
			InputStream stream = socket.getInputStream();

			// read HTTP header (just bytes for now...)
			byte[] byteBuffer = new byte[1024];
			byte[] shiftUTF = new byte[8];
			byte[] shiftASCII = new byte[4];
			int header_length = 0;
			int http_state = 0;
			while (header_length < 1024) { // cuts off at 1kB
				// UTF shift register
				shiftUTF[0]=shiftUTF[1];
				shiftUTF[1]=shiftUTF[2];
				shiftUTF[2]=shiftUTF[3];
				shiftUTF[3]=shiftUTF[4];
				shiftUTF[4]=shiftUTF[5];
				shiftUTF[5]=shiftUTF[6];
				shiftUTF[6]=shiftUTF[7];
				shiftUTF[7]=(byte) stream.read(); // next byte from socket
				// ASCII shift register (mirrors upper 4 bytes of UTF register )
				shiftASCII[0]=shiftUTF[4];
				shiftASCII[1]=shiftUTF[5];
				shiftASCII[2]=shiftUTF[6];
				shiftASCII[3]=shiftUTF[7];
				// 1kB byte buffer
				byteBuffer[header_length++] = shiftUTF[7];
				// test state of shift registers
				String testUTF = new String( shiftUTF );
				String testASCII = new String( shiftASCII );
				// events based on shift register states
				if (testUTF.equals("GET ") || testASCII.equals("GET ")) type = "GET";
				else if (testUTF.equals("POST") || testASCII.equals("POST")) type = "POST";
				else if (testUTF.equals("\r\n") || testASCII.equals("\r\n")) { if (type.equals("GET")) break; }
				else if (testUTF.equals("\r\n\r\n") || testASCII.equals("\r\n\r\n")) break;
			}

			// parse HTTP header (...and now we convert it to a String.)
			String whole_header = new String( byteBuffer );
			// split on returns
			String[] split_header = whole_header.split("\r\n");
			// first line
			firstLine = split_header[0];
			// header key-value pairs
			for (int i=1; i<split_header.length; i++) {
				String[] split_tuple = split_header[i].split(": ");
				if (split_tuple.length > 1) {
					header.write( split_tuple[0], split_tuple[1] );
				}
			}
			// System.out.println( "["+sessionId+"] Request: "+firstLine+"\n"+header );


			// read HTTP body (read in bytes...)
			if (header.defined("Content-Length")) {
				// get Content-Length in an int
				int content_length = Integer.parseInt( header.read("Content-Length") );
				System.out.println( "["+sessionId+"] Request: Content-Length="+content_length );
				// read Content-Length bytes from the socket
				byte[] body_bytes = new byte[content_length];
				// ...now convert bytes to a String.
				if (stream.read( body_bytes ) < 1) {
					// check for zero bytes (0) or end of stream (-1)
					System.out.println( "["+sessionId+"] Request: body: no data received" );
				} else {
					// convert the body_bytes into the data String
					data = new String( body_bytes );
					System.out.println( "["+sessionId+"] Request: body: "+data );
				}
			} else if (type.equals("POST")) {
				System.out.println( "["+sessionId+"] Request: ERROR: Content-Length not defined" );
			}


			// GET request
			if (type.equals("GET")) {

				if (firstLine.contains("?")) {

					// read path
					path = firstLine
						.substring( 4, firstLine.indexOf("?") );

					// read data
					data = firstLine
						.substring( firstLine.indexOf("?")+1, firstLine.indexOf(" HTTP/1") );

				} else {

					// read path
					path = firstLine
						.substring( 4, firstLine.indexOf(" HTTP/1") );
				}

			// POST request
			} else if (type.equals("POST")) {

				// read path
				path = firstLine
					.substring( 5, firstLine.indexOf(" HTTP/1") );

			} else {

				System.out.println("["+sessionId+"] Request: didn't recognize GET or POST request.");

			}

		} catch (Exception e) {
			System.out.println("["+sessionId+"] Request: ERROR reading from socket");
			e.printStackTrace();
		}

	}

	public String firstLine () {
		return firstLine;
	}

	public Map<String,String> header () {
		return header.map();
	}

	public String data () {
		return data;
	}

	public String path () {
		return path;
	}

	public Socket socket () {
		return socket;
	}

	public int sessionId () {
		return sessionId;
	}

	public String toString () {
		return firstLine + "\n" + header.toString() + "\n" + data();
	}

}
