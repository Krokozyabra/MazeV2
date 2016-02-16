package maze;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import kro.frame.Paintable;

public class TimerUI implements Paintable{
	volatile int milliSeconds = 0;

	Font font;
	Properties properties;

	public int id = 0;

	Timer timer = new Timer(true);

	public TimerUI(Properties properties){
		this.properties = properties;
		setFont();
	}

	public void shedule() throws Exception{
		timer.cancel();
		timer = new Timer(true);
		timer.schedule(new TimerTask(){
			public void run(){
				milliSeconds+=1;
			}
		}, 0, 1);
	}

	private void setFont(){
		try{
			font = Font.createFont(Font.PLAIN, TimerUI.class.getResourceAsStream("font/font.ttf"));
			font = font.deriveFont(Font.PLAIN, 25);
		}catch(FontFormatException e){
		}catch(IOException e){
		}
	}

	public void start(int id){
		this.id = id;
		try{
			shedule();
		}catch(Exception ex){
		}
	}

	public void stop(int id){
		if(this.id == id){
			timer.cancel();
		}
	}

	public void reset(){
		milliSeconds = 0;
		timer.cancel();
	}

	public void paint(Graphics2D gr){
		gr.setColor(Colors.timerBackgroundColor);
		gr.fillRect(0, properties.WINDOW_HEIGHT - 18, 14 * Integer.toString(milliSeconds).length() + 1, 18);

		gr.setFont(font);
		gr.setColor(Colors.timerColor);
		try{
			gr.drawString(Integer.toString(milliSeconds), 0, properties.WINDOW_HEIGHT);
		}catch(Exception ex){
		}
	}
}
