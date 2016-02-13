package kro.maze.game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import com.sun.javafx.scene.traversal.Direction;

import kro.frame.KFrame;
import kro.frame.Paintable;
import kro.maze.Cell;
import kro.maze.CellType;
import kro.maze.Colors;
import kro.maze.Properties;

public class Game implements Paintable{
	enum Direction{
		RIGHT, LEFT, DOWN, UP
	}
	
	Cell[][] cells;
	Properties properties;
	KFrame kFrame;
	
	int x, y;
	
	public volatile boolean isEnd = false;
	
	KeyListener keyListener = new KeyListener(){
		public void keyTyped(KeyEvent e){
			
		}
		
		public void keyReleased(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
				move(Direction.RIGHT);
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
				move(Direction.LEFT);
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
				move(Direction.DOWN);
			}
			if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
				move(Direction.UP);
			}
		}
		
		public void keyPressed(KeyEvent e){
			
		}
	};
	
	public Game(KFrame kFrame, Properties properties, Cell[][] cells){
		this.kFrame = kFrame;
		this.properties = properties;
		this.cells = cells;
		
		x = properties.BIGIN_CELL_X;
		y = properties.BEGIN_CELL_Y;
		
		kFrame.addKeyListener(keyListener);
	}
	
	private void move(Direction direction){
		switch(direction){
			case RIGHT:
				if(isBreaked(x, y, x + 2, y)){
					x = x + 2;
					y = y;
				}
				break;
			case LEFT:
				if(isBreaked(x, y, x - 2, y)){
					x = x - 2;
					y = y;
				}
				break;
			case DOWN:
				if(isBreaked(x, y, x, y + 2)){
					x = x;
					y = y + 2;
				}
				break;
			case UP:
				if(isBreaked(x, y, x, y - 2)){
					x = x;
					y = y - 2;
				}
				break;
		}
		if(x == properties.END_CELL_X && y == properties.END_CELL_Y){
			isEnd = true;
			kFrame.getKCanvas().removeKeyListener(keyListener);
			showWinMessage();
		}
	}

	private void showWinMessage(){
		Object[] objects = {"ОК"};
		JOptionPane.showOptionDialog(kFrame, "Поздравляем, Вы прошли лабиринт!", "Победа", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, objects, 1);
	}
	
	private boolean isBreaked(int x1, int y1, int x2, int y2){
		try{
			return cells[(x1 + x2) / 2][(y1 + y2) / 2].breaked;
		}catch(Exception ex){
		}
		return false;
	}

	private boolean isBreaked(int x, int y){
		try{
			return cells[x][y].breaked;
		}catch(Exception ex){
		}
		return false;
	}
	
	public void paint(Graphics2D gr){
		gr.setColor(Colors.breakedColor);
		gr.fillRect(0, 0, properties.WINDOW_WIDTH, properties.WINDOW_HEIGHT);

		for(int xx = 0; xx < cells.length; xx++){
			for(int yy = 0; yy < cells[xx].length; yy++){
				if(!isBreaked(xx, yy)){
					gr.setColor(Colors.notBreakedColor);
					gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
				}
				if(cells[xx][yy].x == x && cells[xx][yy].y == y){
					gr.setColor(Colors.currentCellColorGame);
					gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
				}
				if(cells[xx][yy].x == properties.END_CELL_X && cells[xx][yy].y == properties.END_CELL_Y){
					gr.setColor(Colors.exitGameColor);
					gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
				}
			}
		}

	}
}
