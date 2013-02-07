package study.gravity2;

import java.awt.Point;

public class Mechanics {

	public static final double HORISONTAL = 0;
	public static final double VERTICAL = Math.PI / 2;
	static final double MIN_SPEED = 0;

	public Point currentCoord;
	public Point nextCoord;

	public Vector2D speed = new Vector2D();
	public Vector2D nextSpeed = new Vector2D();

	public Vector2D acceleration = new Vector2D();

	private double currentTime;
	private long timer;

	public Mechanics() {
		acceleration.setVector(70, Mechanics.VERTICAL);
	}

	public void setStartPoint(Point pos) {
		currentCoord = new Point(pos);
		nextCoord = (Point) currentCoord.clone();
		resetTimer();
	}

	public void resetTimer() {
		timer = System.nanoTime();
	}

	public void setCurrentTime() {
		currentTime = ((double) System.nanoTime() - timer) / 1000000000.;
	}

	public void setCurrentTime(double time) {
		currentTime = time;
	}

	public void calculateNextCoord() {
		double newX = currentCoord.x + speed.getVectorX() * currentTime
				+ acceleration.getVectorX() * currentTime * currentTime / 2;
		double newY = currentCoord.y + speed.getVectorY() * currentTime
				+ acceleration.getVectorY() * currentTime * currentTime / 2;
		nextCoord.setLocation(newX, newY);
	}

	public void calculateNextSpeed() {
		double newSpeedVectorX = speed.getVectorX() + acceleration.getVectorX() * currentTime;
		double newSpeedVectorY = speed.getVectorY() + acceleration.getVectorY() * currentTime;
		nextSpeed.setProjections(newSpeedVectorX, newSpeedVectorY);
	}

	public void moveToNext() {
		currentCoord = (Point) nextCoord.clone();
		speed.setProjections(nextSpeed.getVectorX(), nextSpeed.getVectorY());
	}

	public void reflect(double angle) {
		moveToNext();
		double speedAngle = speed.getAngle();
		double reflectAngle = 2 * angle - speedAngle;
		// double fallAngle  = angle - speedAngle;
		// Коеффициент потери скорости после соударения
		double fallAngleRelCoeff = 1; // 0.99 - 0.1*Math.sin(fallAngle);
		double newSpeed = speed.getValue() * fallAngleRelCoeff;
		speed.setVector(newSpeed, reflectAngle);
		resetTimer();
	}
}

class Vector2D {
	private double angle = 0;
	private double value = 0;
	private double vectorX = 0;
	private double vectorY = 0;

	Vector2D() {}
	
	Vector2D(double VectorX, double VectorY) {
		setProjections(VectorX, VectorY);
	}
	
	public void setVector(double value, double angle) {
		vectorX = Math.cos(angle) * value;
		vectorY = Math.sin(angle) * value;
		this.value = value;
		this.angle = angle;
	}

	public void setVector(double value) {
		setVector(value, this.angle);
	}

	public void setProjections(double VectorX, double VectorY) {
		this.vectorX = VectorX;
		this.vectorY = VectorY;
		value = Math.hypot(VectorX, VectorY);
		angle = Math.atan2(VectorY, VectorX);
	}

	// Векторное вычитание
	public Vector2D subVector(Vector2D v2) {
		return new Vector2D(v2.getVectorX() - getVectorX(), v2.getVectorY() - getVectorY());
	}
	
	public double getAngle() {
		return angle;
	}

	public double getValue() {
		return value;
	}

	public double getVectorX() {
		return vectorX;
	}

	public double getVectorY() {
		return vectorY;
	}

}
