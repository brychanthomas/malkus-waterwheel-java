package com.brychan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MalkusWaterwheel extends JPanel {
	Bucket[] buckets;
	double velocity = 0;
	double radius = 1;
	double wheelMass = 10;
	int    numBuckets = 8;
	static final int FPS = 40;
	
	MalkusWaterwheel () {
		buckets = new Bucket[numBuckets];
		double offset = Math.PI/16;
		for (int i = 0; i<numBuckets; i++) {
			buckets[i] = new Bucket((i*2*Math.PI/numBuckets)+offset, radius*100);
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D)g;
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0,  0, 550, 250);
		graphics.setColor(Color.ORANGE);
		graphics.fillOval(10, 0, (int)radius*200, (int)radius*200);
		graphics.setColor(Color.WHITE);
		graphics.fillOval(20, 10, (int)radius*200-20, (int)radius*200-20);
		double force = -velocity*3.2;
		for (int i=0; i<numBuckets; i++) {
			force += buckets[i].calculateForce();
		}
		double acc = (force) / (mass() * radius);
		velocity += acc / FPS;
		double minY = 10000;
		double y = 0;
		int    minYIdx = 0;
		for (int i=0; i<numBuckets; i++) {
			buckets[i].update(velocity);
			y = buckets[i].draw(graphics);
			minY = Math.min(y, minY); //find smallest y coordinate
			minYIdx = (minY == y) ? i : minYIdx;
		}
		buckets[minYIdx].mass += 0.3 / FPS; //increase  mass of top bucket
		buckets[minYIdx].mass = (buckets[minYIdx].mass > 0.5) ? 0.5 : buckets[minYIdx].mass; //limit mass to 1
	 }
	
	private double mass() {
		double bucketMass = 0;
		for (var i=0; i<numBuckets; i++) {
			bucketMass += buckets[i].mass;
		}
		return bucketMass + wheelMass;
	}
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Malkus waterwheel");
		MalkusWaterwheel mww = new MalkusWaterwheel();
		frame.add(mww);
		frame.setSize(550, 250);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ActionListener updater = new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent evt) {
	    		mww.repaint();
	    	}
	    };
	    new Timer(1000/FPS, updater).start();
	}
}
