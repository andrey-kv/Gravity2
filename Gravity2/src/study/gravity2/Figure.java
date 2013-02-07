package study.gravity2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

public abstract class Figure {

	public int figureSize;
	private Point currentPoint = new Point(figureSize / 2 + 2,
			figureSize / 2 + 2);
	private Mechanics mechanics = new Mechanics();
	private boolean stopped = true;
	private boolean fill = true;
	Color color;
	DrawPanel drawPanel;

	Figure(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
		this.figureSize = getRandom(16, 32);
		this.color = DrawPanel.FIG_COLOR[getRandom(0,
				DrawPanel.FIG_COLOR.length - 1)];
		int startX = getRandom(figureSize, 320);
		int startY = getRandom(figureSize, 280);
		int startSpeed = getRandom(50, 200);
		int startAngle = getRandom(-180, 180);
		fill = getRandom();
		setLocation(startX, startY);
		pull(startSpeed, Math.toRadians(startAngle));
	}

	Figure(DrawPanel drawPanel, int size, Color color) {
		this.drawPanel = drawPanel;
		this.figureSize = size;
		this.color = color;
	}

	public void draw(Graphics2D g2) {
		Point p = getLocation();
		int currentX = p.x - (figureSize / 2);
		int currentY = p.y - (figureSize / 2);
		g2.setColor(color);
		if (fill) {
			g2.fill(getShape(currentX, currentY, figureSize));
		} else {
			g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND));
			g2.draw(getShape(currentX, currentY, figureSize));
		}
	}

	public abstract Shape getShape(double currentX, double currentY, double figureSize);

	public void pull(double speed, double angle) {
		mechanics.setStartPoint(currentPoint);
		mechanics.speed.setVector(speed, angle);
		if (stopped) {
			unFreeze();
		}
		stopped = false;
	}

	public void pullFromPoint(Point pullPoint, double coefficient) {
		double speed = distance(pullPoint) * coefficient;
		double angle = getAzimuthTo(pullPoint);
		pull(speed, angle);
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
		return (currentPoint.distance(x, y) < figureSize / 2 - 2);
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
		if (mechanics.nextCoord.x < (figureSize / 2 + 1)
				&& mechanics.nextSpeed.getVectorX() < 0) {
			mechanics.reflect(Mechanics.VERTICAL);
		}
		if (mechanics.nextCoord.x > drawPanel.getWidth() - (figureSize / 2 + 1)
				&& mechanics.nextSpeed.getVectorX() >= 0) {
			mechanics.reflect(Mechanics.VERTICAL);
		}
		if (mechanics.nextCoord.y < (figureSize / 2 + 1)
				&& mechanics.nextSpeed.getVectorY() < 0) {
			mechanics.reflect(Mechanics.HORISONTAL);
		}
		if (mechanics.nextCoord.y > drawPanel.getHeight()
				- (figureSize / 2 + 1)
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
		if (getLocation().distance(fig.getLocation()) < figureSize / 2
				+ fig.figureSize / 2) {
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

	// Возвращает случайное целое в промежутке от min до max.
	private static int getRandom(int min, int max) {
		return min + (int) Math.round(((max - min) * Math.random()));
	}

	private static boolean getRandom() {
		return (Math.random() > 0.5) ;
	}
	
	// Для создания конкретной случайной фигуры используем фабричный метод
	public static Figure CreateRandom(DrawPanel dp) {
		int fig = getRandom(0, 2);
		switch (fig) {
		case 0:
			return new Ring(dp);
		case 1:
			return new Square(dp);
		case 2:
			return new Rounded(dp);
		}
		return null;
	}

}
