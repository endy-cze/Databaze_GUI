package iListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import sablony.MyJButton;
import sablony.ParametryFiltr;
import sablony.errorwin.ExceptionWin;
import storage.SkladOdkazu;
import app.MainFrame;
import app.Navigator;
import app.PromOknoNovaZakazka;
import app.PromOknoNovyModel;
import app.PromOknoNovyZakaznikAndSearch;

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
	private PromOknoNovyZakaznikAndSearch searchOkno;
	private PromOknoNovyModel novyModelOkno;
	private PromOknoNovaZakazka novaZakazOkno;
	
	//Componenty hlavniho okna
	private MainFrame hlavniOkno;
	private JMenuItem [][] sidePopupMenuItems;
	private Navigator navigace;
	private MyJButton [] sideListButton;
	
 	public MainActionMenuItemListener(SkladOdkazu sklad, PromOknoNovyZakaznikAndSearch searchOkno, PromOknoNovyModel novyModel, PromOknoNovaZakazka novaZakazOkno){
		this.sklad = sklad;
		this.hlavniOkno = this.sklad.getHlavniOkno();
		this.sidePopupMenuItems = sklad.getSidePopupMenuItems();
		this.sideListButton = sklad.getSideListButton();
		this.navigace = sklad.getNavigator();
		
		// okna
		this.searchOkno = searchOkno;
		this.novyModelOkno = novyModel;
		this.novaZakazOkno = novaZakazOkno;
	}
	

	@Override
	public void actionPerformed(ActionEvent arg0) {	
		hlavniOkno.getScrollPane().getHorizontalScrollBar().setValue(0);
		hlavniOkno.getScrollPane().getVerticalScrollBar().setValue(0);
		try {
			// hledani ktery popupmenu byl zmacknut (podle indexu)
			boolean end = false;
			sourceString = arg0.getActionCommand();
			int i = 0, j = 0;
			for (i = 0; i < sidePopupMenuItems.length; i++) {
				for (j = 0; j < sidePopupMenuItems[i].length; j++) {
					if (sidePopupMenuItems[i][j] == null) {
						break;
					} else {
						pomString = sidePopupMenuItems[i][j].getActionCommand();
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
			
			// Nastaveni okna
			nastavOknoAplikace(i,j);

			// zmena navigatoru			
			this.navigace.setNavigatorLabels(sideListButton[i].getText(), sidePopupMenuItems[i][j].getText());
			
			// modifikace okna PromOknoNovyZakaznikAndSearch nebo PromOknoNovyModel nebo PromOknoNovaZakazka
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
			case 4:// vypisy				
				index4(j);
				break;
			case 5:
				index5(j);
				break;
			case 6:
				index6(j);
				break;
			case 7: // smazani
				index7(j);
				break;
			case 8:
				index8(j);
				break;
			default:
				throw new Exception("Jsou spatne naindexovany okna. Mas vic sidebutonu. MainActionListener 125 ");
			}
		} catch (Exception e) {
			ExceptionWin.showExceptionMessage(e);
		}
	}
	
	/**
	 * <p>Metoda urci, ktere panely maji byt viditelne a ktere ne. Ovlivnuje i jak maji byt panely usporadane. Repskektive ovlivnuje pouze JPanel <code> PromOknoNovyZakaznikAndSearch</code>.
	 * Podle indexu urci co se ma provest za akci. Usporadani uvnitø panelu se zatim dela i jinde. to se musi zmenit. </p>
	 * <p>V podstate se vždy zobrazí jen modifikované okno <code>PromOknoNovyZakaznikAndSearch</code>, krom dvou pøípadù a to když chceme vytvoøit novou zakazku nebo nový model.</p>
	 * <p>To je logické, protože když necheme neco noveho tak chceme neco upravit a to musime nejdriv najit :D <code>PromOknoNovyZakaznikAnd<b>Search</b></code></p>
	 * @param i index <code>JMenuItem</code> v lokalni promene <code>sidePopupMenuItems</code> 
	 * @param j index <code>JMenuItem</code> v lokalni promene <code>sidePopupMenuItems</code>
	 */
	private void nastavOknoAplikace(int i, int j){
		if(i == 0){ // zobrazujeme jine okno nez PromOknoNovyZakaznikAndSearch
			hlavniOkno.setWindow(j); //zobrazime dany JPanel => 0 pro PromOknoNovyZakaznikAndSearch, 1 pro PromOknoNovyModel a 2 pro PromOknoNovaZakazka
			switch (j) {
			case 0:
				searchOkno.nastavOkno(i, j);
				break;
			case 1:
				novyModelOkno.setNovyModel();
				break;
			case 2:
				novaZakazOkno.setNovaZakazka();
				break;
			default:
				JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, nastavOknoAplikace()");
				break;
			}
		} else {
			hlavniOkno.setWindow(0); // vsude krome pro novy model a novy zakazka vede menu item na PromOknoNovyZakaznikAndSearch nejak modifikovany
			searchOkno.nastavOkno(i, j); // modifikace-nastaveni okna PromOknoNovyZakaznikAndSearch
		}
		
	}
	
	/**
	 * Obsluha Popup menu pro novy zakaznik, model a zakazka
	 * @param j index v PopUpMenu (cislo radku na kterem je v popupmenu umisten)
	 */
	private void index0(int j){
		switch(j){
		case 0:
			searchOkno.setPridejZakaznika();
			break;
		case 1:
			novyModelOkno.setNovyModel();
			break;
		case 2:
			novaZakazOkno.setNovaZakazka();
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
			searchOkno.setPlanovani();
			break;
		case 1:
			searchOkno.setVyhledejZakaznika();
			break;
		case 2:
			searchOkno.setVyhledejModel();
			break;
		case 3:
			searchOkno.setVyhledejZakazku();
			break;
		case 4:
			searchOkno.setVyhledejFyzKusy();
			break;
		case 5:
			searchOkno.setVyhledejZmetky();
			break;
		case 6:
			searchOkno.setVyhledejViniky();
			break;
		case 7:
			searchOkno.setVyhledejVady();
			break;
		case 8:
			searchOkno.setVyhledejKapPropocet();
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
			searchOkno.setZadejOdlitek();
			break;
		/*case 1:
			searchOkno.setZadejVycistenyKus();
			break;
		case 2:
			searchOkno.setUpravCisloTavby();
			break;
		case 3:*/
		case 1:
			searchOkno.setVyhledejUpravModel();
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
		switch(j){
		case 0:
			searchOkno.setZadejOdlitek();
			break;
		case 1:
			searchOkno.setPanelFiltrSetPlanyLiti(true);
			break;
		case 2:
			searchOkno.setPanelFiltrSetPlanyLiti(false);
			break;
		case 3:
			searchOkno.setPlanExpedice();
			searchOkno.setPanelFiltrSetPlanExpedice(j);
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
		searchOkno.setPanelFiltrVypisy(j);
	}

	/**
	 * Obsluha Popup menu pro Upravit zákazníky, modely a zakázky a zadávání Èísla tavby
	 * @param j index v PopUpMenu (cislo radku na kterem je v popupmenu umisten)
	 */
	private void index5(int j){
		switch(j){
		case 0:
			searchOkno.setVyhledejUpravZakaznika();
			break;
		case 1:
			searchOkno.setVyhledejUpravModel();
			break;
		case 2:
			searchOkno.setVyhledejUpravZakazku();
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index5()");	
			break;
		}
		
	}
	
	private void index6(int j){
		switch(j){
		case 0:
			searchOkno.setPridejVinika();
			break;
		case 1:
			searchOkno.setPridejVadu();
			break;
		case 2:
			searchOkno.setVyhledejUpravVinika();
			break;
		case 3:
			searchOkno.setVyhledejUpravVadu();
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index6()");	
			break;
		}
	}
	
	/**
	 * Smazani fyzickych kusu
	 * @param j
	 */
	private void index7(int j){
		switch(j){
		case 0:
			searchOkno.setSmazFyzKusy();
			break;
		/*case 1:
			searchOkno.setVyhledejModel();
			break;
		case 2:
			searchOkno.setVyhledejZakazku();
			break;*/
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index7()");	
			break;
		}
	}
	
	private void index8(int j){
		switch(j){
		case 0:
			searchOkno.setZalohaObnovaDB(true);
			break;
		case 1:
			searchOkno.setZalohaObnovaDB(false);
			break;
		default: JOptionPane.showMessageDialog(hlavniOkno, "Nìco je špatnì v MainActionMenuItemListener, index7()");	
			break;
		}
	}
	
}
