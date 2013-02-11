package study.gravity2;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Ring extends Figure {

	Ring(DrawPanel drawPanel) {
		super(drawPanel);
	}

	@Override
	public Shape getShape(double currentX, double currentY, double figureSize) {
		return new Ellipse2D.Double(currentX, currentY, figureSize, figureSize);
	}
}
