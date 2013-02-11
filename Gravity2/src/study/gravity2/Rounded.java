package study.gravity2;

import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class Rounded extends Figure {

	public Rounded(DrawPanel drawPanel) {
		super(drawPanel);
	}

	@Override
	public Shape getShape(double currentX, double currentY, double figureSize) {
		return new RoundRectangle2D.Double(currentX, currentY, figureSize, figureSize, figureSize/2, figureSize/2);
	}

}
