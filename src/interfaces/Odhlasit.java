package interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sablony.errorwin.ExceptionWin;
import storage.SkladOdkazu;

public class Odhlasit implements ActionListener, WindowListener {
	private final String odhlasitCom = "OdhlasitComand";
	private SkladOdkazu sklad;
	
	public Odhlasit(SkladOdkazu s){
		this.sklad = s;
	}
	/**
	 * 
	 * @param i cislo do System.exit(i)
	 */
	public void odhlasit(int i){
		try {
			sklad.getSql().closeConnections();
			System.out.println("Vypínám všechna pøipojení");
			sklad.getHlavniOkno().odhlasit();
			//JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Úspìšnì jste se odhlásili, ukonèuji aplikaci");
			System.gc();
			System.exit(i);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			ExceptionWin win = new ExceptionWin(e1, true);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase(odhlasitCom)){
			odhlasit(0);
		} else {
			JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Pøi odhlašování se stala nìjaká chyba nebo jsem použil špatný listener, ukonèuji aplikaci");
			odhlasit(-1);
		}
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		odhlasit(0);
	}
	@Override
	public void windowClosed(WindowEvent e) {

	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
