package interfaces;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JYearChooser;

import app.MainFrame;
import app.PromOknoNovyZakaznikAndSearch;
import sablony.ParametryFiltr;
import sablony.errorwin.ExceptionWin;
import sablony.tabulka.ColorCellTable;
import sablony.tabulka.QueryTableModel;
import sablony.tabulka.TableColumnAdjuster;
import sqlstorage.SQLStor;
import storage.SkladOdkazu;
import tisk.FungujiciTabulka;

/**
 * Implementovane interface pro PanelFitr na hledani zakazek, zakazniku, modelu a fyzických kusù.
 * @author Havlicek
 *
 */
public class HledejListener implements ActionListener, MouseListener {
	private MainFrame hlavniOkno;
	private SkladOdkazu sklad;
	private SQLStor sql;
	
	private SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
	private CallableStatement cs = null;
	private ParametryFiltr filtr;
	private ColorCellTable table;
	private TableColumnAdjuster columAdjuster;
	/**
	 *  pole[0] = Label Jmeno Zakaznika <br>
	 *  pole[1] = TextFiled Jmeno Zakaznika  <br>
	 *  pole[2] = Label Cislo modelu  <br>
	 *  pole[3] = JtextField Cislo modelu  <br>
	 *  pole[4] = Label Nazev modelu  <br>
	 *  pole[5] = JtextField Nazev modelu  <br>
	 *  pole[6] = Label id modelu  <br>
	 *  pole[7] = JtextField id modelu  <br>
	 *  pole[8] = Label datum pøijetí zakázky  <br>
	 *  pole[9] = DateChooser datum pøijeti zakazky  <br>
	 *  pole[10] = Label Formovna  <br>
	 *  pole[11] = ComboBox formovna  <br>
	 *  pole[12] = Label id Zakazky  <br>
	 *  pole[13] = JtextField id Zakazky  <br>
	 *  pole[14] = Label Èíslo objednávky  <br>
	 *  pole[15] = JtextField Èíslo objednávky  <br>
	 *  pole[16] = checkVcetneUzavZak
	 */
	private Component [] pole;
	/**
	 *  vypisy[0] = Label Datum od <br>
	 *  vypisy[1] = JDateChooser Datum od  <br>
	 *  vypisy[2] = Label Datum do  <br>
	 *  vypisy[3] = JDateChooser Datum do  <br>
	 *  vypisy[4] = Label Cislo tydne  <br>
	 *  vypisy[5] = JtextField Cislo tydne  <br>
	 *  vypisy[6] = yearChooser <br>
	 *  vypisy[7] = JtextField napoveda datum <br>
	 *  vypisy[8] = JtextField napoveda cislo tydne <br>
	 *  vypisy[9] = JButton Prevod do PDF  <br>
	 *  
	 */
	private Component [] vypisy;
	private JButton vyhledej;
	private Color [] barvy;
	private static final int vyskaNadTabulkou = 270;
	
	private static final String acesDenied = "execute command denied to user";
	
