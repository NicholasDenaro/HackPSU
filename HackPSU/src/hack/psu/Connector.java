package hack.psu;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Connector extends Thread
{
	private OutputStream out;
	private ByteBuffer buffer;
	
	@Override
	public void run()
	{
		try
		{
			boolean running=true;
			Socket soc=new Socket();
			soc.connect(new InetSocketAddress("127.0.0.1", 7777));
			System.out.println("connected");
			out=soc.getOutputStream();
			while(running)
			{
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	synchronized public void sendBuffer(int[] buff, int width) throws IOException
	{
		
		/*swap(buff, 15, 29);
		swap(buff, 16, 28);
		swap(buff, 17, 27);
		swap(buff, 18, 26);
		swap(buff, 19, 25);
		swap(buff, 20, 24);
		swap(buff, 21, 23);
		
		swap(buff, 45, 59);
		swap(buff, 46, 58);
		swap(buff, 47, 57);
		swap(buff, 48, 56);
		swap(buff, 49, 55);
		swap(buff, 50, 54);
		swap(buff, 51, 53);*/
		
		//out.write(255);
		//out.write(255);
		//out.write(0);
		
		buffer=ByteBuffer.allocate(buff.length*3);
		
		int[][] arr=new int[width][buff.length/width];
		int x=0;
		for(int i=0;i<arr[0].length;i++)
		{
			for(int j=0;j<arr.length;j++)
			{
				arr[j][i]=buff[x++];
			}
		}
		
		for(int i=1;i<arr[0].length;i+=2)
		{
			for(int j=0;j<arr.length/2;j++)
			{
				int temp=arr[j][i];
				arr[j][i]=arr[arr.length-j-1][i];
				arr[arr.length-j-1][i]=temp;
			}
		}
		
		for(int i=0;i<arr[0].length;i++)
		{
			for(int j=0;j<arr.length;j++)
			{
				Color color=new Color(arr[j][i]);
				buffer.put((byte) color.getRed());
				buffer.put((byte) color.getGreen());
				buffer.put((byte) color.getBlue());
			}
		}
		
		//System.out.println("lights length: "+buff.length);
		
		//this works, I think?
		/*for (int i = 0; i < buff.length; i++)
		{
			Color color = new Color(buff[i]);
			buffer.put((byte) color.getRed());
			buffer.put((byte) color.getGreen());
			buffer.put((byte) color.getBlue());
		}*/
		
		out.write(buffer.array());
		
		/*for (int i = 0; i < buff.length; i++)
		{
			Color color = new Color(buff[i]);
			if(i==0)
			{
				System.out.println("Color: "+color);
				System.out.println("B: "+(byte)(color.getBlue()));
				System.out.println("R: "+(byte)(color.getRed()));
				System.out.println("G: "+(byte)(color.getGreen()));
			}
			out.write((byte)(color.getBlue()*0.1));
			out.write((byte)(color.getRed()*0.1));
			out.write((byte)(color.getGreen()*0.1));
			out.flush();
		}*/
		/*int[][] arr=new int[width][buff.length/width];
		int x=0;
		for(int i=0;i<arr[0].length;i++)
		{
			for(int j=0;j<arr.length;j++)
			{
				arr[j][i]=buff[x++];
			}
		}
		
		for(int i=1;i<arr[0].length;i+=2)
		{
			for(int j=0;j<arr.length/2;j++)
			{
				int temp=arr[j][i];
				arr[j][i]=arr[arr.length-j-1][i];
				arr[arr.length-j-1][i]=temp;
			}
		}
		for(int i=0;i<arr[0].length;i++)
		{
			for(int j=0;j<arr.length;j++)
			{
				Color color=new Color(arr[j][i]);
				out.write((byte)color.getBlue()); //blue
				out.write((byte)color.getRed()); //red
				out.write((byte)color.getGreen()); // green
				out.flush();
			}
		}*/
		
		/*for(int i=0;i<buff.length;i++)
		{
			Color color=new Color(buff[i]);
			out.write((byte)color.getBlue()); //blue
			out.write((byte)color.getRed()); //red
			out.write((byte)color.getGreen()); // green
			out.flush();
		}*/
	}
	
	public static void swap(int[] buff, int indexA, int indexB)
	{
		int temp = buff[indexA];
		buff[indexA] = buff[indexB];
		buff[indexB] = temp;
	}
}
