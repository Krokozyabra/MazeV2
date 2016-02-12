package kro.maze.solver_w;

import java.awt.Color;
import java.awt.Graphics2D;

import kro.frame.Paintable;
import kro.maze.CellType;
import kro.maze.Colors;
import kro.maze.Properties;
import kro.maze.solver_w.Mover.Direction;

public class Solver_w implements Paintable{
	Cell[][] cells;
	Properties properties;

	Mover[] movers = new Mover[999];
	int moversCount = 1;

	public Solver_w(Properties properties, kro.maze.Cell[][] cells){
		this.properties = properties;
		setup(cells);
	}

	private void setup(kro.maze.Cell[][] cells){
		movers[0] = new Mover(properties.BIGIN_CELL_X, properties.BEGIN_CELL_Y, null);

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
		int count = 0;
		while(!isEnd()){
			makeVisitedAndMarkedMovers();
			packMovers();

			splitMovers();
			packMovers();

			move();
			try{
				Thread.sleep(properties.delay);
			}catch(Exception ex){
			}
			System.out.println(++count);
		}
		makeVisitedAndMarkedMovers();
		markWay();
	}

	private void markWay(){
		int x = properties.END_CELL_X, y = properties.END_CELL_Y;
		while(x != properties.BIGIN_CELL_X || y != properties.BEGIN_CELL_Y){
			markWay(x, y);
			Cell cell = cells[x][y];
			x = cell.parent_x;
			y = cell.parent_y;
		}
		markWay(x, y);
	}

	private void makeVisitedAndMarkedMovers(){
		for(int i = 0; i < moversCount; i++){
			makeVisited(movers[i].x, movers[i].y);
			mark(movers[i].x, movers[i].y);
		}
	}

	private void move(){
		for(int i = 0; i < moversCount; i++){
			int x = movers[i].x;
			int y = movers[i].y;

			if(movers[i].direction != null){
				switch(movers[i].direction){
					case RIGHT:
						movers[i].x = x + 2;
						movers[i].y = y;
						break;
					case LEFT:
						movers[i].x = x - 2;
						movers[i].y = y;
						break;
					case DOWN:
						movers[i].x = x;
						movers[i].y = y + 2;
						break;
					case UP:
						movers[i].x = x;
						movers[i].y = y - 2;
						break;
				}
				movers[i].direction = null;
			}else{
				if(!isVisited(x + 2, y) && isBreaked(x, y, x + 2, y)){
					movers[i].x = x + 2;
					movers[i].y = y;
				}else if(!isVisited(x - 2, y) && isBreaked(x, y, x - 2, y)){
					movers[i].x = x - 2;
					movers[i].y = y;
				}else if(!isVisited(x, y + 2) && isBreaked(x, y, x, y + 2)){
					movers[i].x = x;
					movers[i].y = y + 2;
				}else if(!isVisited(x, y - 2) && isBreaked(x, y, x, y - 2)){
					movers[i].x = x;
					movers[i].y = y - 2;
				}
			}

			cells[movers[i].x][movers[i].y].parent_x = x;
			cells[movers[i].x][movers[i].y].parent_y = y;//утсановка родителей
		}
	}

	private void splitMovers(){
		for(int i = 0; i < moversCount; i++){
			int x = movers[i].x;
			int y = movers[i].y;

			boolean currentIsChanged = false;
			if(getLocalNotVisitedNeighborCount(x, y) == 0){
				movers[i] = null;
			}
			if(getLocalNotVisitedNeighborCount(x, y) != 0 && getLocalNotVisitedNeighborCount(x, y) != 1 && movers[i].direction == null){
				if(!isVisited(x + 2, y) && isBreaked(x, y, x + 2, y)){
					movers[i] = new Mover(x, y, Direction.RIGHT);
					currentIsChanged = true;
				}
				if(!isVisited(x - 2, y) && isBreaked(x, y, x - 2, y)){
					if(!currentIsChanged){
						movers[i] = new Mover(x, y, Direction.LEFT);
						currentIsChanged = true;
					}else{
						movers[moversCount] = new Mover(x, y, Direction.LEFT);
						moversCount++;
					}
				}
				if(!isVisited(x, y + 2) && isBreaked(x, y, x, y + 2)){
					if(!currentIsChanged){
						movers[i] = new Mover(x, y, Direction.DOWN);
						currentIsChanged = true;
					}else{
						movers[moversCount] = new Mover(x, y, Direction.DOWN);
						moversCount++;
					}
				}
				if(!isVisited(x, y - 2) && isBreaked(x, y, x, y - 2)){
					if(!currentIsChanged){
						movers[i] = new Mover(x, y, Direction.UP);
						currentIsChanged = true;
					}else{
						movers[moversCount] = new Mover(x, y, Direction.UP);
						moversCount++;
					}
				}
			}
		}
	}

	private void packMovers(){//уплотнитель двигателей
		Mover[] moversTemp = new Mover[movers.length];
		int moversTempCount = 0;
		for(int i = 0; i < movers.length; i++){
			if(movers[i] != null){
				moversTemp[moversTempCount] = movers[i];
				moversTempCount++;
			}
		}
		moversCount = moversTempCount;
		movers = moversTemp;
	}


	private void makeVisited(int x, int y){
		cells[x][y].wasVisited = 1;
	}

	private void makeVisitedAgain(int x, int y){
		cells[x][y].wasVisited = 2;
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

	private boolean isVisited(int x, int y){
		try{
			return cells[x][y].wasVisited != 0;
		}catch(Exception ex){
		}
		return true;
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

	private int getLocalNotVisitedNeighborCount(int x, int y){
		int count = 0;// число НЕПОСЕЩЕННЫХ соседов
		try{
			if(cells[x + 2][y].wasVisited == 0 && isBreaked(x, y, x + 2, y)){
				count++;
			}
		}catch(Exception ex){
		}
		try{
			if(cells[x - 2][y].wasVisited == 0 && isBreaked(x, y, x - 2, y)){
				count++;
			}
		}catch(Exception ex){
		}
		try{
			if(cells[x][y + 2].wasVisited == 0 && isBreaked(x, y, x, y + 2)){
				count++;
			}
		}catch(Exception ex){
		}
		try{
			if(cells[x][y - 2].wasVisited == 0 && isBreaked(x, y, x, y - 2)){
				count++;
			}
		}catch(Exception ex){
		}
		return count;
	}

	private boolean isEnd(){
		for(int i = 0; i < moversCount; i++){
			try{
				if(movers[i].x == properties.END_CELL_X && movers[i].y == properties.END_CELL_Y){
					return true;
				}
			}catch(Exception ex){
			}
		}
		return false;
	}

	public void paint(Graphics2D gr){
		gr.setColor(Colors.breakedColor);
		gr.fillRect(0, 0, properties.WINDOW_WIDTH, properties.WINDOW_HEIGHT);

		for(int xx = 0; xx < cells.length; xx++){
			for(int yy = 0; yy < cells[0].length; yy++){
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
}
