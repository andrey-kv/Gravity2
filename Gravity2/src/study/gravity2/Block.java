package study.gravity2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Block {

	Color color;
	DrawPanel drawPanel;
	private Point currentPoint, endPoint;
	

	public Block(DrawPanel drawPanel, Color color) {
		this.drawPanel = drawPanel;
		this.color = color;
		currentPoint = new Point(50, 60);
		endPoint = new Point(80, 120);
	}
	
	public void setLocation(int x1, int y1, int x2, int y2) {
		currentPoint.x = x1;
		currentPoint.y = y1;
		endPoint.x = x2;
		endPoint.y = y2;
	}

	public void draw(Graphics2D g2) {
		g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND));
		g2.setColor(color);
		int x1 = currentPoint.x;
		int y1 = currentPoint.y;
		int x2 = endPoint.x;
		int y2 = endPoint.y;
		g2.drawLine(x1, y1, x2, y2);
//		g2.drawLine(50, 60, 80, 120);
	}
}
