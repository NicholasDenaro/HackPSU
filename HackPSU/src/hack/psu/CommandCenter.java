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
	
	public CommandCenter(GameEngine ge)
	{
		super("Command Center");
		this.ge=ge;
		//this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
	}
	
	@Override
	public void keyPressed(KeyEvent event)
	{
		System.out.println("event! "+event);
		if(event.getKeyCode()==KeyEvent.VK_UP)
		{
			if(rightPaddle.getY()>0)
				rightPaddle.setY(rightPaddle.getY()-1);
		}
		if(event.getKeyCode()==KeyEvent.VK_DOWN)
		{
			if(rightPaddle.getY()+rightPaddle.getHeight()<ge.lb.getHeight())
				rightPaddle.setY(rightPaddle.getY()+1);
		}
		
		if(event.getKeyCode()==KeyEvent.VK_W)
		{
			if(leftPaddle.getY()>0)
				leftPaddle.setY(leftPaddle.getY()-1);
		}
		if(event.getKeyCode()==KeyEvent.VK_S)
		{
			if(leftPaddle.getY()+leftPaddle.getHeight()<ge.lb.getHeight())
				leftPaddle.setY(leftPaddle.getY()+1);
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
