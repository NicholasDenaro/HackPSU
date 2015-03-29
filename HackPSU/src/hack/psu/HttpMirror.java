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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * This program is a very simple Web server. When it receives a HTTP request it
 * sends the request back as the reply. This can be of interest when you want to
 * see just what a Web client is requesting, or what data is being sent when a
 * form is submitted, for example.
 */
public class HttpMirror extends Thread
{
	public PrintWriter out;
	public CommandCenter center;
  
	public HttpMirror(CommandCenter center)
	{
		super();
		this.center=center;
	}
	
	@Override
	public void run()
	  {
		  try {
	      // Get the port to listen on
	      int port = 9400;//Integer.parseInt(args[0]);
	      // Create a ServerSocket to listen on that port.
	      ServerSocket ss = new ServerSocket(port);
	      System.out.println("HttpMirror bound!");
	      // Now enter an infinite loop, waiting for & handling connections.
	      for (;;) {
	        // Wait for a client to connect. The method will block;
	        // when it returns the socket will be connected to the client
	        Socket client = ss.accept();
	        
	        System.out.println("connection get!");
	        
	        // Get input and output streams to talk to the client
	        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        out = new PrintWriter(client.getOutputStream());
	
	        
	        // Start sending our reply, using the HTTP 1.1 protocol
	        out.print("HTTP/1.1 200 \r\n"); // Version & status code
	        out.print("Content-Type: text/plain\r\n"); // The type of data
	        out.print("Connection: close\r\n"); // Will close stream
	        out.print("Access-Control-Allow-Origin: *");
	        out.print("\r\n");
	        out.flush();// End of headers
	        // Now, read the HTTP request from the client, and send it
	        // right back to the client as part of the body of our
	        // response. The client doesn't disconnect, so we never get
	        // an EOF. It does sends an empty line at the end of the
	        // headers, though. So when we see the empty line, we stop
	        // reading. This means we don't mirror the contents of POST
	        // requests, for example. Note that the readLine() method
	        // works with Unix, Windows, and Mac line terminators.
	        String line;
	        String player=null;
	        String direction=null;
	        String host=null;
	        boolean movement=false;
	        while ((line = in.readLine()) != null) {
	          if (line.length() == 0)
	          {
	        	  //System.out.println("len=0");
	            break;
	          }
	          //System.out.println(line);
	          //out.print(line + "\r\n");
	          if(line.substring(0,3).equals("GET"))
	          {
	        	  //System.out.println("GET:"+line);
	        	  if(line.trim().equals("GET / HTTP/1.1"))
	        	  {
	        		  System.out.println("Serving page!!!");
	        		  servePage();
	        	  }
	        	  else
	        	  {
	        		  if(line.indexOf("=")==-1)//this MAY be an issue, look here!
	        			  break;
	        		  line=line.substring(line.indexOf("=")+1);
	        		  player=line.substring(0,1);
	        		  line=line.substring(line.indexOf("=")+1);
	        		  direction=line.substring(0,line.indexOf(" "));
	        		  movement=true;
		        	  /*line=line.substring(6);
		        	  String get=line.substring(0, line.indexOf("="));
		        	  if(get.equals("direction"))
		        	  {
		        		  movement=true;
		        		  line=line.substring(line.indexOf("=")+1);
		        		  direction=line.substring(0, line.indexOf(" "));
		        	  }*/
	        	  }
	          }
	          if(line.substring(0,4).equals("Host"))
	          {
	        	  //if(direction!=null)
	        		  host=line.substring(line.indexOf(" ")+1);
	          }
	        }
	        
	        //System.out.println("host: "+host);
	        //System.out.println("direction: "+direction);
	        if(player!=""&&movement&&host!=null)
	        {
	        	int retval=center.addPlayer(player);
		        if(retval!=-1)
		        {
		        	System.out.println("sending...");
		        	out.print("HTTP/1.1 200 OK\r\n"); // Version & status code
			        out.print("Content-Type: text/html\r\n"); // The type of data
			        out.print("Connection: close\r\n"); // Will close stream
			        out.print("Access-Control-Allow-Origin: *\r\n");
			        //out.print("Content-Length: "+("<html><body>{player: '"+retval+"'}</body></html>".length())+"\r\n");
			        out.print("Content-Length: 1\r\n");
			        out.print("\r\n");
		        	String output=""+retval;
		        	out.print(output);
		        	out.print("\r\n");
		        	out.flush();
		        }
		        if(direction!=null)
		        	center.movePlayer(player,direction);
	        }
	
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
	    	e.printStackTrace();
	      //System.err.println(e);
	      //System.err.println("Usage: java HttpMirror <port>");
	    }
  }
  
  private void servePage() throws IOException
	{
		String header="";
		header+="HTTP/1.1 200 OK\r\n";
		//header+="Connection: close\r\n";
		header+="Content-Type: text/html\r\n";
		header+="Date: "+new Date()+"\r\n";
		header+="Server: The Fileserver \r\n";
		
		String path=HttpMirror.class.getResource("/ClientGui.html").getPath();
		File f=new File(path);
		ByteArrayOutputStream buf=new ByteArrayOutputStream();
		FileInputStream fin=new FileInputStream(f);
		int data;
		while((data=fin.read())!=-1)
		{
			buf.write(data);
		}
		fin.close();
		
		System.out.println("buf.size(): "+buf.size());
		
		header+="Accept-Ranges: bytes\r\n";
		header+="Content-Length: "+buf.size()+"\r\n";
		//header+="Last-Modified: \r\n";
		header+="\r\n";
		
		header+=new String(buf.toByteArray()/*,"UTF-8"*/);
		
		//header+="\r\n";
		//header+="\r\n";
		
		buf.close();
		//out.write(buf.toByteArray());
		//out.write(header.getBytes());
		out.write(header);
		out.flush();
		out.close();
	}
}