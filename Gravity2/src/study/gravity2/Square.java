package study.gravity2;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class Square extends Figure {

	public Square(DrawPanel drawPanel) {
		super(drawPanel);
	}

	public Square(DrawPanel drawPanel, int size, Color color) {
		super(drawPanel, size, color);
	}

	@Override
	public Shape getShape(double currentX, double currentY, double figureSize) {
		return new Rectangle2D.Double(currentX, currentY, figureSize, figureSize);
	}

}
