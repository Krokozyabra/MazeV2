package kro.maze;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import kro.frame.KFrame;
import kro.frame.Paintable;
import kro.maze.game.Game;
import kro.maze.generator_d.Generator_d;
import kro.maze.solver_d.Solver_d;
import kro.maze.solver_h.Solver_h;
import kro.maze.solver_w.Solver_w;

public class Main implements Paintable{
	enum Mode{
		GENERATING_d, SOLVING_h, SOLVING_d, SOLVING_w, PLAING
	}


	KFrame kFrame;
	Properties properties = new Properties();// ���������
	Timer timer;

	Generator_d generator_d;
	Solver_h solver_h;
	Solver_d solver_d;
	Solver_w solver_w;
	Game game;

	Cell[][] cells;

	boolean settingsIsOpen = false;


	int id = 0;


	Mode mode = Mode.GENERATING_d;//�����: ���������-�����������

	Thread paintThread = new Thread(new Runnable(){
		public void run(){
			do{
				try{
					kFrame.paint();
				}catch(Exception ex){
				}
			}while(true);
		}
	});

	public static void main(String[] args){
		new Main();
	}

	public Main(){
		deser();// �������������� ��������
		openWindow();// �������� ����
		timer = new Timer(properties);
		paintThread.start();
		generate_d();
	}

