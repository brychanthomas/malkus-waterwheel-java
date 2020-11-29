package com.brychan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * JPanel subclass to simulate several Malkus waterwheels in parallel.
 * 
 * @author Brychan
 *
 */
public class WaterwheelPanel extends JPanel {
	
	static final int FPS = 30;
	int frameCount = 0;
	MalkusWaterwheel[] wheels;
	FileWriter csvWriter;
	
	WaterwheelPanel () throws IOException {
		wheels = new MalkusWaterwheel[2];
		wheels[0] = new MalkusWaterwheel(150, 200, Math.toRadians(1));
		wheels[1] = new MalkusWaterwheel(450, 200, Math.toRadians(2));
		
		initCSV();
		
		ActionListener updater = new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent evt) {
	    		repaint();
	    	}
	    };
	    new Timer(1000/FPS, updater).start();
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D)g;
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0,  0, 700, 400);
		for (int i=0; i<wheels.length; i++) {
			wheels[i].update();
			wheels[i].draw(g);
		}
		writeCSV();
		frameCount++;
	}
	
	private void initCSV() throws IOException {
		csvWriter = new FileWriter("data.csv");
		csvWriter.append("time,");
		csvWriter.append("wheel1 angular velocity,");
		csvWriter.append("wheel2 angular velocity,");
		csvWriter.append("wheel1 centre of mass x,");
		csvWriter.append("wheel2 centre of mass x,");
		csvWriter.append("wheel1 centre of mass y,");
		csvWriter.append("wheel2 centre of mass y\n");
	}
	
	private void writeCSV() {
		try {
			double time = (double)frameCount / FPS;
			csvWriter.append(Double.toString(time)+",");
			csvWriter.append(Double.toString(wheels[0].velocity)+",");
			csvWriter.append(Double.toString(wheels[1].velocity)+",");
			csvWriter.append(Double.toString(wheels[0].centreOfMassX())+",");
			csvWriter.append(Double.toString(wheels[1].centreOfMassX())+",");
			csvWriter.append(Double.toString(wheels[0].centreOfMassY())+",");
			csvWriter.append(Double.toString(wheels[1].centreOfMassY())+"\n");
		} catch (IOException e) {
			System.out.println("IOException!");
		}
	}
	
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
			WaterwheelPanel panel = new WaterwheelPanel();
			frame.add(panel);
			frame.setSize(630, 400);
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
