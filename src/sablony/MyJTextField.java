package sablony;

import java.awt.Font;

import javax.swing.JTextField;

public class MyJTextField extends JTextField {
	
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;

	public MyJTextField(){
		super();
		this.setFont(new Font("Tahoma", Font.BOLD, 14));
	}
	

}
