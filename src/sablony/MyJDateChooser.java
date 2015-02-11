package sablony;

import java.util.Date;

import com.toedter.calendar.JDateChooser;

public class MyJDateChooser extends JDateChooser {
	
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;

	public MyJDateChooser(){
		super(new Date());				
	}

}