	public HledejListener(JButton but, ParametryFiltr filtr, ColorCellTable table, Component [] pole, Component [] vypisy, MainFrame hlavniOkno, TableColumnAdjuster columAdjuster){
		this.vyhledej = but;
		this.filtr = filtr;
		this.table = table;
		this.pole = pole;
		this.vypisy = vypisy;
		this.columAdjuster = columAdjuster;
		this.hlavniOkno = hlavniOkno;
		this.sklad = hlavniOkno.getSklad();
		this.barvy = sklad.getBarvy();
		this.sql = sklad.getSql();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println(arg0.getActionCommand());
		try {
			if (arg0.getActionCommand().equalsIgnoreCase("HledejZakazniky")) {
				this.hledejZakaznikyVyhledej();
			}
			else if (arg0.getActionCommand().equalsIgnoreCase("HledejModely")) {
				this.hledejModelyVyhledej();
			}
			else if (arg0.getActionCommand().equalsIgnoreCase("HledejZakazky")) {
				this.hledejZakazkyVyhledej();
			}
			else if (arg0.getActionCommand().equalsIgnoreCase("HledejFyzKusy")) {
				this.hledejFyzKusyVyhledej();
			}
			else if(arg0.getActionCommand().equalsIgnoreCase("HledejZmetky")){ 
				this.hledejZmetky();
			} 
			else if(arg0.getActionCommand().equalsIgnoreCase("HledejViniky")){
				this.hledejViniky();
			}
			else if(arg0.getActionCommand().equalsIgnoreCase("HledejVady")){
				this.hledejVady();
			} 
			else if(arg0.getActionCommand().equalsIgnoreCase("ZaklPlanLiti")){
				this.zaklLiciPlan();
			}
			else if(arg0.getActionCommand().equalsIgnoreCase("PlanovaniLiti")){
				this.liciPlan();
			} 
			else if(arg0.getActionCommand().equalsIgnoreCase("HledejKapacitniProcet")){
				this.kapPropocet();
			} else { //vypisy a tisk
				System.out.println("Impementuje se "+arg0.getActionCommand());
				vypisy(arg0.getActionCommand());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getLocalizedMessage() != null){
				if(e.getLocalizedMessage().startsWith(acesDenied)){
					JOptionPane.showMessageDialog(hlavniOkno, "Na tuto operaci nemáte pravomoce");
				}else {
					ExceptionWin.showExceptionMessage(e);
				}
			} else {
				ExceptionWin.showExceptionMessage(e);
			}
			
		}
		int width = 500;
		for(int i = 0; i < table.getColumnCount() - 1; i++){
			width += table.getColumnModel().getColumn(i).getWidth();
		}
		
		JPanel [] hoho = (sklad.getPromOkna());
		Dimension s = ((PromOknoNovyZakaznikAndSearch)hoho[0]).getPanelTabulka().getPreferredSize();
		s.height = s.height + vyskaNadTabulkou;
		s.width = width;
		this.hlavniOkno.getObalVedlejsihoOkna().setPreferredSize(s);
		
		sklad.getScrollPane().validate();
		
	}
	
	private int tyden = 0;
	private int rok = 0;

	/**
	 * Metoda pro vypisy, vezme action comand a zjisti kterej vypis ma udìlat a pak ho udìlá (button hledej),
	 *  action comand porovnává se sklad.getCommands()[4]
	 * @param actionComand
	 * @throws Exception
	 */
	public void vypisy(String actionComand) throws Exception{
		boolean isVypis = true;
		if(actionComand.startsWith("PDF")){
			isVypis = false;
		}
		String [] comands =  sklad.getCommands()[4];
		int i = 0; boolean exist = false;
		for(i = 0; i < comands.length; i++){
			if(isVypis){
				if(actionComand.equalsIgnoreCase(comands[i])){
					exist = true;
					break;
				} 
			} else {
				if(actionComand.equalsIgnoreCase("PDF"+comands[i])){
					exist = true;
					break;
				}
			}
		}
		if(exist){
			switch(i){
			case 0:
				this.vypisStavuNeuzavrenychZakazek(isVypis);
				break;
			case 1:
				this.denniVypisOdlitku(isVypis);
				break;
			case 2:
				this.vypisVycistenychKusuOdDo(isVypis);
				break;
			case 3:
				this.vypisMzdySlevacu(isVypis);
				break;
			case 4:
				this.vypisTiskOdlitkuVKgKc(isVypis);
				break;
			case 5:
				this.vypisOdlitychKusuOdDo(isVypis);
				break;
			case 6:
				this.vypisPolozekSOdhadHmot(isVypis);
				break;
			case 7:
				this.vypisDleTerminuExpedice(isVypis);
				break;
			case 8:
				this.vypisExpedovanychKusuOdDo(isVypis);
				break;
			case 9:
				this.vypisTiskZpozdeneVyroby(isVypis);
				break;
			case 10:
				this.inventuraRozpracVyroby(isVypis);
				break;
			case 11:
				this.vypisSkladuKeDnesnimuDni(isVypis);
				break;
			default: JOptionPane.showMessageDialog(hlavniOkno, "Špatný vypis Hledejlistener vypis()");
				break;
			}
			if(!isVypis){
				JOptionPane.showMessageDialog(hlavniOkno, "PDF vytvoøeno");
			}
		} 
		if(!isVypis && !exist){
			if (this.table.getModel().getColumnName(0).equalsIgnoreCase("Jmeno_zakaznika") && this.table.getModel().getColumnName(1).equalsIgnoreCase("Jmeno_modelu")) {
				if (actionComand.equalsIgnoreCase("PDFZaklPlan")) {
					FungujiciTabulka tab = new FungujiciTabulka(hlavniOkno);
					boolean pom = tab.createPdf("ZaklLiciPlan-Tyden_"+tyden+"_rok_"+rok, this.table.getModel(), FungujiciTabulka.zaklLitiPlan);
					if(pom)JOptionPane.showMessageDialog(hlavniOkno, "Uloženo jako PDF");
				} else if (actionComand.equalsIgnoreCase("PDFPlanovani")) {
					if(!this.table.getModel().getColumnName(9).equalsIgnoreCase("Material")){
						JOptionPane.showMessageDialog(hlavniOkno, "Špatná tabuka");
						return;
					}
					FungujiciTabulka tab = new FungujiciTabulka(hlavniOkno);
					boolean pom = tab.createPdf("LiciPlan-Tyden_"+tyden+"_rok_"+rok, this.table.getModel(), FungujiciTabulka.liciPlan);
					if(pom)JOptionPane.showMessageDialog(hlavniOkno, "Uloženo jako PDF");
				}
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatný (neimplementovaný) tisk, Hledejlistener 240 ");
			}
		}
		
	}
	
