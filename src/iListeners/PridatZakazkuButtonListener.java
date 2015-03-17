package iListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import com.toedter.calendar.JDateChooser;

import sablony.DateStor;
import sablony.errorwin.ExceptionWin;
import sqlstorage.SQLStor;
import storage.SkladOdkazu;
import app.MainFrame;
import app.PromOknoNovaZakazka;
import app.PromOknoNovyZakaznikAndSearch;

/**
 * Tøída pro obsluhu tlaèítka ve tøídì PromOknoNovaZakazka jbutton, konkrétnì pro najdi model a najdi zakazníka<br>
 * a pak tak pro obsluhu tlaèítka které pøidává popøípadì upravuje zakázku (uplnì v levo dole)
 * @author Havlicek
 *
 */
public class PridatZakazkuButtonListener implements ActionListener {
	
	private MainFrame hlavniOkno;
	private PromOknoNovyZakaznikAndSearch okno;
	private PromOknoNovaZakazka oknoNovaZakazka;
	private SkladOdkazu sklad;
	private SQLStor sql;
	/**
	 *  listComponents[0][0] = idZakazkyLabel;<br>
		listComponents[1][0] = textVyberZakaznika; //jmeno zakaznika<br>
		listComponents[2][0] = textCisloObjednavky;<br>
		listComponents[3][0] = textVyberModel;  //Cislo modelu<br>
		listComponents[4][0] = datumPrijetiZakazky;<br>
		listComponents[5][0] = textPocetKusu;<br>
		listComponents[6][0] = datumExpedice;<br>
		listComponents[7][0] = textCena; 	    <br>
		listComponents[8][0] = czk; <br>
		listComponents[8][1] = eur; <br>
		listComponents[9][0] = zaKus; <br>
		listComponents[9][1] = zaKg; <br>
		listComponents[10][0] = kurzEuCzk;<br>
		listComponents[11][0] = textVyberModel; //id modelu<br>
		listComponents[12][0] = textVyberModel; //jmeno modelu<br>
		listComponents[13][0] = ; //material<br>
		listComponents[14][0] = ; //vlastni material<br>
		listComponents[15][0] = ; //Formovna<br>
		listComponents[16][0] = ; //hmotnost<br>
		listComponents[17][0] = ; //is odhadova hmotnost<br>
		listComponents[18][0] = ; //norma slevac<br>
		listComponents[19][0] = textVyberZakaznika; //id zakaznika<br>
		listComponents[20][0] = uzavrenaCheck; // je uzavrena zakazka?<br>
		listComponents[21][0] = textPoznamka; //poznamka<br>
		listComponents[22][0] = textPaganyrka; //datumExpedice<br>
	 */
	private JComponent[][] listComponents;
	
	private final String [] actionCommands = {"NajdiZakaznika", "NajdiModel","PridejZakazku", "UpravZakazku"};
	
