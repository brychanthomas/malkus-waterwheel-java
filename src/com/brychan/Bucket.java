package com.brychan;

import java.awt.Color;
import java.awt.Graphics2D;


/**
 * Class to simulate and draw a single bucket on a Malkus waterwheel.
 * 
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
		int y = (int)(centreY - radial * Math.sin(angular));
		int x = (int)(centreX + radial * Math.cos(angular));
		g.setColor(Color.BLUE);
		g.fillRect(x, y+20-(int)(mass*41), 20, (int)(mass*41));
		g.setColor(Color.GREEN);
		g.fillRect(x+10, y, 2, 2);
		g.setColor(Color.BLACK);
		g.drawLine(x, y, x, y+20);
		g.drawLine(x, y+20, x+20, y+20);
		g.drawLine(x+20, y+20, x+20, y);
		return y;
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