package iListeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import sablony.errorwin.ExceptionWin;
import sqlstorage.SQLStor;
import storage.SkladOdkazu;


/**
 * Listener pro pridavani noveho zakaznika a modelu
 * @author Havlicek
 *
 */
public class PridejZakaznikaList implements ActionListener {

	private SkladOdkazu sklad;
	private SQLStor sql;
	private static final String acesDenied = "execute command denied to user";
	
	public PridejZakaznikaList (SkladOdkazu sklad){
		this.sklad = sklad;
		this.sql = sklad.getSql();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String com = arg0.getActionCommand();
		try{
		if (com.equalsIgnoreCase("PridatZakaznikaAction")) {
			String novyZakaznik = sklad.getNoveJmenoZakaznikaTextField().getText();
			try {
				sql.novyZakaznik(novyZakaznik);
			} catch (SQLException e) {
				if(e.getLocalizedMessage() != null){
					if(e.getLocalizedMessage().startsWith("Duplicate entry")){
						JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Tento zákazník již v databázi existuje");
					} else if(e.getLocalizedMessage().startsWith(acesDenied)){
						JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Na tuto operaci nemáte pravomoce");
					} else {
						ExceptionWin.showExceptionMessage(e);
					}
				} else {
					ExceptionWin.showExceptionMessage(e);
				}
			}
		}
		else if(com.equalsIgnoreCase("PridejNovyModelAction")){
			Component [] comp = sklad.getNovyModelListTextComponent();
			String pom;
			String jmeno; String cisloModelu; String material; String materialVlastni; double hmotnost; boolean isOdhadHmot;  String formovna; double norma;
			// comp[0] je Id modelu
			jmeno = ((JTextField)comp[1]).getText();
			cisloModelu = ((JTextField)comp[2]).getText();
			material = ((JTextField)comp[3]).getText();
			materialVlastni = ((JTextField)comp[4]).getText();
			pom = ((JTextField)comp[5]).getText();
			try {
				pom = ((JTextField) comp[5]).getText();
				pom = pom.replace(',', '.');
				hmotnost = myRound(Double.parseDouble(pom), 2); 
				System.out.println("Moje hmotnost "+hmotnost);
				isOdhadHmot = ((JCheckBox) comp[6]).isSelected();
				formovna = String.valueOf((char) ((JComboBox<Character>) comp[7]).getSelectedItem());
				pom = ((JTextField) comp[8]).getText();
				pom = pom.replace(',', '.');
				norma = myRound(Double.parseDouble(pom), 1);
				System.out.println(norma);
				if(jmeno.isEmpty() || cisloModelu.isEmpty() || material.isEmpty() || materialVlastni.isEmpty() || formovna.isEmpty()){
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Nìjaké pole je prázdné");
					return;
				}
				try {
					sql.novyModel(jmeno, cisloModelu, material,	materialVlastni, hmotnost, isOdhadHmot, formovna,norma);
				} catch (SQLException e) {
					if(e.getLocalizedMessage().startsWith(acesDenied)){
						JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Na tuto operaci nemáte pravomoce");
					}else {
						ExceptionWin.showExceptionMessage(e);
					}
				}
			} catch (NumberFormatException e) {
				if(e.getMessage().equalsIgnoreCase("empty String")){
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Hmotnost nebo norma jsou prázdné");
				}
				if(e.getMessage().startsWith("For input string:")){
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Špatnì napsané èíslo v norme nebo hmotnosti");
				}
			}
		} else if (com.equalsIgnoreCase("UpravModelAction")){
			Component [] comp = sklad.getNovyModelListTextComponent();
			String pom;
			int idzakazky = 0;
			String jmeno; String cisloModelu; String material; String materialVlastni; double hmotnost; boolean isOdhadHmot;  String formovna; double norma;
			// comp[0] je Id modelu
			
			jmeno = ((JTextField)comp[1]).getText();
			cisloModelu = ((JTextField)comp[2]).getText();
			material = ((JTextField)comp[3]).getText();
			materialVlastni = ((JTextField)comp[4]).getText();
			pom = ((JTextField)comp[5]).getText();
			try {
				idzakazky = Integer.parseInt(((JLabel)comp[0]).getText());
				pom = ((JTextField) comp[5]).getText();
				pom = pom.replace(',', '.');
				hmotnost = myRound(Double.parseDouble(pom), 2); 
				System.out.println("Moje hmotnost "+hmotnost);
				isOdhadHmot = ((JCheckBox) comp[6]).isSelected();
				formovna = String.valueOf((char) ((JComboBox<Character>) comp[7]).getSelectedItem());
				pom = ((JTextField) comp[8]).getText();
				pom = pom.replace(',', '.');
				norma = myRound(Double.parseDouble(pom), 1);
				System.out.println(norma);
				if(jmeno.isEmpty() || cisloModelu.isEmpty() || material.isEmpty() || materialVlastni.isEmpty() || formovna.isEmpty()){
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Nìjaké pole je prázdné");
					return;
				}
				try {
					sql.updateModel(idzakazky, jmeno, cisloModelu, material,	materialVlastni, hmotnost, isOdhadHmot, formovna,norma);
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Model byl úspìšnì upraven");
				} catch (SQLException e) {
					if(e.getLocalizedMessage().startsWith(acesDenied)){
						JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Na tuto operaci nemáte pravomoce");
					}else {
						ExceptionWin.showExceptionMessage(e);
					}
				}
			} catch (NumberFormatException e) {
				if(e.getMessage().equalsIgnoreCase("empty String")){
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Hmotnost nebo norma jsou prázdné");
				}
				if(e.getMessage().startsWith("For input string:")){
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Špatnì napsané èíslo v norme nebo hmotnosti");
				}
			}
			
		} else if(com.equalsIgnoreCase("UpravZakaznikaAction")){
			int idZakaznika = Integer.parseInt(sklad.getIdZakaznikaText().getText());
			String noveJmeno = sklad.getNoveJmenoZakaznikaTextField().getText();
			try {
				sql.updateZakaznika(idZakaznika, noveJmeno);
				JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Zákazník byl upraven");
			} catch (SQLException e) {
				String pom = "Duplicate entry";
				if (e.getMessage().startsWith(pom)) {
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Tento zákazník již v databázi existuje");
				} else if(e.getLocalizedMessage().startsWith(acesDenied)){
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Na tuto operaci nemáte pravomoce");
				}else {
					ExceptionWin.showExceptionMessage(e);
				}
			}
			
		} else if(com.equalsIgnoreCase("PridatVinikaAction")){
			String novaVada = sklad.getNoveJmenoZakaznikaTextField().getText();
			try {
				sql.pridejVinika(novaVada);
			} catch (SQLException e) {
				String pom = "Duplicate entry";
				if (e.getMessage().startsWith(pom)) {
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Tento viník již v databázi existuje");
				} else if(e.getLocalizedMessage().startsWith(acesDenied)){
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Na tuto operaci nemáte pravomoce");
				} else {
					ExceptionWin.showExceptionMessage(e);
				}
			}
		} else if(com.equalsIgnoreCase("PridatVaduAction")){
			String novaVada = sklad.getNoveJmenoZakaznikaTextField().getText();
			try {
				sql.pridejVadu(novaVada);
			} catch (SQLException e) {
				String pom = "Duplicate entry";
				if (e.getMessage().startsWith(pom)) {
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Tato vada již v databázi existuje");
				} else if(e.getLocalizedMessage().startsWith(acesDenied)){
					JOptionPane.showMessageDialog(sklad.getHlavniOkno(), "Na tuto operaci nemáte pravomoce");
				} else {
					ExceptionWin.showExceptionMessage(e);
				}
			}
		}
		}catch(Exception e){
			ExceptionWin.showExceptionMessage(e);
		}
	}
	
	public double myRound(double cislo, int pocetMist){
		cislo = cislo*Math.pow(10, pocetMist);
		cislo = Math.round(cislo);
		cislo = cislo/Math.pow(10, pocetMist);
		return cislo;
	}

}
