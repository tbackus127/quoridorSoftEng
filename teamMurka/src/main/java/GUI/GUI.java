package com.tmquoridor.GUI;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private JFrame frame;
    private JPanel panel;
    private JButton button;
    private JLabel label;


	public GUI() {
    gui();
    
	}

	public void gui() {
    	frame = new JFrame("Fuck this shit");
    	frame.setVisible(true);
    	frame.setSize(600, 400);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    	panel = new JPanel();
    	panel.setBackground(Color.BLUE);
    
    	button = new JButton("Test");
    	label = new JLabel("This is a test-label");
    
    	panel.add(button);
    	panel.add(label);
    
    	frame.add(panel);
	}


	public static void main(String [] args) {
    	new GUI();
	}
}
