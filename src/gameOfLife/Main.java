package gameOfLife;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	public static JFrame frame = new JFrame();
	private MainPanel p;
	private Image img;
	public static Dimension currFrameDim;

	public Main() {

		p = new MainPanel();
		frame.setTitle("Game Of Life; FPS " + FpsCounter.getFps());
		frame.setBackground(Color.WHITE);
		try {
			img = new ImageIcon(getClass().getResource("/res/images/gol_icon.png")).getImage();
			frame.setIconImage(img);
		} catch (Exception ex) {
		}
		// frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(p.panel1);
		frame.pack();
		currFrameDim = Main.frame.getSize();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setMinimumSize(p.panel1.getPreferredSize());
		p.getDp().resetAll();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					frame.setTitle("Game Of Life; FPS " + FpsCounter.getCurrFps());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}

		}).start();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}
		new Main();
	}

}
