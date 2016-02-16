package maze.generator_d;

import java.awt.Graphics2D;

import kro.frame.KFrame;
import kro.frame.Paintable;
import maze.CellType;
import maze.Colors;
import maze.Properties;

public class Generator_d implements Paintable{
	//метод поиска вглубину по графу
	Properties properties;

	Cell[][] cells;

	int x, y;


	public Generator_d(Properties properties){
		this.properties = properties;

		x = properties.BIGIN_CELL_X;
		y = properties.BEGIN_CELL_Y;
		
		setup();// настройка клеток, их типа

	}

	public maze.Cell[][] generate(){
		while(getGlobalNotVisitedCellsCount() != 0){// общее кол-во пустых клеток
			if(getLocalNotVisitedNeighborCount(x, y) != 0){//кол-во соседних неосещенных €чеек
				makeVisited(x, y);

				Cell randomNeighborCell = getRandomNotVisistedNeighbor(x, y);
				breakWall(x, y, randomNeighborCell);
				
				x = randomNeighborCell.x;
				y = randomNeighborCell.y;
				makeVisited(x, y);
			}else{//если зашЄл в тупик
				makeVisitedAgain(x, y);
				goBack(x, y);
			}
			try{
				Thread.sleep(properties.generatorDelay);
			}catch(Exception ex){
			}
		}
		return returnCells();
	}





	private void setup(){
		cells = new Cell[properties.WIDTH][properties.HEIGHT];

		for(int xx = 0; xx < cells.length; xx++){
			for(int yy = 0; yy < cells[xx].length; yy++){
				boolean b1 = xx % 2 == 0;
				boolean b2 = yy % 2 == 0;

				if(b1 && b2){
					cells[xx][yy] = new Cell(xx, yy, CellType.WAY);
				}
				if((b1 && !b2) || (!b1 && b2)){
					cells[xx][yy] = new Cell(xx, yy, CellType.WALL);
				}
				if(!b1 && !b2){
					cells[xx][yy] = new Cell(xx, yy, CellType.POLE);
				}
			}
		}
	}





	private int random(int MIN, int MAX){// ранд. целое число от ћ»Ќ до ћј —
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
				// проходимс€ по всем клеткам, возвращаем соседа
				for(int xx = 0; xx < cells.length; xx++){
					for(int yy = 0; yy < cells[xx].length; yy++){
						boolean b = false;
						if(cells[xx][yy].type == CellType.WAY && cells[xx][yy].wasVisited == 0){
							if(random == 1){
								if(cells[xx][yy].x == x + 2 && cells[xx][yy].y == y){
									b = true;
								}
							}
							if(random == 2){
								if(cells[xx][yy].x == x - 2 && cells[xx][yy].y == y){
									b = true;
								}
							}
							if(random == 3){
								if(cells[xx][yy].x == x && cells[xx][yy].y == y + 2){
									b = true;
								}
							}
							if(random == 4){
								if(cells[xx][yy].x == x && cells[xx][yy].y == y - 2){
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

	private int getLocalNotVisitedNeighborCount(int x, int y){
		int count = 0;// число Ќ≈ѕќ—≈ў≈ЌЌџ’ соседов
		try{
			if(cells[x + 2][y].wasVisited == 0){
				count++;
			}
		}catch(Exception ex){
		}
		try{
			if(cells[x - 2][y].wasVisited == 0){
				count++;
			}
		}catch(Exception ex){
		}
		try{
			if(cells[x][y + 2].wasVisited == 0){
				count++;
			}
		}catch(Exception ex){
		}
		try{
			if(cells[x][y - 2].wasVisited == 0){
				count++;
			}
		}catch(Exception ex){
		}
		return count;
	}


	private int getGlobalNotVisitedCellsCount(){
		int count = 0;// общее кол-во непосещенных ƒ¬ј∆ƒџ €чеек
		for(int xx = 0; xx < cells.length; xx++){
			for(int yy = 0; yy < cells[xx].length; yy++){
				if(cells[xx][yy].type == CellType.WAY && cells[xx][yy].wasVisited < 2){
					count++;
				}
			}
		}
		return count;
	}


	private void makeVisited(int x, int y){
		cells[x][y].wasVisited = 1;
	}

	private void makeVisitedAgain(int x, int y){
		cells[x][y].wasVisited = 2;
	}

	private void breakWall(int x, int y, Cell neighbor){
		cells[(x + neighbor.x) / 2][(y + neighbor.y) / 2].breaked = true;
	}

	private boolean isBreaked(int x1, int y1, int x2, int y2){
		return cells[(x1 + x2) / 2][(y1 + y2) / 2].breaked;
	}
	
	private boolean isBreaked(int x, int y){
		return cells[x][y].breaked;
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

	private maze.Cell[][] returnCells(){
		return (maze.Cell[][]) cells;
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
				if(cells[xx][yy].x == x && cells[xx][yy].y == y){
					gr.setColor(Colors.currentCellColorGenerator);
					gr.fillRect(xx * properties.CELL_WIDTH, yy * properties.CELL_HEIGHT, properties.CELL_WIDTH, properties.CELL_HEIGHT);
				}
			}
		}
	}

}
