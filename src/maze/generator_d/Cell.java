package maze.generator_d;

import maze.CellType;

public class Cell extends maze.Cell{//для генератора
	byte wasVisited = 0;//кол-во посещений, только для прохоов
	
	public Cell(int x, int y, CellType type){
		this.x = x;
		this.y = y;
		this.type = type;
		
		if(type == CellType.WAY){
			breaked = true;
		}
	}
	
}
