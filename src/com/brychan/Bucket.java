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
	
	Bucket (double angularCoord, double radialCoord) {
		radial = radialCoord;
		angular =  angularCoord;
	}
	
	/** Draw the bucket on the canvas. Returns its y coordinate. */
	double draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		double y = radial * Math.sin(angular);
		double x = radial * Math.cos(angular);
		g.fillRect((int)(100+x), (int)(100-y), 20, (int)(mass*40));
		g.setColor(Color.GREEN);
		g.fillRect((int)(100+x)+10, (int)(100-y), 2, 2);
		return 100-y;
	}
	
	void update(double velocity) {
		angular += (velocity/MalkusWaterwheel.FPS);
		mass -= (0.04 / MalkusWaterwheel.FPS);
		mass = (mass < 0) ? 0 : mass;
	}
	
	double calculateForce() {
		return -mass * 9.81 * Math.cos(angular);
	}
}