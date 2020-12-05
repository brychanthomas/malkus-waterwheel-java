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
	
	private double radial;
	private double angular;
	public  double mass = 0;
	private int centreX;
	private int centreY;
	
	Bucket (double angularCoord, double radialCoord, int centreXCoord, int centreYCoord) {
		radial = radialCoord;
		angular =  angularCoord;
		centreX = centreXCoord;
		centreY = centreYCoord;
	}
	
	/**
	 * Draw the bucket on the canvas every frame.
	 */
	public void draw(Graphics2D g) {
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
	
	/**
	 * Update the position of the bucket using the wheel's angular velocity.
	 * @param velocity - angular velocity of wheel (radians/second)
	 */
	public void update(double velocity) {
		angular += (velocity/WaterwheelPanel.FPS);
		mass -= (0.04 / WaterwheelPanel.FPS);
		mass = (mass < 0) ? 0 : mass;
	}
	
	/**
	 * Calculate the component of the bucket's weight perpendicular to the
	 * wheel's radius.
	 * @return component of weight (Newtons)
	 */
	public double calculateForce() {
		return -mass * 9.81 * Math.cos(angular);
	}
	
	/**
	 * Get Cartesian x coordinate of bucket.
	 * @return x coordinate of bucket
	 */
	public double getX() {
		return (radial * Math.cos(angular));
	}
	
	/**
	 * Get Cartesian y coordinate of the bucket.
	 * @return y coordinate of bucket
	 */
	public double getY() {
		return (-radial * Math.sin(angular));
	}
}