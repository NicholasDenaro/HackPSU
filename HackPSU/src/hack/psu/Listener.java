package hack.psu;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread
{
	@Override
	public void run()
	{
		try
		{
			ServerSocket listener=new ServerSocket();
			listener.bind(new InetSocketAddress(InetAddress.getLocalHost(), 7777));
			while(true)
			{
				Socket soc=listener.accept();
				InputStream in=soc.getInputStream();
				int input;
				while((input=in.read())!=-1)
				{
					System.out.println("Server: "+input);
				}
			}
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
