package com.brychan;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * JPanel subclass to simulate several Malkus waterwheels in parallel.
 * 
 * @author Brychan Thomas
 *
 */
public class WaterwheelPanel extends JPanel {
	
	static final int FPS = 30;
	int frameCount = 0;
	MalkusWaterwheel[] wheels;
	FileWriter csvWriter;
	
	WaterwheelPanel (int numWaterwheels) throws IOException {
		wheels = new MalkusWaterwheel[numWaterwheels];
		for (int i = 0; i<numWaterwheels; i++) {
			wheels[i] = new MalkusWaterwheel(150+i*300, 200, Math.toRadians(1));
		}
		
		initCSV();
		
		ActionListener updater = new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent evt) {
	    		repaint();
	    	}
	    };
	    new Timer(1000/FPS, updater).start();
	}
	
	/**
	 * Draw everything on the window every frame.
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D)g;
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0,  0, 600, 400);
		if (frameCount < 3) {
			graphics.fillRect(0, 0, 900, 800);
		}
		graphics.setColor(Color.BLACK);
		g.setFont(new Font("Sans-serif", Font.PLAIN, 16)); 
		graphics.drawString("Centres of mass", 240, 420);
		graphics.drawString("Initial angle = 1.0°", 100, 20);
		graphics.drawString("Initial angle = 1.2°", 400, 20);
		graphics.drawLine(610, 0, 610, 800);
		graphics.drawLine(0, 390, 610, 390);
		for (int i=0; i<wheels.length; i++) {
			wheels[i].update();
			wheels[i].draw(g);
		}
		drawAttractors(graphics);
		writeCSV();
		frameCount++;
	}
	
	/**
	 * Update the Lorenz attractor graphs on the screen.
	 * @param graphics - the Graphics2D object to use to draw the graphs
	 */
	private void drawAttractors(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
		graphics.drawString("Lorenz attractor for left wheel:", 650, 75);
		graphics.setFont(new Font("Sans-serif", Font.PLAIN, 12)); 
		graphics.drawString("Velocity", 730, 300);
		graphics.drawString("Velocity", 730, 500);
		AffineTransform orig = graphics.getTransform();
		graphics.rotate(-Math.PI/2);
		graphics.drawString("Centre of mass X coord", -270, 630);
		graphics.drawString("Centre of mass Y coord", -480, 630);
		graphics.setTransform(orig);
		graphics.drawLine(745, 200, 755, 200);
		graphics.drawLine(750, 195, 750, 205);
		graphics.drawLine(745, 430, 755, 430);
		graphics.drawLine(750, 425, 750, 435);
		graphics.setColor(Color.GREEN);
		graphics.drawRect((int)(750+wheels[0].velocity*80), (int)(200-wheels[0].centreOfMassX()*6), 1, 1);
		graphics.drawRect((int)(750+wheels[0].velocity*80), (int)(430+wheels[0].centreOfMassY()*6), 1, 1);
	}
	
	/**
	 * Open the CSV file and write the column titles to it.
	 */
	private void initCSV() throws IOException {
		csvWriter = new FileWriter("data.csv");
		csvWriter.append("time,");
		for (int i=0; i<wheels.length; i++) {
			csvWriter.append("wheel"+i+" angular velocity,");
			csvWriter.append("wheel"+i+" centre of mass x,");
			csvWriter.append("wheel"+i+" centre of mass y,");
		}
	}
	
	/**
	 * Write the current velocities and centre of mass coordinates of the
	 * wheels to the CSV files.
	 */
	private void writeCSV() {
		try {
			double time = (double)frameCount / FPS;
			csvWriter.append(Double.toString(time)+",");
			for (int i=0; i<wheels.length; i++) {
				csvWriter.append(Double.toString(wheels[i].velocity)+",");
				csvWriter.append(Double.toString(wheels[i].centreOfMassX())+",");
				csvWriter.append(Double.toString(wheels[i].centreOfMassY())+",");
			}
		} catch (IOException e) {
			System.out.println("IOException!");
		}
	}
	
	/**
	 * Close the CSV file.
	 */
	private void closeCSV() {
		try {
			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			System.out.println("IOException!");
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Malkus waterwheel");
		try {
			WaterwheelPanel panel = new WaterwheelPanel(2);
			frame.add(panel);
			frame.setSize(900, 600);
			frame.setVisible(true);
			frame.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	                panel.closeCSV();
	                System.exit(0);
	            }
	        });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
