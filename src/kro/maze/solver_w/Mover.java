package kro.maze.solver_w;

public class Mover{
	enum Direction{
		RIGHT, LEFT, DOWN, UP;
	}
	int x, y;
	Direction direction = null;
	
	public Mover(int x, int y, Direction direction){
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
}
