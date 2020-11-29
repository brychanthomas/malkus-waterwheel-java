package com.brychan;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;


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
	 */
	void draw(Graphics2D g) {
		int y = (int)(centreY - radial * Math.sin(angular));
		int x = (int)(centreX + radial * Math.cos(angular));
		g.setColor(Color.BLUE);
		if (mass > 0) {
			Graphics2D g2d = (Graphics2D) g.create();
			Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6}, 0);
	        g2d.setStroke(dashed);
	        g2d.setColor(Color.CYAN);
			g2d.drawLine(x, y+20, x, y+70);
			g2d.dispose();
		}
		g.fillRect(x-10, y+20-(int)(mass*41), 20, (int)(mass*41));
		g.setColor(Color.BLACK);
		g.fillRect(x-1, y, 2, 2);
		g.drawLine(x-10, y, x-10, y+20);
		g.drawLine(x-10, y+20, x+10, y+20);
		g.drawLine(x+10, y+20, x+10, y);
	}
	
	void update(double velocity) {
		angular += (velocity/WaterwheelPanel.FPS);
		mass -= (0.04 / WaterwheelPanel.FPS);
		mass = (mass < 0) ? 0 : mass;
	}
	
	double calculateForce() {
		return -mass * 9.81 * Math.cos(angular);
	}
	
	double getX() {
		return (radial * Math.cos(angular));
	}
	
	double getY() {
		return (-radial * Math.sin(angular));
	}
}