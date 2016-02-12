package kro.maze.solver_h;

import java.awt.Graphics2D;

import kro.frame.KFrame;
import kro.frame.Paintable;
import kro.maze.CellType;
import kro.maze.Colors;
import kro.maze.Properties;

public class Solver_h implements Paintable{
	//по правилу левой(правой) руки
	
	enum Direction{
		RIGHT, LEFT, DOWN, UP
	}

	KFrame kFrame;
	Properties properties;

	Direction direction = Direction.RIGHT;

	Cell[][] cells;

	int x = 0, y = 0;

	boolean moveLeft = true;

	
	public Solver_h(Properties properties, kro.maze.Cell[][] cells){
		this.kFrame = kFrame;
		this.properties = properties;

		x = properties.BIGIN_CELL_X;
		y = properties.BEGIN_CELL_Y;

		setup(cells);
	}

	private void setup(kro.maze.Cell[][] cells){
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

	public void solve(){
		mark(x, y);
		int count = 0;
		while(!(x == properties.END_CELL_X && y == properties.END_CELL_Y)){//пока не конец
			changeDirection();// изменить направление
			moveToDirection();// двигаться в направлении
			try{
				Thread.sleep(properties.delay);
			}catch(Exception ex){
			}
			System.out.println(++count);
		}

	}

	private boolean isMarked(int x, int y){
		if((x >= 0 && x < cells.length) && (y >= 0 && y < cells[x].length)){
			return cells[x][y].marked;
		}
		return false;
	}

	private void mark(int x, int y){
		cells[x][y].marked = true;
	}

	private void unMark(int x, int y){
		cells[x][y].marked = false;
	}

	private void moveToDirection(){
		int _x = x;
		int _y = y;

		if(direction == Direction.RIGHT){
			x = x + 2;
		}
		if(direction == Direction.LEFT){
			x = x - 2;
		}
		if(direction == Direction.DOWN){
			y = y + 2;
		}
		if(direction == Direction.UP){
			y = y - 2;
		}

		if(isMarked(x, y)){
			unMark(_x, _y);
		}
		mark(x, y);
	}

	private boolean isFree(int x, int y, Direction direction){// проверка на отсутствие стенки
		if(direction == Direction.RIGHT && x + 2 < properties.WIDTH){
			return cells[x + 1][y].breaked;
		}
		if(direction == Direction.LEFT && x - 2 >= 0){
			return cells[x - 1][y].breaked;
		}
		if(direction == Direction.DOWN && y + 2 < properties.HEIGHT){
			return cells[x][y + 1].breaked;
		}
		if(direction == Direction.UP && y - 2 >= 0){
			return cells[x][y - 1].breaked;
		}
		return false;
	}

	private Direction turnRight(Direction direction){
		Direction _direction = null;

		switch(direction){
			case RIGHT:
				_direction = Direction.DOWN;
				break;
			case LEFT:
				_direction = Direction.UP;
				break;
			case DOWN:
				_direction = Direction.LEFT;
				break;
			case UP:
				_direction = Direction.RIGHT;
				break;
		}

		return _direction;
	}

	private Direction turnLeft(Direction direction){
		Direction _direction = null;

		switch(direction){
			case RIGHT:
				_direction = Direction.UP;
				break;
			case LEFT:
				_direction = Direction.DOWN;
				break;
			case DOWN:
				_direction = Direction.RIGHT;
				break;
			case UP:
				_direction = Direction.LEFT;
				break;
		}

		return _direction;
	}

	private void changeDirection(){
		if(moveLeft){
			direction = turnLeft(direction);
			while(!(isFree(x, y, direction))){
				direction = turnRight(direction);
			}
		}else{
			direction = turnRight(direction);
			while(!(isFree(x, y, direction))){
				direction = turnLeft(direction);
			}
		}
	}

	private boolean isBreaked(int x, int y){
		return cells[x][y].breaked;
	}
	
	public void paint(Graphics2D gr){
		gr.setColor(Colors.breakedColor);
		gr.fillRect(0, 0, properties.WINDOW_WIDTH, properties.WINDOW_HEIGHT);

		for(int xx = 0; xx < cells.length; xx++){
			for(int yy = 0; yy < cells[xx].length; yy++){
				// изменение цвета прорисовки клеток в зависимости от их типов
				if(cells[xx][yy].type == CellType.POLE || cells[xx][yy].type == CellType.WALL && !isBreaked(xx, yy)){
					gr.setColor(Colors.notBreakedColor);
					gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
				}
				if(isMarked(xx, yy)){
					gr.setColor(Colors.wayColor);
					gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
				}
				try{
					if(cells[xx][yy].type == CellType.WALL && isBreaked(xx, yy)){
						if((isMarked(xx + 1, yy) && isMarked(xx - 1, yy)) || (isMarked(xx, yy + 1) && isMarked(xx, yy - 1))){
							gr.setColor(Colors.wayColor);
							gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
						}
					}
				}catch(Exception ex){
				}
				if(cells[xx][yy].x == x && cells[xx][yy].y == y){
					gr.setColor(Colors.currentCellColorSolver);
					gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
				}
			}
		}

	}
}
