package hack.psu;

/*
 * Copyright (c) 2004 David Flanagan.  All rights reserved.
 * This code is from the book Java Examples in a Nutshell, 3nd Edition.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose,
 * including teaching and use in open-source projects.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, or to purchase the book, 
 * please visit http://www.davidflanagan.com/javaexamples3.
 */
//package je3.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This program is a very simple Web server. When it receives a HTTP request it
 * sends the request back as the reply. This can be of interest when you want to
 * see just what a Web client is requesting, or what data is being sent when a
 * form is submitted, for example.
 */
public class HttpMirror
{
  public static void main(String args[]) {
    try {
      // Get the port to listen on
      int port = 9400;//Integer.parseInt(args[0]);
      // Create a ServerSocket to listen on that port.
      ServerSocket ss = new ServerSocket(port);
      // Now enter an infinite loop, waiting for & handling connections.
      for (;;) {
        // Wait for a client to connect. The method will block;
        // when it returns the socket will be connected to the client
        Socket client = ss.accept();

        // Get input and output streams to talk to the client
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream());

        // Start sending our reply, using the HTTP 1.1 protocol
        out.print("HTTP/1.1 200 \r\n"); // Version & status code
        out.print("Content-Type: text/plain\r\n"); // The type of data
        out.print("Connection: close\r\n"); // Will close stream
        out.print("\r\n"); // End of headers

        // Now, read the HTTP request from the client, and send it
        // right back to the client as part of the body of our
        // response. The client doesn't disconnect, so we never get
        // an EOF. It does sends an empty line at the end of the
        // headers, though. So when we see the empty line, we stop
        // reading. This means we don't mirror the contents of POST
        // requests, for example. Note that the readLine() method
        // works with Unix, Windows, and Mac line terminators.
        String line;
        String direction=null;
        String host=null;
        while ((line = in.readLine()) != null) {
          if (line.length() == 0)
            break;
          //System.out.println(line);
          out.print(line + "\r\n");
          if(line.substring(0,3).equals("GET"))
          {
        	  line=line.substring(6);
        	  String get=line.substring(0, line.indexOf("="));
        	  if(get.equals("direction"))
        	  {
        		  line=line.substring(line.indexOf("=")+1);
        		  direction=line.substring(0, line.indexOf(" "));
        	  }
          }
          if(line.substring(0,4).equals("Host"))
          {
        	  if(direction!=null)
        		  host=line.substring(line.indexOf(" ")+1);
          }
        }
        
        System.out.println("host: "+host);
        System.out.println("direction: "+direction);

        System.out.println("-______________________________-");
        // Close socket, breaking the connection to the client, and
        // closing the input and output streams
        out.close(); // Flush and close the output stream
        in.close(); // Close the input stream
        client.close(); // Close the socket itself
      } // Now loop again, waiting for the next connection
    }
    // If anything goes wrong, print an error message
    catch (Exception e) {
      System.err.println(e);
      System.err.println("Usage: java HttpMirror <port>");
    }
  }
}