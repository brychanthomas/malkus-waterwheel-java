package com.brychan;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Class to simulate and draw a Malkus waterwheel.
 * 
 * @author Brychan Thomas
 */
public class MalkusWaterwheel {
	Bucket[] buckets;
	double velocity = 0;
	double radius = 1.3;
	double wheelMass = 10;
	int    numBuckets = 16;
	int centreX;
	int centreY;
	double angularCoord;
	
	MalkusWaterwheel (int centreXCoord, int centreYCoord, double offset) {
		angularCoord = offset;
		centreX = centreXCoord;
		centreY = centreYCoord;
		buckets = new Bucket[numBuckets];
		for (int i = 0; i<numBuckets; i++) {
			buckets[i] = new Bucket((i*2*Math.PI/numBuckets)+offset, radius*100, centreX, centreY);
		}
	}
	
	/**
	 * Update the velocity and coordinates of the wheel + buckets every frame, and add
	 * water to the top bucket.
	 */
	public void update() {
		double force = -velocity*4.5;
		for (int i=0; i<numBuckets; i++) {
			force += buckets[i].calculateForce();
		}
		double acc = (force) / (mass() * radius);
		velocity += acc / WaterwheelPanel.FPS;
		
		double minY = 10000;
		double y = 0;
		int    minYIdx = 0;
		for (int i=0; i<numBuckets; i++) {
			buckets[i].update(velocity);
			y = buckets[i].getY();
			minY = Math.min(y, minY); //find smallest y coordinate
			minYIdx = (minY == y) ? i : minYIdx;
		}
		buckets[minYIdx].mass += 0.35 / WaterwheelPanel.FPS; //increase  mass of top bucket
		buckets[minYIdx].mass = (buckets[minYIdx].mass > 0.5) ? 0.5 : buckets[minYIdx].mass; //limit mass to 1
		
		angularCoord += velocity / WaterwheelPanel.FPS;
	}
	
	/**
	 * Draw the wheel and its buckets to the window every frame.
	 * @param g - Graphics object used to draw wheel and buckets
	 */
	public void draw(Graphics g) {
		Graphics2D graphics = (Graphics2D)g;
		graphics.setColor(Color.ORANGE);
		graphics.setStroke(new BasicStroke(10));
		graphics.drawOval((int)(centreX-radius*100), (int)(centreY-radius*100), (int)(radius*200), (int)(radius*200));
		graphics.fillOval(centreX-20, centreY-20, 40, 40);
		graphics.drawLine(polarToX(angularCoord), polarToY(angularCoord), polarToX(angularCoord+Math.PI), polarToY(angularCoord+Math.PI));
		graphics.drawLine(polarToX(angularCoord+Math.PI/2), polarToY(angularCoord+Math.PI/2), polarToX(angularCoord+3*Math.PI/2), polarToY(angularCoord+3*Math.PI/2));
		graphics.fillRect(centreX-3, centreY-3, 6, 6);
		graphics.setStroke(new BasicStroke(1));
		
		for (int i=0; i<numBuckets; i++) {
			buckets[i].draw(graphics);
		}
		
		graphics.setColor(Color.CYAN);
		graphics.drawLine(centreX, (int)(centreY-radius*100-25), centreX, (int)(centreY-radius*100-5));
		graphics.drawLine(centreX-10, (int)(centreY-radius*100-25), centreX-10, (int)(centreY-radius*100-5));
		graphics.drawLine(centreX+10, (int)(centreY-radius*100-25), centreX+10, (int)(centreY-radius*100-5));
		graphics.setColor(Color.GRAY);
		graphics.fillRect(centreX-15, (int)(centreY-radius*100-40), 30, 15);
		
		drawPlot(graphics);
	 }
	
	/**
	 * Get the total mass of the wheel and all of the buckets.
	 * @return total mass (kg)
	 */
	private double mass() {
		double bucketMass = 0;
		for (var i=0; i<numBuckets; i++) {
			bucketMass += buckets[i].mass;
		}
		return bucketMass + wheelMass;
	}
	
	/**
	 * Convert a polar coordinate angle to a Cartesian x coordinate.
	 * @param angular - the angle of the polar coordinate
	 * @return the x coordinate of the point on the wheel at that angle
	 */
	private int polarToX(double angular) {
		return (int)(centreX + 100*radius*Math.cos(angular));
	}
	
	/**
	 * Convert a polar coordinate angle to a Cartesian y coordinate.
	 * @param angular - the angle of the polar coordinate
	 * @return the y coordinate of the point on the wheel at that angle
	 */
	private int polarToY(double angular) {
		return (int)(centreY - 100*radius*Math.sin(angular));
	}
	
	/**
	 * Get the x coordinate of the centre of mass of the wheel and buckets.
	 * @return x coordinate of centre of mass
	 */
	public double centreOfMassX() {
		double count = 0;
		for (var i=0; i<numBuckets; i++) {
			count += buckets[i].mass * buckets[i].getX();
		}
		return (count / mass());
	}
	
	/**
	 * Get the y coordinate of the centre of mass of the wheel and buckets.
	 * @return y coordinate of centre of mass
	 */
	public double centreOfMassY() {
		double count = 0;
		for (var i=0; i<numBuckets; i++) {
			count += buckets[i].mass * buckets[i].getY();
		}
		return (count / mass());
	}
	
	/**
	 * Update the plot of the wheel's centre of mass below the wheel every frame.
	 * @param g - the Graphics2D object to use to draw the plot
	 */
	private void drawPlot(Graphics2D g) {
		g.setColor(Color.RED);
		g.drawRect((int)(centreX+centreOfMassX()*6), (int)(centreY+radius*100+160+centreOfMassY()*6), 1, 1);
	}
}
