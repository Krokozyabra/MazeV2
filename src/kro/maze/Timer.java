package kro.maze;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import kro.Downloader;
import kro.frame.Paintable;
import kro.maze.Main.Mode;

public class Timer implements Paintable{
	boolean isStarted = false;
	int milliSeconds = 0;

	Font font;
	Properties properties;

	public int id = 0;
	
	Thread thread;

	public Timer(Properties properties){
		this.properties = properties;
		init();
		setFont();
	}
	
	public void init(){
		thread = new Thread(new Runnable(){
			public void run(){
				while(isStarted){
					try{
						Thread.sleep(1);
						milliSeconds++;
					}catch(Exception ex){
					}
				}
			}
		});
	}

	private void setFont(){
		try{
			font = Font.createFont(Font.PLAIN, Downloader.class.getResourceAsStream("font/font.ttf"));
			font = font.deriveFont(Font.PLAIN, 25);
		}catch(FontFormatException e){
		}catch(IOException e){
		}
	}

	public void start(int id){
		this.id = id;
		isStarted = true;
		thread.start();
	}

	public void stop(int id){
		if(this.id == id){
			isStarted = false;
		}
	}

	public void reset(){
		milliSeconds = 0;
		thread.stop();
		stop(id);
		init();
	}

	public void paint(Graphics2D gr){
		gr.setColor(new Color(255, 255, 255, 230));
		gr.fillRect(0, properties.WINDOW_HEIGHT - 18, 14 * Integer.toString(milliSeconds).length() + 1, 18);

		gr.setFont(font);
		gr.setColor(Colors.timerColor);
		try{
			gr.drawString(Integer.toString(milliSeconds), 0, properties.WINDOW_HEIGHT);
		}catch(Exception ex){
		}
	}
}
