package study.gravity2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.util.Random;

public abstract class Figure {

	public static final int FIGURE_CIRCLE = 0;
	public static final int FIGURE_SQUARE = 1;
	public static final int FIGURE_ROUNDED = 2;
	public static final Color FIGURE_COLOR[] = { new Color(255, 165, 0),
		new Color(0, 168, 107), new Color(8, 37, 103), Color.RED,
		new Color(186, 36, 184), Color.BLACK };
	private int figureSize = 20;
	private Point currentPoint = new Point(figureSize / 2 + 2,
			figureSize / 2 + 2);
	private Mechanics mechanics = new Mechanics();
	private boolean stopped = true;
	private boolean fill = true;
	private Color color = Color.BLACK;
	DrawPanel drawPanel;

	Figure(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
	}
	
	public void defineMotion(double speed, double angle) {
		mechanics.setStartPoint(currentPoint);
		mechanics.speed.setVector(speed, angle);
	}
	
	/* ------ Define figure's color -----------------------*/
	public void setColor(int colorNumber) {
		if (colorNumber < 0 || colorNumber >= FIGURE_COLOR.length) {
			colorNumber = 0;
		}
		this.color = FIGURE_COLOR[colorNumber];
	}
	public void setColor(Color c) {
		this.color = c;
	}
	/* ----------------------------------------------------*/
	
	/* ------ Define figure's size ------------------------*/
	public int getFigureSize() {
		return figureSize;
	}
	public void setFigureSize(int figureSize) {
		this.figureSize = figureSize;
	}
	/* ----------------------------------------------------*/
	
	public void setFill(boolean fill) {
		this.fill = fill;
	}
	
	public void setRandomStartParameters() {
		Random rnd = new Random();
		setFigureSize(16+rnd.nextInt(17));
		setColor(rnd.nextInt(FIGURE_COLOR.length));
		int startX = getFigureSize()+rnd.nextInt(300);
		int startY = getFigureSize()+rnd.nextInt(300);
		int startSpeed = 50 + rnd.nextInt(150);
		int startAngle = -180 + rnd.nextInt(360);
		setFill(rnd.nextBoolean());
		setLocation(startX, startY);
		defineMotion(startSpeed, Math.toRadians(startAngle));
	}

	public void draw(Graphics2D g2) {
		Point p = getLocation();
		int currentX = p.x - (getFigureSize() / 2);
		int currentY = p.y - (getFigureSize() / 2);
		g2.setColor(color);
		if (fill) {
			g2.fill(getShape(currentX, currentY, getFigureSize()));
		} else {
			g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND));
			g2.draw(getShape(currentX, currentY, getFigureSize()));
		}
	}

	public abstract Shape getShape(double currentX, double currentY, double figureSize);

	public void pullFromPoint(Point pullPoint, double coefficient) {
		double speed = distance(pullPoint) * coefficient;
		double angle = getAzimuthTo(pullPoint);
		defineMotion(speed, angle);
		if (stopped) {
			unFreeze();
		}
		stopped = false;
	}

	private double getAzimuthTo(Point point2) {
		double sideX = currentPoint.x - point2.x;
		double sideY = currentPoint.y - point2.y;
		return Math.atan2(sideY, sideX);
	}

	public void freeze() {
		if (!stopped) {
			mechanics.setCurrentTime();
			mechanics.calculateNextCoord();
			mechanics.calculateNextSpeed();
			mechanics.moveToNext();
		}
		stopped = true;
	}

	public void unFreeze() {
		if (stopped) {
			mechanics.resetTimer();
			mechanics.setCurrentTime();
		}
		stopped = false;
	}

	public boolean isFreeze() {
		return stopped;
	}

	public void setLocation(int x, int y) {
		currentPoint.setLocation(x, y);
	}

	public void setLocation(Point pos) {
		currentPoint.setLocation(pos);
	}

	public Point getLocation() {
		return currentPoint;
	}

	public Point getNextLocation() {
		return mechanics.nextCoord;
	}

	public boolean isPointInside(int x, int y) {
		return (currentPoint.distance(x, y) < getFigureSize() / 2 - 2);
	}

	public double distance(int x, int y) {
		return currentPoint.distance(x, y);
	}

	public double distance(Point pos) {
		return currentPoint.distance(pos);
	}

	public double getNextSpeed() {
		return mechanics.nextSpeed.getValue();
	}

	public void checkPosition(Figure[] figures) {
		if (stopped) {
			return;
		}
		mechanics.setCurrentTime();
		mechanics.calculateNextCoord();
		mechanics.calculateNextSpeed();
		if (mechanics.nextSpeed.getValue() == 0)
			return;
		if (mechanics.nextCoord.x < (getFigureSize() / 2 + 1)
				&& mechanics.nextSpeed.getVectorX() < 0) {
			mechanics.reflect(Mechanics.VERTICAL);
		}
		if (mechanics.nextCoord.x > drawPanel.getWidth() - (getFigureSize() / 2 + 1)
				&& mechanics.nextSpeed.getVectorX() >= 0) {
			mechanics.reflect(Mechanics.VERTICAL);
		}
		if (mechanics.nextCoord.y < (getFigureSize() / 2 + 1)
				&& mechanics.nextSpeed.getVectorY() < 0) {
			mechanics.reflect(Mechanics.HORISONTAL);
		}
		if (mechanics.nextCoord.y > drawPanel.getHeight()
				- (getFigureSize() / 2 + 1)
				&& mechanics.nextSpeed.getVectorY() >= 0) {
			mechanics.reflect(Mechanics.HORISONTAL);
		}
		reflectWith(figures);
		setLocation(mechanics.nextCoord);

	}

	public void reflectWith(Figure[] figures) {
		for (int i = 0; i < figures.length; i++) {
			for (int j = i; j < figures.length; j++) {
				if (this.equals(figures[j])) {
					continue;
				}
				reflectWithA(figures[j]);
			}
		}
	}

	private void reflectWithA(Figure fig) {
		if (getLocation().distance(fig.getLocation()) < getFigureSize() / 2
				+ fig.getFigureSize() / 2) {
			Vector2D relSpeed = mechanics.speed.subVector(fig.mechanics.speed);
			double relativeSpeedAngle = relSpeed.getAngle();
			double relativeDistanceAngle = getAzimuthTo(fig.getLocation());
			double relative = Math.cos(relativeDistanceAngle
					- relativeSpeedAngle);
			if (relative > 0) {
				mechanics.reflect(Math.PI / 2 + relativeDistanceAngle);
				if (fig.isFreeze()) {
					fig.unFreeze();
				}
				fig.mechanics.reflect(Math.PI / 2 + relativeDistanceAngle);
			}
		}
	}

	public static Figure CreateRandom(DrawPanel dp) {
		Random rnd = new Random();
		return Create(dp, rnd.nextInt(3));
	}
	public static Figure Create(DrawPanel dp, int fig) {
		switch (fig) {
		case FIGURE_SQUARE:
			return new Square(dp);
		case FIGURE_ROUNDED:
			return new Rounded(dp);
		default:
			return new Ring(dp);
		}
	}
	

}
