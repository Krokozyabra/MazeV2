package kro.maze;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import kro.frame.KFrame;
import kro.maze.Main.Mode;
import sun.net.www.content.image.jpeg;

public class Settings extends JFrame{
	JLabel widthLabel = new JLabel("Ширина поля, клетки: ");
	JLabel heightLabel = new JLabel("Высота поля, клетки: ");
	JLabel cellWidthLabel = new JLabel("Ширина клетки, пиксели: ");
	JLabel cellHeightLabel = new JLabel("Высота клетки, пиксели: ");
	JLabel beginCellLabel = new JLabel("Начальная точка: ");
	JLabel endCellLabel = new JLabel("Конечная точка: ");
	JLabel delaySolverLabel = new JLabel("Задержка(прохождение), мс: ");
	JLabel delayGeneratorLabel = new JLabel("Задержка(генерация), мс: ");

	JLabel helpLabel = new JLabel("<html><body text=\"ff0000\" textsize=\"20\"><h9><i><b><u>Пишет \"Некорректно введено число\"?</u></i></b></h9></body></html>");

	JTextField widthField = new JTextField(5);
	JTextField heightField = new JTextField(5);
	JTextField cellWidthField = new JTextField(5);
	JTextField cellHeightField = new JTextField(5);
	JTextField beginCellField = new JTextField(5);
	JTextField endCellField = new JTextField(5);
	JTextField delaySolverField = new JTextField(5);
	JTextField delayGeneratorField = new JTextField(5);

	JFrame jFrame;
	KFrame kFrame;

	Main main;

	JButton okButton = new JButton("ОК");

	Properties properties;
	Properties _properties = null;

	public Settings(Properties properties, KFrame kFrame, Main main){
		this.properties = properties;
		this.kFrame = kFrame;

		this.main = main;

		showFrame();
	}

	private void showFrame(){
		jFrame = new JFrame("Настройки");
		jFrame.setLayout(new FlowLayout());
		JPanel jPanel = new JPanel(new GridLayout(10, 2));

		jPanel.add(widthLabel);
		jPanel.add(widthField);

		jPanel.add(heightLabel);
		jPanel.add(heightField);

		jPanel.add(cellWidthLabel);
		jPanel.add(cellWidthField);

		jPanel.add(cellHeightLabel);
		jPanel.add(cellHeightField);

		jPanel.add(beginCellLabel);
		jPanel.add(beginCellField);

		jPanel.add(endCellLabel);
		jPanel.add(endCellField);

		jPanel.add(delaySolverLabel);
		jPanel.add(delaySolverField);

		jPanel.add(delayGeneratorLabel);
		jPanel.add(delayGeneratorField);


		jPanel.add(helpLabel);
		jPanel.add(new JLabel());
		jPanel.add(okButton);

		setTextFromProperties();

		jFrame.add(jPanel);
		jFrame.pack();
		jFrame.setResizable(false);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);

		jFrame.addWindowListener(new WindowListener(){
			public void windowOpened(WindowEvent e){

			}

			public void windowIconified(WindowEvent e){

			}

			public void windowDeiconified(WindowEvent e){

			}

			public void windowDeactivated(WindowEvent e){

			}

			public void windowClosing(WindowEvent e){
				main.settingsIsOpen = false;
			}

			public void windowClosed(WindowEvent e){

			}

			public void windowActivated(WindowEvent e){

			}
		});

		helpLabel.addMouseListener(new MouseListener(){
			boolean helpIsOpened = false;

			public void mouseReleased(MouseEvent e){
				if(!helpIsOpened){
					JFrame jFrame = new JFrame("Помощь");
					jFrame.setResizable(false);

					JLabel jLabel = new JLabel("<html><body>Итоговая высота окна должна быть <i>не меньше 60</i> пикселей, <br>а ширина <i>не меньше 262</i> пикселей.\nШирина и высота<br> поля должны быть <i>нечетными</i>, а координаты<br> начальной и конечной точек<br><i>чётными</i>.</html></body>");
					jFrame.add(jLabel);
					jFrame.pack();
					jFrame.setLocationRelativeTo(null);
					jFrame.setVisible(true);

					helpIsOpened = true;

					jFrame.addWindowListener(new WindowListener(){
						public void windowOpened(WindowEvent e){

						}

						public void windowIconified(WindowEvent e){

						}

						public void windowDeiconified(WindowEvent e){

						}

						public void windowDeactivated(WindowEvent e){

						}

						public void windowClosing(WindowEvent e){
							helpIsOpened = false;
							jFrame.dispose();
						}

						public void windowClosed(WindowEvent e){

						}

						public void windowActivated(WindowEvent e){

						}
					});
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

		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				apply();//применение настроек
				main.ser();
			}
		});
	}

