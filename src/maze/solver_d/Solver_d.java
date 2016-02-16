package maze.solver_d;

import java.awt.Graphics2D;

import kro.frame.Paintable;
import maze.CellType;
import maze.Colors;
import maze.Properties;

public class Solver_d implements Paintable{
	Properties properties;

	int x, y;

	Cell[][] cells;
	
	
	int stepNumber = 0;

	public Solver_d(Properties properties, maze.Cell[][] cells){
		this.properties = properties;

		x = properties.BIGIN_CELL_X;
		y = properties.BEGIN_CELL_Y;

		setup(cells);
	}

	public void solve(){
		while(!(x == properties.END_CELL_X && y == properties.END_CELL_Y)){//пока не конец
			if(getLocalNotVisitedNeighborCount(x, y) != 0){
				makeVisited(x, y);
				mark(x, y);
	
				Cell randomNeighborCell = getRandomNotVisistedNeighbor(x, y);//случайный сосед
				x = randomNeighborCell.x;
				y = randomNeighborCell.y;
				
				makeVisited(x, y);
				mark(x, y);
			}else{
				makeVisitedAgain(x, y);
				unMark(x, y);
				goBack(x, y);
			}
			try{
				Thread.sleep(properties.solverDelay);
			}catch(Exception ex){
			}
			stepNumber++;
		}
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

	private void goBack(int x, int y){
		try{
			if(cells[x + 2][y].wasVisited == 1 && isBreaked(x, y, x + 2, y)){
				this.x = x + 2;
				return;
			}
		}catch(Exception ex){
		}
		try{
			if(cells[x - 2][y].wasVisited == 1 && isBreaked(x, y, x - 2, y)){
				this.x = x - 2;
				return;
			}
		}catch(Exception ex){
		}
		try{
			if(cells[x][y + 2].wasVisited == 1 && isBreaked(x, y, x, y + 2)){
				this.y = y + 2;
				return;
			}
		}catch(Exception ex){
		}
		try{
			if(cells[x][y - 2].wasVisited == 1 && isBreaked(x, y, x, y - 2)){
				this.y = y - 2;
				return;
			}
		}catch(Exception ex){
		}
	}


	private int random(int MIN, int MAX){// ранд. целое число от МИН до МАКС
		int random;
		do{
			random = (int) (Math.round((Math.random() * (MAX - MIN + 2)) + MIN - 1));
		}while(random < MIN || random > MAX);
		return random;
	}


	private Cell getRandomNotVisistedNeighbor(int x, int y){
		Cell cell;
		la: while(true){
			int random = random(1, 4);
			// проходимся по всем клеткам, возвращаем соседа
			for(int xx = 0; xx < cells.length; xx++){
				for(int yy = 0; yy < cells[xx].length; yy++){
					boolean b = false;
					if(cells[xx][yy].type == CellType.WAY && cells[xx][yy].wasVisited == 0){
						if(random == 1){
							if(cells[xx][yy].x == x + 2 && cells[xx][yy].y == y && isBreaked(x, y, x + 2, y) ){
								b = true;
							}
						}
						if(random == 2){
							if(cells[xx][yy].x == x - 2 && cells[xx][yy].y == y && isBreaked(x, y, x - 2, y) ){
								b = true;
							}
						}
						if(random == 3){
							if(cells[xx][yy].x == x && cells[xx][yy].y == y + 2 && isBreaked(x, y, x, y + 2)){
								b = true;
							}
						}
						if(random == 4){
							if(cells[xx][yy].x == x && cells[xx][yy].y == y - 2 && isBreaked(x, y, x ,y - 2)){
								b = true;
							}
						}
						if(b){
							cell = cells[xx][yy];
							break la;
						}
					}
				}
			}
		}
		return cell;
	}


	private boolean isBreaked(int x1, int y1, int x2, int y2){
		return cells[(x1 + x2) / 2][(y1 + y2) / 2].breaked;
	}

	private boolean isBreaked(int x, int y){
		return cells[x][y].breaked;
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

	private void mark(int x, int y){
		cells[x][y].marked = true;
	}

	private void unMark(int x, int y){
		cells[x][y].marked = false;
	}

	private int getGlobalNotVisitedCellsCount(){
		int count = 0;// общее кол-во непосещенных ДВАЖДЫ ячеек
		for(int xx = 0; xx < cells.length; xx++){
			for(int yy = 0; yy < cells[xx].length; yy++){
				if(cells[xx][yy].type == CellType.WAY && cells[xx][yy].wasVisited < 2){
					count++;
				}
			}
		}
		return count;
	}

	
	public int getStepNumber(){
		return stepNumber;
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
