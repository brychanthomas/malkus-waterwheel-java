package com.brychan;

import java.awt.Color;
import java.awt.Graphics2D;


/**
 * Class to represent a single bucket on a Malkus waterwheel.
 * @author Brychan Thomas
 */
public class Bucket {
	
	double radial;
	double angular;
	double mass = 0;
	int centreX;
	int centreY;
	
	Bucket (double angularCoord, double radialCoord, int centreXCoord, int centreYCoord) {
		radial = radialCoord;
		angular =  angularCoord;
		centreX = centreXCoord;
		centreY = centreYCoord;
	}
	
	/**
	 * Draw the bucket on the canvas.
	 * @return the y coordinate of the bucket
	 */
	double draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		double y = radial * Math.sin(angular);
		double x = radial * Math.cos(angular);
		g.fillRect((int)(centreX+x), (int)(centreY-y), 20, (int)(mass*40));
		g.setColor(Color.GREEN);
		g.fillRect((int)(centreX+x)+10, (int)(centreY-y), 2, 2);
		return centreY-y;
	}
	
	void update(double velocity) {
		angular += (velocity/WaterwheelPanel.FPS);
		mass -= (0.04 / WaterwheelPanel.FPS);
		mass = (mass < 0) ? 0 : mass;
	}
	
	double calculateForce() {
		return -mass * 9.81 * Math.cos(angular);
	}
}