	private void apply(){
		try{
			_properties = (Properties) properties.clone();
		}catch(CloneNotSupportedException e){
		}
		try{
			changeSettings();

			if(properties.WINDOW_WIDTH < 262 || properties.WINDOW_HEIGHT < 60 || properties.WIDTH % 2 == 0 || properties.HEIGHT % 2 == 0){
				throw new Exception();
			}
			kFrame.setSize(properties.WINDOW_WIDTH, properties.WINDOW_HEIGHT);

			jFrame.dispose();
			kFrame.setLocationRelativeTo(null);

			main.mode = Mode.GENERATING_d;
			if(properties.WIDTH != _properties.WIDTH || properties.HEIGHT != _properties.HEIGHT){
				new Thread(new Runnable(){
					public void run(){
						main.generate_d();
					}
				}).start();//автоматическая генерция после выхода из настроек
			}
			main.settingsIsOpen = false;
		}catch(Exception ex){
			resetSettings();
			JOptionPane.showMessageDialog(null, "Неккоректно введено число!", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void changeSettings() throws Exception{
		properties.WIDTH = Integer.parseInt(widthField.getText());
		properties.HEIGHT = Integer.parseInt(heightField.getText());

		properties.CELL_WIDTH = Integer.parseInt(cellWidthField.getText());
		properties.CELL_HEIGHT = Integer.parseInt(cellHeightField.getText());

		properties.solverDelay = Integer.parseInt(delaySolverField.getText());
		properties.generatorDelay = Integer.parseInt(delayGeneratorField.getText());

		properties.WINDOW_WIDTH = properties.WIDTH * properties.CELL_WIDTH;
		properties.WINDOW_HEIGHT = properties.HEIGHT * properties.CELL_HEIGHT;

		if(beginCellField.getText().equals("1:1")){
			properties.BIGIN_CELL_X = 0;
			properties.BEGIN_CELL_Y = 0;
		}else{
			String string = beginCellField.getText();
			int colonPos = 0;
			for(int i = 0; i < string.length(); i++){
				if(string.charAt(i) == ":".charAt(0)){
					colonPos = i;
					break;
				}
			}
			String string1 = string.substring(0, colonPos);
			properties.BIGIN_CELL_X = Integer.parseInt(string1);
			String string2 = string.substring(colonPos + 1);
			properties.BEGIN_CELL_Y = Integer.parseInt(string2);
			if(properties.BIGIN_CELL_X % 2 != 0 || properties.BEGIN_CELL_Y % 2 != 0 || !containPoint(properties.BIGIN_CELL_X, properties.BEGIN_CELL_Y)){
				throw new Exception();
			}
		}
		if(endCellField.getText().equals("1:1")){
			properties.END_CELL_X = properties.WIDTH - 1;
			properties.END_CELL_Y = properties.HEIGHT - 1;
		}else{
			String string = endCellField.getText();
			int colonPos = 0;
			for(int i = 0; i < string.length(); i++){
				if(string.charAt(i) == ":".charAt(0)){
					colonPos = i;
					break;
				}
			}
			String string1 = string.substring(0, colonPos);
			properties.END_CELL_X = Integer.parseInt(string1);
			String string2 = string.substring(colonPos + 1);
			properties.END_CELL_Y = Integer.parseInt(string2);
			if(properties.END_CELL_X % 2 != 0 || properties.END_CELL_Y % 2 != 0 || !containPoint(properties.END_CELL_X, properties.END_CELL_Y)){
				throw new Exception();
			}
		}
	}
	
	private boolean containPoint(int x, int y){
		if(x >= 0 && y >= 0 && x < properties.WIDTH && y < properties.HEIGHT){
			return true;
		}
		return false;
	}

	private void resetSettings(){
		properties.WIDTH = _properties.WIDTH;
		properties.HEIGHT = _properties.HEIGHT;

		properties.CELL_WIDTH = _properties.CELL_WIDTH;
		properties.CELL_HEIGHT = _properties.CELL_HEIGHT;

		properties.solverDelay = _properties.solverDelay;
		properties.generatorDelay = _properties.generatorDelay;

		properties.WINDOW_WIDTH = properties.WIDTH * properties.CELL_WIDTH;
		properties.WINDOW_HEIGHT = properties.HEIGHT * properties.CELL_HEIGHT;

		properties.END_CELL_X = properties.WIDTH - 1;
		properties.END_CELL_Y = properties.HEIGHT - 1;
	}

	private void setTextFromProperties(){
		setPlaceHolder(widthField, Integer.toString(properties.WIDTH));//placeHolder со значениями настроек
		setPlaceHolder(heightField, Integer.toString(properties.HEIGHT));

		setPlaceHolder(cellWidthField, Integer.toString(properties.CELL_WIDTH));
		setPlaceHolder(cellHeightField, Integer.toString(properties.CELL_HEIGHT));

		setPlaceHolder(beginCellField, Integer.toString(properties.BIGIN_CELL_X) + ":" + Integer.toString(properties.BEGIN_CELL_Y));
		setPlaceHolder(endCellField, Integer.toString(properties.END_CELL_X) + ":" + Integer.toString(properties.END_CELL_Y));

		setPlaceHolder(delaySolverField, Integer.toString(properties.solverDelay));
		setPlaceHolder(delayGeneratorField, Integer.toString(properties.generatorDelay));
	}

	private void setPlaceHolder(JTextField jTextField, String placeholder){
		jTextField.setForeground(new Color(200, 200, 200));
		jTextField.setText(placeholder);

		jTextField.addFocusListener(new FocusListener(){
			boolean isShowing = true;

			public void focusLost(FocusEvent e){
				if(jTextField.getText().length() == 0){
					jTextField.setForeground(new Color(200, 200, 200));
					jTextField.setText(placeholder);

					isShowing = true;
				}
			}

			public void focusGained(FocusEvent e){
				jTextField.setForeground(new Color(0, 0, 0));
				if(isShowing){
					jTextField.setText("");
					isShowing = false;
				}
			}
		});
	}
}
