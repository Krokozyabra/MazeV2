package maze.generator_d;

import maze.CellType;

public class Cell extends maze.Cell{//��� ����������
	byte wasVisited = 0;//���-�� ���������, ������ ��� �������
	
	public Cell(int x, int y, CellType type){
		this.x = x;
		this.y = y;
		this.type = type;
		
		if(type == CellType.WAY){
			breaked = true;
		}
	}
	
}
