package org.glasser.qform;

import javax.swing.JButton;

public class ButtonConfig {
	public final JButton button;
	public final Character shortCut;
	public final String text;
	public final String help;
	
	public ButtonConfig(JButton button, Character shortCut, String text, String help) {
		super();
		this.button = button;
		this.shortCut = shortCut;
		this.text = text;
		this.help = help;
	}

	public ButtonConfig(Object[] button) {
		this((JButton) button[0], (Character)  button[1].toString().charAt(0), (String) button[2], (String) button[3]);
	}
}