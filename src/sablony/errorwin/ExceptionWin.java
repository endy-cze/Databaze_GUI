package sablony.errorwin;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class ExceptionWin {
	private String error;
	
	/**
	 * 
	 * @param e
	 */
	private ExceptionWin(Exception e){
		
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
    	e.printStackTrace(pw);
    	error = sw.toString();
    	ErrorWin okno = ErrorWin.createErrorWin(error, false);
    	okno.setVisible(true);
	}
	
	public static ExceptionWin showExceptionMessage(Exception e){
		e.printStackTrace();
		ExceptionWin win = new ExceptionWin(e);
		
		/*
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
    	e.printStackTrace(pw);
    	String error = sw.toString();
    	ErrorWin okno = ErrorWin.createErrorWin(error, false);
    	okno.setVisible(true);
    	*/
		return win;
	}

}
