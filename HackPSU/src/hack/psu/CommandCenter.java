package hack.psu;

import hack.psu.engine.Entity;
import hack.psu.engine.GameEngine;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;

//import hack.psu.game.*;

public class CommandCenter extends JFrame implements KeyListener, ActionListener, WindowListener
{
	private GameEngine ge;
	public boolean wait;
	private Process proc;
	private Entity leftPaddle;
	private Entity rightPaddle;
	private HttpMirror http;
	public String player1=null;
	public String player2=null;
	
	public CommandCenter(GameEngine ge)
	{
		super("Command Center");
		this.ge=ge;
		JButton connect=new JButton("Connect");
		connect.addActionListener(this);
		Container c=this.getContentPane();
		c.add(connect);
		wait=true;
		this.addWindowListener(this);
		connect.addKeyListener(this);
		for(Entity e:ge.ents)
		{
			if(e.getType().equals("Paddle"))
			{
				if(leftPaddle==null)
					leftPaddle=e;
				else
					rightPaddle=e;
			}
		}
		http=new HttpMirror(this);
		http.start();
	}
	
	public int addPlayer()
	{
		if(player1==null)
		{
			System.out.println("player 1 connected! ");
			player1="1";
			return(1);
		}
		else if(player2==null)
		{
			System.out.println("player 2 connected! ");
			player2="2";
			return(2);
		}
		return(-1);
	}
	
	public void movePlayer(String ip, String direction)
	{
		direction=direction.trim();
		System.out.println("player to move: "+ip);
		System.out.println("\tplayer1:"+player1);
		System.out.println("\tplayer2:"+player2);
		if(player1!=null&&player1.equals(ip))
		{
			System.out.println("player1 tried to move!");
			//System.out.println("direction:"+direction);
			if(direction.equals("up"))
				leftPaddleMove(-1);
			else if(direction.equals("down"))
				leftPaddleMove(1);
		}
		else if(player2!=null&&player2.equals(ip))
		{
			System.out.println("player2 tried to move!");
			if(direction.equals("up"))
				rightPaddleMove(-1);
			else if(direction.equals("down"))
				rightPaddleMove(1);
		}
	}
	
	public void leftPaddleMove(int delta)
	{
		if(delta<0)
		if(leftPaddle.getY()>0)
			leftPaddle.setY(leftPaddle.getY()-1);
		if(delta>0)
		if(leftPaddle.getY()+leftPaddle.getHeight()<ge.lb.getHeight())
			leftPaddle.setY(leftPaddle.getY()+1);
	}
	
	public void rightPaddleMove(int delta)
	{
		if(delta<0)
		if(rightPaddle.getY()>0)
			rightPaddle.setY(rightPaddle.getY()-1);
		if(delta>0)
		if(rightPaddle.getY()+rightPaddle.getHeight()<ge.lb.getHeight())
			rightPaddle.setY(rightPaddle.getY()+1);
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		System.out.println("event! "+event);
		if(event.getKeyCode()==KeyEvent.VK_UP)
		{
			rightPaddleMove(-1);
		}
		if(event.getKeyCode()==KeyEvent.VK_DOWN)
		{
			rightPaddleMove(1);
		}
		
		if(event.getKeyCode()==KeyEvent.VK_W)
		{
			leftPaddleMove(-1);
		}
		if(event.getKeyCode()==KeyEvent.VK_S)
		{
			leftPaddleMove(1);
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		if(((JButton)event.getSource()).getText().equals("Connect"))
		{
			//try
			{
				String path=Main.class.getResource("/lights.exe").getPath();
				Thread th=new Thread()
				{
					public void run()
					{
						//Process proc;
						try {
							//proc = Runtime.getRuntime().exec("C:/Users/Nick/Desktop/workspaceHACKPSU/HackPSU/bin/lights.exe");
							proc=Runtime.getRuntime().exec(path);
							System.out.println("alive? "+proc.isAlive());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				};
				th.start();
				wait=false;
			}
			//catch (IOException e)
			{
			//	e.printStackTrace();
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if(proc!=null)
			proc.destroyForcibly();
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
