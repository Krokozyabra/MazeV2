package kro.maze;

import java.awt.Color;
import java.awt.Graphics2D;

import kro.frame.Paintable;

public class Timer implements Paintable{
	boolean isStarted = false;
	int milliSeconds = 0;
	
	public void start(){
		isStarted = true;
		new Thread(new Runnable(){
			public void run(){
				while(isStarted){
					try{
						Thread.sleep(1);
						milliSeconds++;
					}catch(Exception ex){
					}
				}
			}
		}).start();
	}
	
	public void stop(){
		isStarted = false;
	}
	
	public void reset(){
		milliSeconds = 0;
	}
	
	public void paint(Graphics2D gr){
		gr.setColor(new Color(255, 0, 0));
		try{
			gr.drawString(Integer.toString(milliSeconds), 0, 10);
		}catch(Exception ex){
		}
	}
}