	private void kapPropocet() throws Exception{
		int[] poleCi = getWeekAndYear();
		if (poleCi != null) {
			this.tyden = poleCi[0];
			this.rok = poleCi[1];
			char formovna = ((String)((JComboBox)vypisy[11]).getSelectedItem()).charAt(0);
			ResultSet rs = sql.kapacitniPropocet(tyden, rok, formovna);
			if (rs != null) {
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		}
	}
	
	private void zaklLiciPlan() throws Exception{
		int[] poleCi = getWeekAndYear();
		if (poleCi != null) {
			this.tyden = poleCi[0];
			this.rok = poleCi[1];
			ResultSet rs = sql.liciPlanZakl(poleCi[0], poleCi[1]);
			if (rs != null) {
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		}
	}
	
	private void liciPlan() throws Exception{
		int[] poleCi = getWeekAndYear();
		if (poleCi != null) {
			this.tyden = poleCi[0];
			this.rok = poleCi[1];
			ResultSet rs = sql.liciPlanovaci(poleCi[0], poleCi[1]);
			if (rs != null) {
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		}
	}
	
	private void vypisStavuNeuzavrenychZakazek(boolean isVypis) throws Exception{
		if(isVypis){
			int idModelu = 0, idZakazky = 0;
			String idModeliString = ((JTextField) pole[7]).getText();
			String idZakazkyString = ((JTextField) pole[13]).getText();
			if(idModeliString.equalsIgnoreCase(""))idModeliString = "0";
			if(idZakazkyString.equalsIgnoreCase(""))idZakazkyString = "0";
			
			try {
				idModelu = Integer.parseInt(idModeliString);
				idZakazky = Integer.parseInt(idZakazkyString);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatnì napsané Id modelu nebo Id zakázky");
				return;
			}
			ResultSet rs =sql.vypisStavuNeuzavrenychZakazek(
					 idZakazky,
					((JTextField) pole[1]).getText(),
					((JTextField) pole[3]).getText(),
					((JTextField) pole[5]).getText(),
					idModelu,
					((JDateChooser) pole[9]).getDate(),
					((JTextField) pole[15]).getText());
			if(rs == null){
				JOptionPane.showMessageDialog(hlavniOkno, "Nenalezeny žádné zakázky");
				return;
			}
			QueryTableModel tm = new QueryTableModel(rs);
			table.setModel(tm);
			columAdjuster.adjustColumns();
			rs.close();

		} /*else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Odlitky_KgKc_" + sdt.format(pom), table.getModel(), FungujiciTabulka.vypisOdlitkuVKgKc);
		}*/
	}
	private void denniVypisOdlitku(boolean isVypis) throws Exception{
		if(isVypis){
			Date datum = get1Date();
			//ResultSet rs = sql.denniVypisOdlitku(datum);
			Statement st = sql.denniVypisOdlitku(datum);
			if(st != null){
				QueryTableModel tm = new QueryTableModel(st);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				//st.close(); // nesmim zavrit statement kvuli optimalizaci
			}
		}
	}
	/**
	 * 
	 * @param isVypis jestli to je vypis nebo planovani
	 * @throws Exception
	 */
	private void vypisTiskOdlitkuVKgKc(boolean isVypis) throws Exception{
		if(isVypis){
			Date od = get1Date(), do_ = get2Date();
			ResultSet rs = sql.vypisOdlitkuVKgKc(od, do_);
			if(rs != null){
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		}else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Odlitky_KgKc_" + sdt.format(pom), table.getModel(), FungujiciTabulka.vypisOdlitkuVKgKc);
		}
		
	}
	private void vypisTiskZpozdeneVyroby(boolean isVypis) throws Exception{
		if(isVypis){
			Date pomDate = get1Date();
			ResultSet rs = sql.vypisZpozdeniVyroby(pomDate);
			if(rs != null){
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		} else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Zpozdena_vyroba_" + sdt.format(pom), table.getModel(), FungujiciTabulka.zpozdeniVyroby);
		}
	}
	
	private void vypisDleTerminuExpedice(boolean isVypis) throws Exception{
		if(isVypis){
			int[] poleCi = getWeekAndYear();
			if (poleCi != null) {
				ResultSet rs = sql.vypisDleTerminuExpedice(poleCi[0], poleCi[1]);
				if (rs != null) {
					QueryTableModel tm = new QueryTableModel(rs);
					table.setModel(tm);
					columAdjuster.adjustColumns();
					rs.close();
				}
			}
		} else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Terminy_expedice_" + sdt.format(pom), table.getModel(), FungujiciTabulka.vypisSTerminExp);
		}
		
	}
	private void vypisPolozekSOdhadHmot(boolean isVypis) throws Exception{
		if (isVypis) {
			ResultSet rs = sql.vypisPolozekSOdhadHmot();
			if (rs != null) {
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		} else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Polozky_s_odhad_hmot_" + sdt.format(pom), table.getModel(), FungujiciTabulka.vypisOdhadHmot);
		}
	}
	private void vypisMzdySlevacu(boolean isVypis) throws Exception{
		if (isVypis) {
			Date datum = get1Date();
			Statement st = sql.vypisMzdySlevacu(datum);
			if (st != null) {
				QueryTableModel tm = new QueryTableModel(st);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				//st.close(); kvuli optimalizaci
			}
		} else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Mzdy_slevacu_" + sdt.format(pom), table.getModel(), FungujiciTabulka.mezdySlevacu);
		}		
	}
	private void vypisOdlitychKusuOdDo(boolean isVypis) throws Exception{
		if (isVypis) {
			Date od = get1Date(), do_ = get2Date();
			ResultSet rs = sql.vypisOdlitychKusuOdDo(od, do_);
			if (rs != null) {
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		} else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Odlitky_od_do_" + sdt.format(pom), table.getModel(), FungujiciTabulka.vypisOdlitkuOdDo);
		}
		
	}
	private void vypisVycistenychKusuOdDo(boolean isVypis) throws Exception{  // vypis vycistených kusu za období
		if (isVypis) {
			Date od = get1Date(), do_ = get2Date();
			ResultSet rs = sql.vypisVycistenychKusuOdDo(od, do_);
			if (rs != null) {
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		} else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Vycisteno_od_do_" + sdt.format(pom), table.getModel(), FungujiciTabulka.vypisVycistenychOdDo);
		}
	}
	private void inventuraRozpracVyroby(boolean isVypis) throws Exception{
		if (isVypis) {
			ResultSet rs = sql.vypisRozpracovaneVyroby();
			if (rs != null) {
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		} else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Inventura_vyroby_" + sdt.format(pom), table.getModel(), FungujiciTabulka.inventuraVyroby);
		}
		
	}

	private void vypisExpedovanychKusuOdDo(boolean isVypis) throws Exception{
		if(isVypis){
			Date od = get1Date(), do_ = get2Date();
			ResultSet rs = sql.vypisExpedovanychKusuOdDo(od,do_);
			if(rs != null){
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		}else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Expedovane_kusyOdDo_" + sdt.format(pom), table.getModel(), FungujiciTabulka.vypisExpediceOdDo);
		}
	}
	
	private void vypisSkladuKeDnesnimuDni(boolean isVypis) throws Exception{
		if(isVypis){
			ResultSet rs = sql.vypisSkladuKDnesnimuDni();
			if(rs != null){
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		} else {
			FungujiciTabulka pdf = new FungujiciTabulka(hlavniOkno);
			Date pom = new Date();
			pdf.createPdfTable("Vypis_skladu_" + sdt.format(pom), table.getModel(), FungujiciTabulka.vypisSkladu);		
		}
		
	}
	
	private Date get1Date() throws Exception{
		Date od = ((JDateChooser)vypisy[1]).getDate();
		return od;
	}
	
	private Date get2Date() throws Exception{
		Date do_ = ((JDateChooser)vypisy[3]).getDate();
		return do_;
	}
	
	/**
	 * 
	 * @return pole[0] = WeekOfTheYear <br>
	 * pole[1] = Year <br>
	 * null pokud chyba
	 */
	private int [] getWeekAndYear() throws Exception{
		try {
			int[] pole = new int[2];
			pole[0] = Integer.parseInt(((JTextField) vypisy[5]).getText());
			pole[1] = ((JYearChooser) vypisy[6]).getYear();
			return pole;
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(hlavniOkno,
					"Špatnì zapsané èíslo u èísla týdne");
			return null;
		}
	}
	
	private void hledejZakaznikyVyhledej() throws Exception {
		String jmeno = ((JTextField) pole[1]).getText();
		if (jmeno.contentEquals(" ")) {
			JOptionPane.showMessageDialog(hlavniOkno,
					"Ve vyhled\u00E1van\u00E9m jm\u00E9n\u011B je mezera");
			return;
		}
		ResultSet rs = sql.vyberZakazniky(jmeno);
		if(rs == null)return;
		
		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();
	}
	
	private void hledejModelyVyhledej() throws Exception{
		int idModelu = 0, idZakazky = 0;
		String idModeliString = ((JTextField) pole[7]).getText();
		String idZakazkyString = ((JTextField) pole[13]).getText();
		if(idModeliString.equalsIgnoreCase(""))idModeliString = "0";
		if(idZakazkyString.equalsIgnoreCase(""))idZakazkyString = "0";
		
		try {
			idModelu = Integer.parseInt(idModeliString);
			idZakazky = Integer.parseInt(idZakazkyString);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(hlavniOkno,
					"Špatnì napsané Id modelu nebo Id zakázky");
			return;
		}
		ResultSet rs = sql.vyberModely(((JTextField) pole[1]).getText(),
				((JTextField) pole[3]).getText(),
				((JTextField) pole[5]).getText(),
				idModelu,
				((JDateChooser) pole[9]).getDate(),
				(String) ((JComboBox) pole[11]).getSelectedItem(),
				idZakazky,
				((JCheckBox) pole[16]).isSelected());
		if(rs == null)return;
		
		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();
	}
	
	private void hledejZakazkyVyhledej() throws Exception{
		int idModelu = 0, idZakazky = 0;
		String idModeliString = ((JTextField) pole[7]).getText();
		String idZakazkyString = ((JTextField) pole[13]).getText();
		if(idModeliString.equalsIgnoreCase(""))idModeliString = "0";
		if(idZakazkyString.equalsIgnoreCase(""))idZakazkyString = "0";
		
		try {
			idModelu = Integer.parseInt(idModeliString);
			idZakazky = Integer.parseInt(idZakazkyString);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(hlavniOkno, "Špatnì napsané Id modelu nebo Id zakázky");
			return;
		}
		ResultSet rs = sql.vyberZakazky(idZakazky,
				((JTextField) pole[1]).getText(),
				((JTextField) pole[3]).getText(),
				((JTextField) pole[5]).getText(),
				idModelu,
				((JDateChooser) pole[9]).getDate(),
				((JTextField) pole[15]).getText(),
				((JCheckBox) pole[16]).isSelected());
		if(rs == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Nenalezeny žádné zakázky");
			return;
		}
		
		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();		
	}
	
	/**
	 * Ještì se musí dodìlat
	 * @throws Exception
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	
	private void hledejFyzKusyVyhledej() throws Exception, InterruptedException, ExecutionException{
		String idZakazkyString = ((JTextField) pole[13]).getText();
		int idZakazky = 0;
		try{
			idZakazky = Integer.parseInt(idZakazkyString);
		} catch(NumberFormatException e){
			JOptionPane.showMessageDialog(hlavniOkno, "Špatnì napsané Id zakázky");
			return;
		}
		ResultSet rs = sql.vyberFyzKusy(idZakazky).rs;

		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();
		
	}
	
	private void hledejZmetky() throws Exception, InterruptedException, ExecutionException{
		int idModelu = 0, idZakazky = 0;
		String idModeliString = ((JTextField) pole[7]).getText();
		String idZakazkyString = ((JTextField) pole[13]).getText();
		if(idModeliString.equalsIgnoreCase(""))idModeliString = "0";
		if(idZakazkyString.equalsIgnoreCase(""))idZakazkyString = "0";
		
		try {
			idModelu = Integer.parseInt(idModeliString);
			idZakazky = Integer.parseInt(idZakazkyString);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(hlavniOkno,
					"Špatnì napsané Id modelu nebo Id zakázky");
			return;
		}
		ResultSet rs = sql.vyberZmetky(idZakazky,
				((JTextField) pole[1]).getText(),
				((JTextField) pole[3]).getText(),
				((JTextField) pole[5]).getText(),
				idModelu,
				((JDateChooser) pole[9]).getDate(),
				((JTextField) pole[15]).getText(),
				((JCheckBox) pole[16]).isSelected());
		if(rs == null){
			JOptionPane.showMessageDialog(hlavniOkno,
					"Nenalezeny žádné zakázky");
			return;
		}
		
		
		ResultSet [] pole = new ResultSet[2];
		pole[0] = rs;
		pole[1] = rs;

		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();		
		
	}
	
	private void hledejViniky() throws SQLException{
		String jmenoVady = ((JTextField) pole[1]).getText();
		if (jmenoVady.contentEquals(" ")) {
			JOptionPane.showMessageDialog(hlavniOkno,
					"Ve vyhled\u00E1van\u00E9m jm\u00E9n\u011B je mezera");
			return;
		}
		ResultSet rs = sql.vyberViniky(jmenoVady);
		if(rs == null)return;
		
		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();
		
	}
	
	private void hledejVady() throws SQLException{
		String jmenoVady = ((JTextField) pole[1]).getText();
		if (jmenoVady.contentEquals(" ")) {
			JOptionPane.showMessageDialog(hlavniOkno,
					"Ve vyhled\u00E1van\u00E9m jm\u00E9n\u011B je mezera");
			return;
		}
		ResultSet rs = sql.vyberVady(jmenoVady);
		if(rs == null)return;
		
		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();
	}
	
	public void onButton(){
		vyhledej.setBackground(barvy[14]);
	}
	
	public void offButton(){
		vyhledej.setBackground(barvy[13]);
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		onButton();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		offButton();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {		
	}

}