	public PridatZakazkuButtonListener(MainFrame hlavniOkno, PromOknoNovaZakazka oknoNovaZakazka){
		this.oknoNovaZakazka = oknoNovaZakazka;
		this.listComponents = oknoNovaZakazka.getListComponents();
		this.hlavniOkno = hlavniOkno;
		this.okno = this.hlavniOkno.getVyhledavac();
		this.sklad = hlavniOkno.getSklad();
		this.sql = sklad.getSql();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			try {
				sklad.getPromOknoNovyZakaznikSearchColorTable().setModel(sklad.getPrazdneTabulky()[5]);
				sklad.getPromOknoNovyZakaznikAndSearchColumAdjuster().adjustColumns();
				if (arg0.getActionCommand().equals(actionCommands[0])) {
					okno.setNovaZakazkaVyberZakaznika();
					hlavniOkno.setWindow(0);
				} else if (arg0.getActionCommand().equals(actionCommands[1])) {
					okno.setNovaZakazkaVyberModel();
					hlavniOkno.setWindow(0);
				} else if (arg0.getActionCommand().equals(actionCommands[2])) {
					novaZakazka();
				} else if (arg0.getActionCommand().equals(actionCommands[3])) {
					upravitZakazku();
				}
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(hlavniOkno, "Kurz, cena nebo poèet kusù jsou napsány špatnì");
				return;
			}
		} catch (Exception e1) {
			ExceptionWin.showExceptionMessage(e1);
			e1.printStackTrace();
		}
	}
	
	private void novaZakazka() throws SQLException, NumberFormatException {
		double kurz, cena;
		String stringKurz = ((JTextField)listComponents[10][0]).getText();
		if(stringKurz != null){
			stringKurz = stringKurz.replace(',', '.');
			if(((JRadioButton)listComponents[8][0]).isSelected()) kurz = 0;
			else kurz = myRound(Double.parseDouble(stringKurz), 4);
		} else {
			kurz = -1.1;
		}
		
		String cenaString = (((JTextField)listComponents[7][0]).getText());
		cenaString = cenaString.replace(',', '.');
		cena = myRound(Double.parseDouble(cenaString), 3);
		
		String paganyrka = ((JTextField)listComponents[22][0]).getText();
		if(paganyrka.equalsIgnoreCase("0-0")){
			JOptionPane.showMessageDialog(hlavniOkno, "Øíkal jsem, že pokud neznáte paganýrku, nechte pole prázdné ;-)");
			return;
		}
		int idZakaznika = oknoNovaZakazka.getIdIntAktualnihoZakaznika();
		int idModelu = oknoNovaZakazka.getIdIntAktualnihoModelu();
		DefaultComboBoxModel<String> zakaznikComboModel = oknoNovaZakazka.getZakaznikComboModel();
		DefaultComboBoxModel<String> modelNoveZakazkyComboModel = oknoNovaZakazka.getModelNoveZakazkyComboModel();
		if(zakaznikComboModel.getSize() <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Není vybraný žádný zákazník");
			return;
		}
		if(modelNoveZakazkyComboModel.getSize() <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Není vybraný žádný model");
			return;
		}
		String selectedString = null;
		selectedString = (String) zakaznikComboModel.getSelectedItem();
		if(!selectedString.contains(" "+Integer.toString(idZakaznika)+" ")){
			JOptionPane.showMessageDialog(hlavniOkno, "Není vybraný žádný zákazník");
			return;
		}
		selectedString = (String) modelNoveZakazkyComboModel.getSelectedItem();
		if(!selectedString.contains(" "+Integer.toString(idModelu)+" ")){
			JOptionPane.showMessageDialog(hlavniOkno, "Není vybraný žádný model");
			return;
		}
		
		int id = sql.novaZakazka(
				idZakaznika,
				idModelu,
				((JTextField)listComponents[2][0]).getText(),
				((JDateChooser)listComponents[4][0]).getDate(),
				Integer.parseInt(((JTextField)listComponents[5][0]).getText()),
				paganyrka,
				cena,
				((JRadioButton)listComponents[8][0]).isSelected(),
				((JRadioButton)listComponents[9][0]).isSelected(),
				kurz,
				((JTextComponent)listComponents[21][0]).getText(),
				((JDateChooser)listComponents[6][0]).getDate(),
				oknoNovaZakazka.getModel()
				);
		
		if(id <= 0){ // pokud se nepovedla zakazka nesmím pøidat dilèí termíny
			return;
		}
		
		DefaultListModel<DateStor> mod = oknoNovaZakazka.getModel();
		DateStor pom;
		for(int i = 0;  i < mod.size(); i++){
			pom = mod.get(i);
			sql.zadejDilciTermin(id, pom.getDate(), pom.getPocetKusu());
		}
	}
	
	private void upravitZakazku() throws NumberFormatException, SQLException{		
		double kurz, cena;
		String stringKurz = ((JTextField)listComponents[10][0]).getText();
		if(stringKurz != null){
			stringKurz = stringKurz.replace(',', '.');
			if(((JRadioButton)listComponents[8][0]).isSelected()) kurz = 0;
			else kurz = myRound(Double.parseDouble(stringKurz), 4);
		} else {
			kurz = -1.1;
		}
		
		String cenaString = (((JTextField)listComponents[7][0]).getText());
		cenaString = cenaString.replace(',', '.');
		cena = myRound(Double.parseDouble(cenaString), 3);
		
		String paganyrka = ((JTextField)listComponents[22][0]).getText();
		if(paganyrka.equalsIgnoreCase("0-0")){
			JOptionPane.showMessageDialog(hlavniOkno, "Øíkal jsem, že pokud neznáte paganýrku, nechte pole prázdné ;-)");
			return;
		}
		
		int idZakaznika = oknoNovaZakazka.getIdIntAktualnihoZakaznika();
		int idModelu = oknoNovaZakazka.getIdIntAktualnihoModelu();
		DefaultComboBoxModel<String> zakaznikComboModel = oknoNovaZakazka.getZakaznikComboModel();
		DefaultComboBoxModel<String> modelNoveZakazkyComboModel = oknoNovaZakazka.getModelNoveZakazkyComboModel();
		if(zakaznikComboModel.getSize() <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Není vybraný žádný zákazník");
			return;
		}
		if(modelNoveZakazkyComboModel.getSize() <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Není vybraný žádný model");
			return;
		}
		String selectedString = null;
		selectedString = (String) zakaznikComboModel.getSelectedItem();
		if(!selectedString.contains(" "+Integer.toString(idZakaznika)+" ")){
			JOptionPane.showMessageDialog(hlavniOkno, "Není vybraný žádný zákazník");
			return;
		}
		selectedString = (String) modelNoveZakazkyComboModel.getSelectedItem();
		if(!selectedString.contains(" "+Integer.toString(idModelu)+" ")){
			JOptionPane.showMessageDialog(hlavniOkno, "Není vybraný žádný model");
			return;
		}
		
		int id = Integer.parseInt(((JLabel)listComponents[0][0]).getText());
		sql.updateZakazku(
				id,
				idZakaznika,
				idModelu,
				((JTextField)listComponents[2][0]).getText(),
				((JDateChooser)listComponents[4][0]).getDate(),
				Integer.parseInt(((JTextField)listComponents[5][0]).getText()),
				paganyrka,
				cena,
				((JRadioButton)listComponents[8][0]).isSelected(),
				((JRadioButton)listComponents[9][0]).isSelected(),
				kurz,
				((JTextComponent)listComponents[21][0]).getText(),
				((JDateChooser)listComponents[6][0]).getDate()
				);
		
		DefaultListModel<DateStor> mod = oknoNovaZakazka.getModel();
		DateStor pom;
		for(int i = 0;  i < mod.size(); i++){
			pom = mod.get(i);
			sql.zadejDilciTermin(id, pom.getDate(), pom.getPocetKusu());
		}
		
	}
	
	public double myRound(double cislo, int pocetMist){
		cislo = cislo*Math.pow(10, pocetMist);
		cislo = Math.round(cislo);
		cislo = cislo/Math.pow(10, pocetMist);
		return cislo;
	}

}
