package kro.maze;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kro.frame.KFrame;
import kro.maze.Main.Mode;

public class Settings extends JFrame{
	JLabel widthLabel = new JLabel("Ширина поля, клетки: ");
	JLabel heightLabel = new JLabel("Высота поля, клетки: ");
	JLabel cellWidthLabel = new JLabel("Ширина клетки, пиксели: ");
	JLabel cellHeightLabel = new JLabel("Высота клетки, пиксели: ");
	JLabel delayLabel = new JLabel("Задержка, мс: ");

	JTextField widthField = new JTextField(5);
	JTextField heightField = new JTextField(5);
	JTextField cellWidthField = new JTextField(5);
	JTextField cellHeightField = new JTextField(5);
	JTextField delayField = new JTextField(5);
	
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
		JPanel jPanel = new JPanel(new GridLayout(6, 2));

		jPanel.add(widthLabel);
		jPanel.add(widthField);

		jPanel.add(heightLabel);
		jPanel.add(heightField);

		jPanel.add(cellWidthLabel);
		jPanel.add(cellWidthField);

		jPanel.add(cellHeightLabel);
		jPanel.add(cellHeightField);
		
		jPanel.add(delayLabel);
		jPanel.add(delayField);


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
				main.generate_d();//автоматическая генерция после выхода из настроек
			}
			main.settingsIsOpen = false;
		}catch(Exception ex){
			resetSettings();
			JOptionPane.showMessageDialog(null, "Неккоректно введено число!", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void changeSettings(){
		properties.WIDTH = Integer.parseInt(widthField.getText());
		properties.HEIGHT = Integer.parseInt(heightField.getText());
		
		properties.CELL_WIDTH = Integer.parseInt(cellWidthField.getText());
		properties.CELL_HEIGHT = Integer.parseInt(cellHeightField.getText());
		
		properties.delay = Integer.parseInt(delayField.getText());
		
		properties.WINDOW_WIDTH = properties.WIDTH * properties.CELL_WIDTH;
		properties.WINDOW_HEIGHT = properties.HEIGHT * properties.CELL_HEIGHT;
		
		properties.END_CELL_X = properties.WIDTH - 1;
		properties.END_CELL_Y = properties.HEIGHT - 1;
	}
	
	private void resetSettings(){
		properties.WIDTH = _properties.WIDTH;
		properties.HEIGHT = _properties.HEIGHT;
		
		properties.CELL_WIDTH = _properties.CELL_WIDTH;
		properties.CELL_HEIGHT = _properties.CELL_HEIGHT;
		
		properties.delay = _properties.delay;
		
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
		
		setPlaceHolder(delayField, Integer.toString(properties.delay));
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
