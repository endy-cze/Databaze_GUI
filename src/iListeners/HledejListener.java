package iListeners;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.CallableStatement;
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
import javax.swing.table.TableModel;

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
import tiskExcel.PlanovaniLitiToExcelTisk;
import tiskExcel.TableToExcel;

/**
 * Implementovane interface pro PanelFitr na hledani zakazek, zakazniku, modelu a fyzických kusù.
 * Action listener pto tisk.
 * @author Havlicek
 *
 */
public class HledejListener implements ActionListener, MouseListener {
	private MainFrame hlavniOkno;
	private SkladOdkazu sklad;
	private SQLStor sql;
	
	private SimpleDateFormat sdf = null;
	private ParametryFiltr filtr;
	private ColorCellTable table;
	private TableColumnAdjuster columAdjuster;

	private Color [] barvy;
	private static final int vyskaNadTabulkou = 270;
	
	private Date lastUsedDate1 = null;
	private Date lastUsedDate2 = null;
	private int [] lastUsedWeekNumberAYear = {-1,-1};
	private String  lastUsedFormovna = null;
	/**
	 * Slouží pro metody, které nemaji jako parametr datum, ale aby bylo jasné kdy to bylo vyhledané,
	 * tak se pøidá datum do nadpisu, kdy byla tato metoda volána.
	 */
	private Date datumPoslVolaniMetody = null;
	private static final String acesDenied = "execute command denied to user";
	/**
	 * ActionComandy z ParametryFiltr
	 */
	private String [] actionComands;
	
