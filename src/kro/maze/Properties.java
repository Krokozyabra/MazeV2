package kro.maze;

import java.awt.Color;
import java.io.Serializable;

public class Properties implements Serializable, Cloneable{
	public int WIDTH, HEIGHT;//Размер поля в клетках
	public int CELL_WIDTH, CELL_HEIGHT;//Размер клетки в пикселях
	public int WINDOW_WIDTH, WINDOW_HEIGHT;//Размер окна в пикселях
	
	public int solverDelay;
	public int generatorDelay;
	
	public int BIGIN_CELL_X, BEGIN_CELL_Y;
	public int END_CELL_X, END_CELL_Y;

	
	protected Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
}
