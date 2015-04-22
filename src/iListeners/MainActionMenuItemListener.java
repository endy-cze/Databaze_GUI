package iListeners;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sablony.MyJButton;
import sablony.ParametryFiltr;
import sablony.errorwin.ExceptionWin;
import storage.SkladOdkazu;
import app.MainFrame;
import app.PromOknoNovaZakazka;
import app.PromOknoNovyModel;
import app.PromOknoNovyZakaznikAndSearch;
import myinterface.NastavOkno;

/**
 * Tøída, která obsluhuje údálosti vytvoøené PopUpMenu itemy v PopupMenu, které se vytvoøí
 * pøi najetí tlaèitka na postrani menu v hlavním oknì
 * @author Ondøej Havlíèek
 *
 */
public class MainActionMenuItemListener implements ActionListener {
	private SkladOdkazu sklad;
	
	//pomocne ukazatele
	private String sourceString;
	private String pomString;
	private PromOknoNovyZakaznikAndSearch tmp;
	private PromOknoNovyModel tmp2;
	private PromOknoNovaZakazka tmp3;
	
	//Componenty hlavniho okna
	private MainFrame hlavniOkno;
	private JMenuItem [][] sidePopupMenuItems;
	private JPanel [] promOkna;
	private JLabel [] navigatorLabels;
	private MyJButton [] sideListButton;
	
 	public MainActionMenuItemListener(SkladOdkazu sklad){
		this.sklad = sklad;
		this.hlavniOkno = this.sklad.getHlavniOkno();
		this.sidePopupMenuItems = sklad.getSidePopupMenuItems();
		this.promOkna = sklad.getPromOkna();
		this.navigatorLabels = sklad.getNavigatorLabels();
		this.sideListButton = sklad.getSideListButton();
	}
	

	@Override
	public void actionPerformed(ActionEvent arg0) {	
		try {
			boolean end = false;
			sourceString = arg0.getActionCommand();
			int i = 0, j = 0;
			for (i = 0; i < sidePopupMenuItems.length; i++) {
				for (j = 0; j < sidePopupMenuItems[i].length; j++) {
					if (sidePopupMenuItems[i][j] == null) {
						break;
					} else {
						pomString = sidePopupMenuItems[i][j].getText();
						if (pomString.equalsIgnoreCase(sourceString)) {
							end = true;
							break;
						}
					}
				}
				if (end) {
					break;
				}
			}
			// vyprazdnime tabulku prohledávání
			sklad.getPromOknoNovyZakaznikSearchColorTable().setModel(sklad.getPrazdneTabulky()[5]);
			sklad.getPromOknoNovyZakaznikAndSearchColumAdjuster().adjustColumns();
			
			if (i != 0) {
				hlavniOkno.setWindow(0);
				((NastavOkno) promOkna[0]).nastavOkno(i, j);

			} else {
				// pro nova Zakazka model a zakaznik
				hlavniOkno.setWindow(j);
				((NastavOkno) promOkna[j]).nastavOkno(0, j);
			}

			navigatorLabels[2].setText(sideListButton[i].getText());
			navigatorLabels[4].setText(sidePopupMenuItems[i][j].getActionCommand());
		
			switch (i) {
			case 0:
				index0(j);
				break;
			case 1:
				index1(j);
				break;
			case 2:
				index2(j);
				break;
			case 3:
				index3(j);
				break;
			case 4:
				// vypisy
				index4(j);
				break;
			case 5:
				index5(j);
				break;
			case 6:
				index6(j);
				break;
			case 7:
				index7(j);
				break;
			case 8:
				index8(j);
				break;
			default:
				System.out.println("Nothing hapend, MainActionListener 115");
				throw new Exception("Jsou spatne naindexovany okna. Mas vic sidebutonu. MainActionListener 115 ");
			}
		} catch (Exception e) {
			ExceptionWin.showExceptionMessage(e);
		}
		
	}
	
	/**
	 * Obsluha Popup menu pro novy zakaznik, model a zakazka
	 * @param j index v PopUpMenu (cislo radku na kterem je v popupmenu umisten)
	 */
	private void index0(int j){
		switch(j){
		case 0:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setPridejZakaznika();
			break;
		case 1:
			tmp2 = (PromOknoNovyModel) promOkna[1];
			tmp2.setNovyModel();
			break;
		case 2:
			tmp3 = (PromOknoNovaZakazka) promOkna[2];
			tmp3.setNovaZakazka();
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index0()");	
			break;
		}
		
		
	}
	/**
	 * Obsluha Popup menu pro Veškere prohlížení a Plánování
	 * @param j index v PopUpMenu (cislo radku na kterem je v popupmenu umisten)
	 */
	private void index1(int j){
		switch(j){
		case 0:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setPlanovani();
			break;
		case 1:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejZakaznika();
			break;
		case 2:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejModel();
			break;
		case 3:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejZakazku();
			break;
		case 4:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejFyzKusy();
			break;
		case 5:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejZmetky();
			break;
		case 6:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejViniky();
			break;
		case 7:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejVady();
			break;
		case 8:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejKapPropocet();
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index1()");	
			break;
		}		
	}
	
	/**
	 * Obsluha pro zadávání odlitkù a zadávání zmetkù
	 * @param j index v PopUpMenu (cislo radku na kterem je v popupmenu umisten)
	 */
	private void index2(int j){
		switch(j){
		case 0:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setZadejOdlitek();
			break;
		case 1:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setZadejVycistenyKus();
			break;
		case 2:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setUpravCisloTavby();
			break;
		case 3:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejUpravModel();
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index2()");	
			break;
		}
		
	}
	
	/**
	 * Expedice a licí plány
	 * @param j
	 */
	private void index3(int j){
		ParametryFiltr panelFiltr;
		switch(j){
		case 0:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setZadejOdlitek();
			break;
		case 1:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			panelFiltr = tmp.getPanelFiltr();
			panelFiltr.setPlanyLiti(true);
			break;
		case 2:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			panelFiltr = tmp.getPanelFiltr();
			panelFiltr.setPlanyLiti(false);
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index3()");	
			break;
		}
		
	}
	
	/**
	 * Vypisy
	 * @param j index v PopUpMenu (cislo radku na kterem je v popupmenu umisten)
	 */
	private void index4(int j){
		tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
		ParametryFiltr panelFiltr = tmp.getPanelFiltr();
		panelFiltr.setVypisy(j);		
	}

	/**
	 * Obsluha Popup menu pro Upravit zákazníky, modely a zakázky a zadávání Èísla tavby
	 * @param j index v PopUpMenu (cislo radku na kterem je v popupmenu umisten)
	 */
	private void index5(int j){
		switch(j){
		case 0:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejUpravZakaznika();
			break;
		case 1:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejUpravModel();
			break;
		case 2:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejUpravZakazku();
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index5()");	
			break;
		}
		
	}
	
	private void index6(int j){
		switch(j){
		case 0:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setPridejVinika();
			break;
		case 1:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setPridejVadu();
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index6()");	
			break;
		}
	}
	
	private void index7(int j){
		System.out.println("7");
		switch(j){
		case 0:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejZakaznika();
			break;
		case 1:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejModel();
			break;
		case 2:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setVyhledejZakazku();
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index7()");	
			break;
		}
	}
	
	private void index8(int j){
		switch(j){
		case 0:
			tmp = (PromOknoNovyZakaznikAndSearch) promOkna[0];
			tmp.setZalohaDB();
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index7()");	
			break;
		}
	}
	
}
