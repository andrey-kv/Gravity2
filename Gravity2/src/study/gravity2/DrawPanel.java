package study.gravity2;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class DrawPanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int FIGURES_COUNT = 2;
	private static final boolean FIGURES_RANDOM = false;

	private boolean stopThread;
	private boolean paused = false;
	private Thread thread;
	Figure figures[] = new Figure[FIGURES_COUNT];
	Figure readyToPull;
	private JButton pauseButton;

	public DrawPanel(JButton pauseButton) {
		super();
		this.pauseButton = pauseButton;
		thread = new Thread(this, "MainThread");
		if (FIGURES_RANDOM) {
			for (int i = 0; i < figures.length; i++) {
				figures[i] = Figure.CreateRandom(this);
				figures[i].setRandomStartParameters();
			}
		} else {
			figures[0] = Figure.Create(this, Figure.FIGURE_CIRCLE);
			figures[0].setLocation(100, 100);
			figures[0].setFigureSize(24);
			figures[0].setColor(2);
			figures[0].setFill(false);
			figures[0].defineMotion(100, Math.toRadians(45));
			
			figures[1] = Figure.Create(this, Figure.FIGURE_CIRCLE);
			figures[1].setLocation(150, 100);
			figures[1].defineMotion(80, Math.toRadians(20));
			figures[1].setColor(3);
			figures[1].setFill(false);
		}
		pause();
		thread.start();

		MouseListener ml = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				readyToPull = null;
				for (Figure f : figures) {
					if (f.isPointInside(e.getX(), e.getY())) {
						readyToPull = f;
						break;
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (readyToPull != null
						&& readyToPull.distance(e.getX(), e.getY()) > 20) {
					pull(readyToPull, new Point(e.getX(), e.getY()));
					setPaused(false);
				}
				readyToPull = null;
			}
		};
		addMouseListener(ml);
	}

	public void pull(Figure fig, Point pullPoint) {
		fig.pullFromPoint(pullPoint, 2);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
//		Включение полупрозрачности
//		 AlphaComposite ac =
//		 AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f);
//		 g2.setComposite(ac);
		for (Figure f : figures) {
			f.draw(g2);
		}
	}

	public void newPosition() {
		repaint();
	}

	@Override
	public void run() {
		do {
			if (stopThread) {
				return;
			}
			for (Figure f : figures) {
				f.checkPosition(figures);
			}
			newPosition();
			try {
				Thread.sleep(10);
			} catch (InterruptedException ex) {
			}
		} while (true);
	}

	public void stopThread() {
		stopThread = true;
	}

	public void pause() {
		if (paused) {
			for (Figure f : figures) {
				f.unFreeze();
			}
		} else {
			for (Figure f : figures) {
				f.freeze();
			}
		}
		setPaused(!paused);
		newPosition();
	}

	private void setPaused(boolean paused) {
		pauseButton.setText(paused ? "Go!" : "Pause");
		this.paused = paused;
	}

}