	public HledejListener(ParametryFiltr filtr, ColorCellTable table, MainFrame hlavniOkno, 
			TableColumnAdjuster columAdjuster, String [] actionComands){
		this.filtr = filtr;
		this.table = table;
		this.columAdjuster = columAdjuster;
		this.hlavniOkno = hlavniOkno;
		this.sklad = hlavniOkno.getSklad();
		this.barvy = sklad.getBarvy();
		this.sql = sklad.getSql();
		this.sdf = sklad.getSdf();
		this.actionComands = actionComands;
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
				this.zaklLiciPlan(true, -1);
			}
			else if(arg0.getActionCommand().equalsIgnoreCase("PlanovaniLiti")){
				this.liciPlan(true, -1);
			} 
			else if(arg0.getActionCommand().equalsIgnoreCase("HledejKapacitniProcet")){
				this.kapPropocet();
			}
			else if(arg0.getActionCommand().equalsIgnoreCase("PlanExpedice")){
				this.planExpedice(false);
			}
			else if(arg0.getActionCommand().equalsIgnoreCase("PDFPlanExpedice")){
				this.planExpedice(true);
			} else { //vypisy a tisk
				vypisyAndTisk(arg0.getActionCommand());
			}
		} catch (Exception e) {
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
	public void vypisyAndTisk(String actionComand) throws Exception{
		boolean isVypis = true;
		if(actionComand.startsWith("PDF")){
			isVypis = false;
		}
		boolean isEmpty = this.table.getColumnName(0).equalsIgnoreCase("Prázdná tabulka");
		if(!isVypis && isEmpty){
			JOptionPane.showMessageDialog(hlavniOkno, "Tabulka je prázdná");
			return;
		}
		// String [] comands =  sklad.getCommands()[4];
		String [] comands =  actionComands;
		
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
			case ParametryFiltr.VypisStavNeuzavrenychZakazek:
				this.vypisStavuNeuzavrenychZakazek(isVypis, i);
				break;
			case ParametryFiltr.DenniVypisOdlitychKusu:
				this.denniVypisOdlitku(isVypis, i);
				break;
			case ParametryFiltr.VypisVycistenychKusuZaObdobi:
				this.vypisVycistenychKusuOdDo(isVypis, i);
				break;
			case ParametryFiltr.MzdySlevacu:
				this.vypisMzdySlevacu(isVypis, i);
				break;
			case ParametryFiltr.VypisOdlitkuVKgKc:
				this.vypisTiskOdlitkuVKgKc(isVypis, i);
				break;
			case ParametryFiltr.VypisOdlitychKusuOdDo:
				this.vypisOdlitychKusuOdDo(isVypis, i);
				break;
			case ParametryFiltr.VypisPolozekSOdhadHmot:
				this.vypisPolozekSOdhadHmot(isVypis, i);
				break;
			case ParametryFiltr.VypisDleTerminuExpedice:
				this.vypisDleTerminuExpedice(isVypis, i);
				break;
			case ParametryFiltr.VypisExpedice_od_do:
				this.vypisExpedovanychKusuOdDo(isVypis, i);
				break;
			case ParametryFiltr.VypisZpozdeneVyroby:
				this.vypisTiskZpozdeneVyroby(isVypis, i);
				break;
			case ParametryFiltr.InventuraRozpracVyroby:
				this.inventuraRozpracVyroby(isVypis, i);
				break;
			case ParametryFiltr.VypisSkladuKeDnesnimuDni:
				this.vypisSkladuKeDnesnimuDni(isVypis, i);
				break;
			case ParametryFiltr.VypisZmetkuZaObdobi:
				this.vypisZmetkyMzdy(isVypis, i);
				break;
			case ParametryFiltr.VypisVinikuVKgKcMzdy:
				this.vypisVinikyVKgKc(isVypis, i);
				break;
			case ParametryFiltr.ZaklPlanLiti:
				zaklLiciPlan(isVypis, i);
				break;
			case ParametryFiltr.PlanovaniLiti:
				liciPlan(isVypis, i);
				break;
			default: JOptionPane.showMessageDialog(hlavniOkno, "Špatný vypis Hledejlistener vypis()");
				break;
			}
		} 		
	}
	
	private void kapPropocet() throws Exception{
		int[] poleCi = getWeekAndYear();
		if (poleCi != null) {
			this.tyden = poleCi[0];
			this.rok = poleCi[1];
			//char formovna = ((String)((JComboBox)vypisy[11]).getSelectedItem()).charAt(0);
			String formovna = filtr.getFormovna2Vypisy();
			ResultSet rs = sql.kapacitniPropocet(tyden, rok, formovna);
			if (rs != null) {
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		}
	}
	
	private void zaklLiciPlan(boolean isVypis, int cisloExportu) throws Exception{
		if (isVypis) {
			int[] poleCi = getWeekAndYear();
			String formovna = this.getFormovna();
			if (poleCi != null) {
				this.tyden = poleCi[0];
				this.rok = poleCi[1];
				Statement st = sql.liciPlanZakl(poleCi[0], poleCi[1], formovna);
				if (st != null) {
					QueryTableModel tm = new QueryTableModel(st);
					table.setModel(tm);
					columAdjuster.adjustColumns();
				}
			}
		} else {
			TableModel mod = table.getModel();
			String extend = lastUsedWeekNumberAYear[0] +" v roce "+  lastUsedWeekNumberAYear[1] + ", " + this.lastUsedFormovna;
			TableToExcel.exportToExcelNaSirku(hlavniOkno, mod, extend, "Zakl_lici_plan", cisloExportu);
		}
	}
	
	private void liciPlan(boolean isVypis, int cisloExportu) throws Exception{
		if (isVypis) {
			int[] poleCi = getWeekAndYear();
			String formovna = this.getFormovna();
			if (poleCi != null) {
				this.tyden = poleCi[0];
				this.rok = poleCi[1];
				Statement st = sql.liciPlanovaci(poleCi[0], poleCi[1], formovna);
				if (st != null) {
					QueryTableModel tm = new QueryTableModel(st);
					table.setModel(tm);
					columAdjuster.adjustColumns();
				}
			}
		} else {
			TableModel mod = table.getModel();
			String extend = lastUsedWeekNumberAYear[0] +" v roce "+  lastUsedWeekNumberAYear[1] + ", " + this.lastUsedFormovna;
			
			//TableToExcel.exportToExcelNaSirku(hlavniOkno, mod, extend, "Planovaci_lici_plan", cisloExportu);
			PlanovaniLitiToExcelTisk.exportPlanovaniLitiToExcel(hlavniOkno, mod, extend, "Planovaci_lici_plan", sdf);
		}
	}
	
	private void planExpedice(boolean isTisk) throws Exception{
		if (!isTisk) {
			Statement st = sql.planExpedice();
			if (st != null) {
				QueryTableModel tm = new QueryTableModel(st);
				table.setModel(tm);
				columAdjuster.adjustColumns();
			}
			//datum kdy byla metoda volana, kvuli datumu v tisku
			datumPoslVolaniMetody = new Date();
		} else {
			TableModel model = table.getModel();
			String extend = this.sdf.format(datumPoslVolaniMetody);
			TableToExcel.exportToExcelNaSirku(hlavniOkno, model, extend, "Plan_expedice", ParametryFiltr.PlanExpedice);
		}
	}
	
	private void vypisStavuNeuzavrenychZakazek(boolean isVypis,int cisloVypisu) throws Exception{
		//vypis
		if(isVypis){
			int idModelu = 0, idZakazky = 0;
			String idModeliString = filtr.getIDModelu();
			String idZakazkyString = filtr.getIDZakazky();
			if(idModeliString.equalsIgnoreCase(""))idModeliString = "0";
			if(idZakazkyString.equalsIgnoreCase(""))idZakazkyString = "0";
			//datum kdy byla metoda volana, kvuli datumu v tisku
			datumPoslVolaniMetody = new Date();
			
			try {
				idModelu = Integer.parseInt(idModeliString);
				idZakazky = Integer.parseInt(idZakazkyString);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(hlavniOkno, "Špatnì napsané Id modelu nebo Id zakázky");
				return;
			}
		
			ResultSet rs =sql.vypisStavuNeuzavrenychZakazek(
					idZakazky,
					filtr.getJmenoZakaznikaOrVadyOrVinika(),
					filtr.getCisloModelu(),
					filtr.getNazevModelu(),
					idModelu,
					filtr.getDatumZakazky(),
					filtr.getCisloObjednavky());
			if(rs == null){
				JOptionPane.showMessageDialog(hlavniOkno, "Nenalezeny žádné zakázky");
				return;
			}
			QueryTableModel tm = new QueryTableModel(rs);
			table.setModel(tm);
			columAdjuster.adjustColumns();
			rs.close();
		}
		//export do Excelu
		else {
			TableModel mod = table.getModel();
			TableToExcel.exportToExcelNaVysku(hlavniOkno, mod, sdf.format(datumPoslVolaniMetody), (cisloVypisu+1)+". "+"Stav_neuzavrenych_zakazek", cisloVypisu);
		}
	}
	private void denniVypisOdlitku(boolean isVypis, int cisloVypisu) throws Exception{
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
		} else {
			TableModel mod = table.getModel();
			TableToExcel.exportToExcelNaVysku(hlavniOkno, mod, sdf.format(lastUsedDate1), (cisloVypisu+1)+". "+"Vypis_odlitku_ke_dni", cisloVypisu);
		}
	}
	/**
	 * 
	 * @param isVypis jestli to je vypis nebo planovani
	 * @throws Exception
	 */
	private void vypisTiskOdlitkuVKgKc(boolean isVypis, int cisloVypisu) throws Exception{
		if(isVypis){
			Date od = get1Date(), do_ = get2Date();
			ResultSet rs = sql.vypisOdlitkuVKgKc(od, do_);
			if(rs != null){
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		} else {
			TableModel mod = table.getModel();
			String datumy = sdf.format(lastUsedDate1)+" do "+ sdf.format(lastUsedDate2);
			TableToExcel.exportToExcelNaVysku(hlavniOkno, mod, datumy, (cisloVypisu+1)+". "+"Odlitky_kg_kc_za_obdobi", cisloVypisu);
		}
	}
	
	private void vypisTiskZpozdeneVyroby(boolean isVypis, int cisloVypisu) throws Exception{
		if(isVypis){
			Date pomDate = get1Date();
			ResultSet rs = sql.vypisZpozdeniVyroby(pomDate);
			if(rs != null){
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		}  else {
			TableModel mod = table.getModel();
			//TableToExcel.exportToExcel(hlavniOkno, mod, "Stav_neuzavrenych_zakazek", cisloVypisu);
		}
	}
	
	private void vypisDleTerminuExpedice(boolean isVypis, int cisloVypisu) throws Exception{
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
			TableModel mod = table.getModel();
			//TableToExcel.exportToExcel(hlavniOkno, mod, "Stav_neuzavrenych_zakazek", cisloVypisu);
		}
		
	}
	private void vypisPolozekSOdhadHmot(boolean isVypis, int cisloVypisu) throws Exception{
		if (isVypis) {
			ResultSet rs = sql.vypisPolozekSOdhadHmot();
			datumPoslVolaniMetody = new Date();
			if (rs != null) {
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		} else {
			TableModel mod = table.getModel();
			TableToExcel.exportToExcelNaVysku(hlavniOkno, mod, sdf.format(datumPoslVolaniMetody), (cisloVypisu+1)+". "+"Polozky_s_odhadovou_hmotnosti", cisloVypisu);
		}
	}
	
	private void vypisMzdySlevacu(boolean isVypis, int cisloVypisu) throws Exception{
		if (isVypis) {
			Date datumOd = get1Date();
			Date datumDo = get2Date();
			Statement st = sql.vypisMzdySlevacu(datumOd, datumDo);
			if (st != null) {
				QueryTableModel tm = new QueryTableModel(st);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				//st.close(); kvuli optimalizaci
			}
		} else {
			TableModel mod = table.getModel();
			TableToExcel.exportToExcelNaVysku(hlavniOkno, mod, sdf.format(lastUsedDate1), (cisloVypisu+1)+". "+"Mzdy_slevacu", cisloVypisu);
		}	
	}
	
	/**
	 * Nedodìláno !!! chybí mi vlastní materiál.
	 * @param isVypis
	 * @param cisloVypisu
	 * @throws Exception
	 */
	private void vypisOdlitychKusuOdDo(boolean isVypis, int cisloVypisu) throws Exception{
		if (isVypis) {
			Date od = get1Date(), do_ = get2Date();
			String formovna = getFormovna();
			//String vlastniMaterialy = getVlastniMaterial();
			String [] seznamVlastniMaterialy = this.getVlastniMaterialy();
			//Statement st = sql.vypisOdlitychKusuOdDo(od, do_, formovna, vlastniMaterialy);
			Statement st = sql.vypisOdlitychKusuOdDoRegEx(od, do_, formovna, seznamVlastniMaterialy);
			if (st != null) {
				QueryTableModel tm = new QueryTableModel(st);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				//st.close();
			}
		} else {
			TableModel mod = table.getModel();
			String datumy = sdf.format(lastUsedDate1)+" do "+ sdf.format(lastUsedDate2);
			TableToExcel.exportToExcelNaSirku(hlavniOkno, mod, datumy, (cisloVypisu+1)+". "+"Odlite_kusy_za_obdobi", cisloVypisu);
		}
		
	}
	private void vypisVycistenychKusuOdDo(boolean isVypis, int cisloVypisu) throws Exception{  // vypis vycistených kusu za období
		if (isVypis) {
			Date od = get1Date(), do_ = get2Date();
			Statement st = sql.vypisVycistenychKusuOdDo(od, do_);
			if (st != null) {
				QueryTableModel tm = new QueryTableModel(st);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				//st.close();
			}
		}  else {
			TableModel mod = table.getModel();
			String datumy = sdf.format(lastUsedDate1)+" do "+ sdf.format(lastUsedDate2);
			TableToExcel.exportToExcelNaSirku(hlavniOkno, mod, datumy, (cisloVypisu+1)+". "+"Vycistene_kusy_za_obdobi", cisloVypisu);
		}
	}
	private void inventuraRozpracVyroby(boolean isVypis, int cisloVypisu) throws Exception{
		if (isVypis) {
			Statement st = sql.vypisRozpracovaneVyroby();
			datumPoslVolaniMetody = new Date();
			if (st != null) {
				QueryTableModel tm = new QueryTableModel(st);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				//st.close();
			}
		} else {
			TableModel mod = table.getModel();
			TableToExcel.exportToExcelNaVysku(hlavniOkno, mod, sdf.format(datumPoslVolaniMetody), (cisloVypisu+1)+". "+"Inventura_rozpracovane_vyroby", cisloVypisu);
		}
	}

	private void vypisExpedovanychKusuOdDo(boolean isVypis, int cisloVypisu) throws Exception{
		if(isVypis){
			Date od = get1Date(), do_ = get2Date();
			ResultSet rs = sql.vypisExpedovanychKusuOdDo(od,do_);
			if(rs != null){
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		} else {
			TableModel mod = table.getModel();
			String datumy = sdf.format(lastUsedDate1)+" do "+ sdf.format(lastUsedDate2);
			TableToExcel.exportToExcelNaSirku(hlavniOkno, mod, datumy, (cisloVypisu+1)+". "+"Expedovane_kusy_za_obdobi", cisloVypisu);
		}
	}
	
	private void vypisSkladuKeDnesnimuDni(boolean isVypis, int cisloVypisu) throws Exception{
		if(isVypis){
			Statement st = sql.vypisSkladuKDnesnimuDni();
			datumPoslVolaniMetody = new Date();
			if(st != null){
				QueryTableModel tm = new QueryTableModel(st);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				// st.close();
			}
		}  else {
			TableModel mod = table.getModel();
			TableToExcel.exportToExcelNaSirku(hlavniOkno, mod, sdf.format(datumPoslVolaniMetody),(cisloVypisu+1)+". "+ "Vypis_skladu", cisloVypisu);
		}
	}
	
	private void vypisZmetkyMzdy(boolean isVypis, int cisloVypisu) throws Exception{
		if(isVypis){
			Date od = get1Date(), do_ = get2Date();
			ResultSet rs = sql.vypisZmetky(od, do_);
			if(rs != null){
				QueryTableModel tm = new QueryTableModel(rs);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				rs.close();
			}
		}  else {
			QueryTableModel mod = (QueryTableModel) table.getModel();
			String datumy = sdf.format(lastUsedDate1)+" do "+ sdf.format(lastUsedDate2);
			TableToExcel.vypisZmetkuZmdyToExcel(mod, hlavniOkno, datumy, (cisloVypisu+1)+". "+"Vypis_zmetky_mzdy");
		}
	}
	
	private void vypisVinikyVKgKc(boolean isVypis, int cisloVypisu) throws Exception{
		if(isVypis){
			Date od = get1Date(), do_ = get2Date();
			Statement st =sql.vypisVinikyVKgKc(od, do_);
			if (st != null) {
				QueryTableModel tm = new QueryTableModel(st);
				table.setModel(tm);
				columAdjuster.adjustColumns();
				//st.close(); kvuli optimalizaci
			}
		}  else {
			TableModel model = table.getModel();
			String datumy = sdf.format(lastUsedDate1)+" do "+ sdf.format(lastUsedDate2);
			TableToExcel.exportToExcelNaSirku(hlavniOkno, model, datumy, (cisloVypisu+1)+". "+"Vypis_viniku_v_kg-kc", cisloVypisu);
		}
	}
	
	private Date get1Date() throws Exception{
		Date od = filtr.getOdDate();
		lastUsedDate1 = od;
		return od;
	}
	
	private Date get2Date() throws Exception{
		Date do_ = filtr.getDoDate();
		lastUsedDate2 = do_;
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
			pole[0] = filtr.getCisloTydne();
			pole[1] = filtr.getRok();
			lastUsedWeekNumberAYear[0] = pole[0];
			lastUsedWeekNumberAYear[1] = pole[1];
			return pole;
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(hlavniOkno, "Špatnì zapsané èíslo u èísla týdne");
			return null;
		}
	}
	
	private String getFormovna() throws Exception{
		//String formovna = (String) ((JComboBox) vypisy[indexComboBoxFormovna]).getSelectedItem();
		String formovna = filtr.getFormovna2Vypisy();
		if(formovna.equalsIgnoreCase("T")){
			this.lastUsedFormovna = "Tìžká formovna";
		} else if(formovna.equalsIgnoreCase("S")){
			this.lastUsedFormovna = "Støední formovna";
		} else if(formovna.equalsIgnoreCase("M")){
			this.lastUsedFormovna = "Malá formovna";
		} else if(formovna.equalsIgnoreCase("")){
			this.lastUsedFormovna = "Všechny formovny";
		} else {
			this.lastUsedFormovna = "chyba";
		}
		return formovna;
	}
	
	/*private String getVlastniMaterial(){
		return filtr.getSelectedVlastniMaterial();
	}*/
	private String [] getVlastniMaterialy(){
		return filtr.getSelectedVlMaterials();
	}
	
	private void hledejZakaznikyVyhledej() throws Exception {
		String jmeno = filtr.getJmenoZakaznikaOrVadyOrVinika();
		ResultSet rs = sql.vyberZakazniky(jmeno);
		if(rs == null)return;

		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();
	}
	
	private void hledejModelyVyhledej() throws Exception{
		int idModelu = 0, idZakazky = 0;
		String idModeliString = filtr.getIDModelu();
		String idZakazkyString = filtr.getIDZakazky();
		if(idModeliString.equalsIgnoreCase(""))idModeliString = "0";
		if(idZakazkyString.equalsIgnoreCase(""))idZakazkyString = "0";
		
		try {
			idModelu = Integer.parseInt(idModeliString);
			idZakazky = Integer.parseInt(idZakazkyString);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(hlavniOkno, "Špatnì napsané Id modelu nebo Id zakázky");
			return;
		}
		ResultSet rs = sql.vyberModely(
				 filtr.getJmenoZakaznikaOrVadyOrVinika(),
				 filtr.getCisloModelu(),
				 filtr.getNazevModelu(),
				idModelu,
				filtr.getDatumZakazky(),
				filtr.getFormovna1Pole(),
				idZakazky,
				filtr.isSelectedVcetneUzavreneZakazky());
		if(rs == null)return;
		
		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();
	}
	
	private void hledejZakazkyVyhledej() throws Exception{
		int idModelu = 0, idZakazky = 0;
		String idModeliString = filtr.getIDModelu();
		String idZakazkyString = filtr.getIDZakazky();
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
				filtr.getJmenoZakaznikaOrVadyOrVinika(),
				filtr.getCisloModelu(),
				filtr.getNazevModelu(),
				idModelu,
				filtr.getDatumZakazky(),
				filtr.getCisloObjednavky(),
				filtr.isSelectedVcetneUzavreneZakazky());
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
		String idZakazkyString = filtr.getIDZakazky();
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
		String idModeliString = filtr.getIDModelu();
		String idZakazkyString = filtr.getIDZakazky();
		if(idModeliString.equalsIgnoreCase(""))idModeliString = "0";
		if(idZakazkyString.equalsIgnoreCase(""))idZakazkyString = "0";
		
		try {
			idModelu = Integer.parseInt(idModeliString);
			idZakazky = Integer.parseInt(idZakazkyString);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(hlavniOkno, "Špatnì napsané Id modelu nebo Id zakázky");
			return;
		}
		ResultSet rs = sql.vyberZmetky(idZakazky,
				filtr.getJmenoZakaznikaOrVadyOrVinika(),
				filtr.getCisloModelu(),
				filtr.getNazevModelu(),
				idModelu,
				filtr.getDatumZakazky(),
				filtr.getCisloObjednavky(),
				filtr.isSelectedVcetneUzavreneZakazky());
		if(rs == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Nenalezeny žádné zakázky");
			return;
		}
		
		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();		
	}
	
	private void hledejViniky() throws SQLException{
		String jmenoVady = filtr.getJmenoZakaznikaOrVadyOrVinika();
		ResultSet rs = sql.vyberViniky(jmenoVady);
		if(rs == null)return;
		
		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();
		
	}
	
	private void hledejVady() throws SQLException{
		String jmenoVady = filtr.getJmenoZakaznikaOrVadyOrVinika();
		ResultSet rs = sql.vyberVady(jmenoVady);
		if(rs == null)return;
		
		QueryTableModel tm = new QueryTableModel(rs);
		table.setModel(tm);
		columAdjuster.adjustColumns();
	}
	
	public void onButton(JButton but){
		but.setBackground(barvy[14]);
	}
	
	public void offButton(JButton but){
		but.setBackground(barvy[13]);
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		Component com = arg0.getComponent();
		if(com instanceof JButton){
			onButton((JButton)com);
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		Component com = arg0.getComponent();
		if(com instanceof JButton){
			offButton((JButton)com);
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {		
	}

}
