package hack.psu;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Main
{
	public static void mains(String[] args)
	{
		Socket soc=new Socket();
		
		try
		{
			//soc.connect(new InetSocketAddress(InetAddress.getLocalHost(), 7777));
			soc.connect(new InetSocketAddress("127.0.0.1", 7777));
			System.out.println("connected");
			OutputStream out=soc.getOutputStream();
			int stupid;
			Color color;
			while (true)
			{
				stupid = Color.HSBtoRGB((float)Math.random(),(float)1.0,(float)1.0);
				color = new Color(stupid);
				for(int i=0;i<75;i++)
				{
					
					out.write((byte)color.getBlue()); //blue
					out.write((byte)color.getRed()); //red
					out.write((byte)color.getGreen()); // green
					out.flush();
				}
				Thread.sleep(5000);
			}
			//////////////////////////////////////FILE SHIT////////////////////////////////////////////
			/*File f=new File("example.txt");
			FileInputStream in=new FileInputStream(f);
			FileOutputStream out=new FileOutputStream(f);
			new Thread()
			{
				public void run()
				{
					boolean running=true;
					while(running)
					{
						int input;
						try
						{
							while((input=in.read())==-1);
							
							System.out.println("read: "+input);
						}
						catch(IOException e)
						{
							running=false;
							e.printStackTrace();
						}
					}
					try
					{
						in.close();
					}
					catch(IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
			
			for(int i=0;i<127;i++)
			{
				try
				{
					Thread.sleep(100);
				}
				catch(InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out.write(i);
			}
			out.close();*/
		}
		catch(IOException | InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
