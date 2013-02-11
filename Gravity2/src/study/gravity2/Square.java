package study.gravity2;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class Square extends Figure {

	public Square(DrawPanel drawPanel) {
		super(drawPanel);
	}

	@Override
	public Shape getShape(double currentX, double currentY, double figureSize) {
		return new Rectangle2D.Double(currentX, currentY, figureSize, figureSize);
	}

}
