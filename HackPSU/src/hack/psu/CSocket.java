package hack.psu;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CSocket
{
	public CSocket()
	{
		socket=new Socket();
	}
	
	public boolean connect() throws IOException
	{
		socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 7777),5000);
		return(socket.isConnected());
	}
	
	public void sendByte(byte b) throws IOException
	{
		out.write(b);
	}
	
	public void writeBuffer(Color[] buffer) throws IOException
	{
		for(int i=0;i<buffer.length;i++)
		{
			Color c=buffer[i];
			out.write(c.getRed());
			out.write(c.getGreen());
			out.write(c.getBlue());
		}
	}
	
	private Socket socket;
	private InputStream in;
	private OutputStream out;
}
