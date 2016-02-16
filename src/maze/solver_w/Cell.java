package maze.solver_w;

public class Cell extends maze.Cell{
	int waveNumber = Integer.MAX_VALUE;
	boolean dontTouch = false;
	boolean marked = false;
	boolean wayMarked = false;
}
