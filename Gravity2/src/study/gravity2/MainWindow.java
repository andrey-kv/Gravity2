package study.gravity2;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Rectangle DEFAULT_FRAME = new Rectangle(100, 100, 360, 360);
	DrawPanel mainPanel;
	JButton btn;

	Graphics gr;

	public MainWindow() {
		super("Gravity");
		JPanel cp = new JPanel(new BorderLayout());
		btn = new JButton();
		mainPanel = new DrawPanel(btn);
		setMinimumSize(DEFAULT_FRAME.getSize());
		setBounds(DEFAULT_FRAME);
		setContentPane(cp);
		cp.add(mainPanel, BorderLayout.CENTER);
		cp.add(btn, BorderLayout.SOUTH);

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainPanel.pause();
			}
		});

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new MainWindow().setVisible(true);
	}

}
