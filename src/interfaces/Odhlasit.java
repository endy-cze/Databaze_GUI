package interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sablony.errorwin.ExceptionWin;
import storage.SkladOdkazu;

public class Odhlasit implements ActionListener {
	private final String odhlasitCom = "OdhlasitComand";
	private SkladOdkazu sklad;
	
	public Odhlasit(SkladOdkazu s){
		this.sklad = s;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase(odhlasitCom)){
			try {
				sklad.getSql().closeConnections();
				System.out.println("Vypínám všechna pøipojení");
				sklad.getHlavniOkno().odhlasit();
				JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Úspìšnì jste se odhlásili, ukonèuji aplikaci");
				System.exit(0);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				ExceptionWin win = new ExceptionWin(e1, true);
			}
		} else {
			JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Pøi odhlašování se stala nìjaká chyba nebo jsem použil špatný listener, ukonèuji aplikaci");
			System.exit(0);
		}
	}
}
