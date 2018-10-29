package org.glasser.qform;

import javax.swing.JComponent;

public class FormFields {
	public final JComponent textField;
	public final String label;
	public final String help;
	
	public FormFields(JComponent textField, String label, String help) {
		super();
		this.textField = textField;
		this.label = label;
		this.help = help;
	}

	public FormFields(Object[] field) {
		this((JComponent) field[0], (String) field[1], (String) field[1]);
	}
}