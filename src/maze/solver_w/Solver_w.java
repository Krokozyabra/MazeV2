package maze.solver_w;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Date;
import java.util.Timer;

import kro.frame.Paintable;
import maze.CellType;
import maze.Colors;
import maze.Properties;

public class Solver_w implements Paintable{
	Cell[][] cells;
	Properties properties;

	int wave = 1;
	
	int stepNumber = 0;
	
	public Solver_w(Properties properties, maze.Cell[][] cells){
		this.properties = properties;
		setup(cells);
	}

	public void solve(){
		cells[properties.BIGIN_CELL_X][properties.BEGIN_CELL_Y].waveNumber = wave;
		while(!isEnd()){
			move();
			markWaves();
			wave++;
			try{
				Thread.sleep(properties.solverDelay);
			}catch(Exception ex){
			}
			stepNumber++;
		}
		markWay();
	}

	private void setup(maze.Cell[][] cells){
		this.cells = new Cell[properties.WIDTH][properties.HEIGHT];

		for(int xx = 0; xx < cells.length; xx++){
			for(int yy = 0; yy < cells[xx].length; yy++){
				this.cells[xx][yy] = new Cell();
				this.cells[xx][yy].breaked = cells[xx][yy].breaked;
				this.cells[xx][yy].x = cells[xx][yy].x;
				this.cells[xx][yy].y = cells[xx][yy].y;
				this.cells[xx][yy].type = cells[xx][yy].type;
			}
		}
	}

	private boolean isEnd(){
		la: for(int xx = 0; xx < cells.length; xx++){
			for(int yy = 0; yy < cells[xx].length; yy++){
				if(xx == properties.END_CELL_X && yy == properties.END_CELL_Y){
					if(isWave(xx, yy)){
						return true;
					}else{
						return false;
					}
				}
			}
		}
		return false;
	}

	private void markWay(){
		int x = properties.END_CELL_X, y = properties.END_CELL_Y;
		markWay(x, y);
		while(x != properties.BIGIN_CELL_X || y != properties.BEGIN_CELL_Y){
			long min = -1;
			int minX = -1, minY = -1;
			try{
				if(isBreaked(x, y, x + 2, y)){
					min = getWaveNumber(x + 2, y);
					minX = x + 2;
					minY = y;
				}
			}catch(Exception ex){
			}
			try{
				if(isBreaked(x, y, x - 2, y)){
					if(min != -1){
						if(min > getWaveNumber(x - 2, y)){
							min = getWaveNumber(x - 2, y);
							minX = x - 2;
							minY = y;
						}
					}else{
						min = getWaveNumber(x - 2, y);
						minX = x - 2;
						minY = y;
					}
				}
			}catch(Exception ex){
			}
			try{
				if(isBreaked(x, y, x, y + 2)){
					if(min != -1){
						if(min > getWaveNumber(x, y + 2)){
							min = getWaveNumber(x, y + 2);
							minX = x;
							minY = y + 2;
						}
					}else{
						min = getWaveNumber(x, y + 2);
						minX = x;
						minY = y + 2;
					}
				}
			}catch(Exception ex){
			}
			try{
				if(isBreaked(x, y, x, y - 2)){
					if(min != -1){
						if(min > getWaveNumber(x, y - 2)){
							min = getWaveNumber(x, y - 2);
							minX = x;
							minY = y - 2;
						}
					}else{
						min = getWaveNumber(x, y - 2);
						minX = x;
						minY = y - 2;
					}
				}
			}catch(Exception ex){
			}
			x = minX;
			y = minY;
			markWay(x, y);
		}
	}

	private void markWaves(){
		for(int xx = 0; xx < cells.length; xx++){
			for(int yy = 0; yy < cells[xx].length; yy++){
				if(isWave(xx, yy)){
					mark(xx, yy);
				}
			}
		}
	}

	private void move(){
		la: for(int xx = 0; xx < cells.length; xx++){
			for(int yy = 0; yy < cells[xx].length; yy++){
				if(wave == getWaveNumber(xx, yy)){
					makeWaves(xx, yy);
				}
			}
		}
	}

	private void makeWaves(int x, int y){
		try{
			if(isBreaked(x, y, x + 2, y) && !isWave(x + 2, y)){
				setWaveNumber(x + 2, y, wave + 1);
			}
		}catch(Exception ex){
		}
		try{
			if(isBreaked(x, y, x - 2, y) && !isWave(x - 2, y)){
				setWaveNumber(x - 2, y, wave + 1);
			}
		}catch(Exception ex){
		}
		try{
			if(isBreaked(x, y, x, y + 2) && !isWave(x, y + 2)){
				setWaveNumber(x, y + 2, wave + 1);
			}
		}catch(Exception ex){
		}
		try{
			if(isBreaked(x, y, x, y - 2) && !isWave(x, y - 2)){
				setWaveNumber(x, y - 2, wave + 1);
			}
		}catch(Exception ex){
		}
	}

	private boolean isWave(int x, int y){
		if(cells[x][y].waveNumber == Integer.MAX_VALUE){
			return false;
		}else{
			return true;
		}
	}

	private void setWaveNumber(int x, int y, int number){
		cells[x][y].waveNumber = number;
	}

	private long getWaveNumber(int x, int y){
		return cells[x][y].waveNumber;
	}

	private boolean isMarked(int x, int y){
		if((x >= 0 && x < cells.length) && (y >= 0 && y < cells[x].length)){
			return cells[x][y].marked;
		}
		return false;
	}

	private boolean isWayMarked(int x, int y){
		if((x >= 0 && x < cells.length) && (y >= 0 && y < cells[x].length)){
			return cells[x][y].wayMarked;
		}
		return false;
	}

	private void markWay(int x, int y){
		cells[x][y].wayMarked = true;
	}

	private void mark(int x, int y){
		cells[x][y].marked = true;
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
				if(isMarked(xx, yy)){
					gr.setColor(Colors.currentCellColorSolver);
					gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
				}
				try{
					if(cells[xx][yy].type == CellType.WALL && isBreaked(xx, yy)){
						if((isMarked(xx + 1, yy) && isMarked(xx - 1, yy)) || (isMarked(xx, yy + 1) && isMarked(xx, yy - 1))){
							gr.setColor(Colors.currentCellColorSolver);
							gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
						}
					}
				}catch(Exception ex){
				}

				if(isWayMarked(xx, yy)){
					gr.setColor(Colors.wayColor);
					gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
				}
				try{
					if(cells[xx][yy].type == CellType.WALL && isBreaked(xx, yy)){
						if((isWayMarked(xx + 1, yy) && isWayMarked(xx - 1, yy)) || (isWayMarked(xx, yy + 1) && isWayMarked(xx, yy - 1))){
							gr.setColor(Colors.wayColor);
							gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
						}
					}
				}catch(Exception ex){
				}
			}
		}

	}
	
	
	
	
	public int getStepNumber(){
		return stepNumber;
	}
	
}
