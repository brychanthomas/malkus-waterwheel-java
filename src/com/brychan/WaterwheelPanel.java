package com.brychan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class WaterwheelPanel extends JPanel {
	
	static final int FPS = 40;
	MalkusWaterwheel[] wheels;
	
	WaterwheelPanel () {
		wheels = new MalkusWaterwheel[2];
		wheels[0] = new MalkusWaterwheel(100, 100);
		wheels[1] = new MalkusWaterwheel(350, 100);
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
		graphics.fillRect(0,  0, 550, 250);
		for (int i=0; i<wheels.length; i++) {
			wheels[i].paint(g);
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Malkus waterwheel");
		WaterwheelPanel mww = new WaterwheelPanel();
		frame.add(mww);
		frame.setSize(550, 250);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
