package maze.solver_d;

public class Cell extends maze.Cell{
	boolean marked = false;
	byte wasVisited = 0;//кол-во посещений, только для прохоов
}