	private void deser(){
		try{
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("data.dat")));
			properties = (Properties) objectInputStream.readObject();
			objectInputStream.close();
		}catch(Exception ex){
			setupDefSettings();
		}
	}

	public void ser(){
		try{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("data.dat")));
			objectOutputStream.writeObject(properties);
			objectOutputStream.flush();
			objectOutputStream.close();
		}catch(Exception ex){
		}
	}

	private void openWindow(){
		kFrame = new KFrame(properties.WINDOW_WIDTH, properties.WINDOW_HEIGHT, "MAZE", 4, this);
		kFrame.setAlwaysOnTop(true);
		kFrame.setVisible(true);
		kFrame.addWindowListener(new WindowListener(){//��� �����������

			public void windowOpened(WindowEvent e){

			}

			public void windowIconified(WindowEvent e){

			}

			public void windowDeiconified(WindowEvent e){

			}

			public void windowDeactivated(WindowEvent e){

			}

			public void windowClosing(WindowEvent e){
				ser();
			}

			public void windowClosed(WindowEvent e){

			}

			public void windowActivated(WindowEvent e){

			}
		});

		setPopupMenu();
		setMenu();
	}

	private void setPopupMenu(){// ��������� ������������ ����
		PopupMenu popupMenu = new PopupMenu();
		MenuItem generator_dMenuItem = new MenuItem("�������������");
		MenuItem solver_hMenuItem = new MenuItem("������ �� ������� ����� ����");
		MenuItem solver_dMenuItem = new MenuItem("������ ������� ������ � ������� �� �����");
		MenuItem solver_wMenuItem = new MenuItem("������ ������� ������ � ������ �� �����");
		MenuItem settingsMenuItem = new MenuItem("������� ���������...");

		generator_dMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = Mode.GENERATING_d;
				generate_d();
			}
		});

		solver_hMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = Mode.SOLVING_h;
				solve_h();
			}
		});
		solver_dMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = Mode.SOLVING_d;
				solve_d();
			}
		});
		solver_wMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = Mode.SOLVING_w;
				solve_w();
			}
		});
		settingsMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!settingsIsOpen){
					openSettings(properties);
				}
			}
		});

		popupMenu.add(generator_dMenuItem);
		popupMenu.add(solver_hMenuItem);
		popupMenu.add(solver_dMenuItem);
		popupMenu.add(solver_wMenuItem);
		popupMenu.add(settingsMenuItem);

		kFrame.add(popupMenu);
		kFrame.addMouseListener(new MouseListener(){
			public void mouseReleased(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON3){
					popupMenu.show(kFrame, e.getX(), e.getY());// �������� ��������
				}
			}

			public void mousePressed(MouseEvent e){

			}

			public void mouseExited(MouseEvent e){

			}

			public void mouseEntered(MouseEvent e){

			}

			public void mouseClicked(MouseEvent e){

			}
		});

	}

	private void setMenu(){
		JMenuBar jMenuBar = new JMenuBar();
		kFrame.setJMenuBar(jMenuBar);

		JMenu generatingMenu = new JMenu("�������������� ��������");
		JMenu solvingMenu = new JMenu("�����������");
		JMenu gameMenu = new JMenu("����");
		JMenu otherMenu = new JMenu("���������");

		jMenuBar.add(generatingMenu);
		jMenuBar.add(solvingMenu);
		jMenuBar.add(gameMenu);
		jMenuBar.add(otherMenu);

		JMenuItem generator_dMenuItem = new JMenuItem("����� ������ � ������� �� �����");
		generatingMenu.add(generator_dMenuItem);

		JMenuItem solver_hMenuItem = new JMenuItem("�� ������� ����� ����");
		solvingMenu.add(solver_hMenuItem);

		JMenuItem solver_dMenuItem = new JMenuItem("����� ������ � ������� �� �����");
		solvingMenu.add(solver_dMenuItem);

		JMenuItem solver_wMenuItem = new JMenuItem("����� ������ � ������ �� �����");
		solvingMenu.add(solver_wMenuItem);

		JMenuItem gameMenuItem = new JMenuItem("������!");
		gameMenu.add(gameMenuItem);

		JMenuItem settingsMenuItem = new JMenuItem("������� ���������...");
		otherMenu.add(settingsMenuItem);

		JMenuItem infoMenuItem = new JMenuItem("� ���������");
		otherMenu.add(infoMenuItem);


		generator_dMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = Mode.GENERATING_d;
				generate_d();
			}
		});

		solver_hMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = Mode.SOLVING_h;
				solve_h();
			}
		});

		solver_dMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = Mode.SOLVING_d;
				solve_d();
			}
		});

		solver_wMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = Mode.SOLVING_w;
				solve_w();
			}
		});

		gameMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mode = Mode.PLAING;
				play();
			}
		});

		settingsMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!settingsIsOpen){
					openSettings(properties);
				}
			}
		});


		kFrame.pack();
		jMenuBar.setBackground(Color.WHITE);
	}

	public void generate_d(){
		new Thread(new Runnable(){
			public void run(){
				removeAll();
				id++;
				int id1 = id;

				timer.reset();
				timer.start(id1);

				generator_d = new Generator_d(properties);
				cells = generator_d.generate();

				timer.stop(id1);
			}
		}).start();
	}

	public void solve_h(){
		new Thread(new Runnable(){
			public void run(){
				removeAll();
				id++;
				int id1 = id;

				timer.reset();
				timer.start(id1);

				solver_h = new Solver_h(properties, cells);
				solver_h.solve();

				timer.stop(id1);
			}
		}).start();
	}

	public void solve_d(){
		new Thread(new Runnable(){
			public void run(){
				removeAll();
				id++;
				int id1 = id;

				timer.reset();
				timer.start(id1);

				solver_d = new Solver_d(properties, cells);
				solver_d.solve();

				timer.stop(id1);
			}
		}).start();
	}

	private void solve_w(){
		new Thread(new Runnable(){
			public void run(){
				removeAll();
				id++;
				int id1 = id;

				timer.reset();
				timer.start(id1);
				solver_w = new Solver_w(properties, cells);
				solver_w.solve();

				timer.stop(id1);
			}
		}).start();
	}

	private void play(){
		new Thread(new Runnable(){
			public void run(){
				removeAll();
				id++;
				int id1 = id;

				timer.reset();
				timer.start(id1);
				game = new Game(kFrame, properties, cells);

				while(!game.isEnd);
				timer.stop(id1);
			}
		}).start();
	}

	private void removeAll(){
		generator_d = null;
		solver_h = null;
		solver_d = null;
		solver_w = null;
		try{
			game.isEnd = true;
		}catch(Exception ex){
		}
		game = null;
	}

	private void openSettings(Properties properties){
		new Settings(properties, kFrame, this);
		settingsIsOpen = true;
	}

	private void setupDefSettings(){//��������� ���������
		properties.WIDTH = 101;
		properties.HEIGHT = 101;
		properties.CELL_WIDTH = 5;
		properties.CELL_HEIGHT = 5;

		properties.BIGIN_CELL_X = 0;
		properties.BEGIN_CELL_Y = 0;

		properties.END_CELL_X = properties.WIDTH - 1;
		properties.END_CELL_Y = properties.HEIGHT - 1;

		properties.delay = 0;

		properties.WINDOW_WIDTH = properties.WIDTH * properties.CELL_WIDTH;
		properties.WINDOW_HEIGHT = properties.HEIGHT * properties.CELL_HEIGHT;
	}


	public void paint(Graphics2D gr){
		try{
			if(mode == Mode.GENERATING_d){//����� ������ ���������� � ����������� �� ������
				generator_d.paint(gr);
			}
			if(mode == Mode.SOLVING_h){
				solver_h.paint(gr);
			}
			if(mode == Mode.SOLVING_d){
				solver_d.paint(gr);
			}
			if(mode == Mode.SOLVING_w){
				solver_w.paint(gr);
			}
			if(mode == Mode.PLAING){
				game.paint(gr);
			}

			timer.paint(gr);
		}catch(Exception ex){
		}
	}

}
