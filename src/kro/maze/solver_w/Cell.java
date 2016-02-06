package kro.maze.solver_w;

public class Cell extends kro.maze.Cell{
	int wasVisited = 0;
	boolean marked = false;
	boolean wayMarked = false;
	int parent_x = -1, parent_y = -1;//координаты родителя
}
