package sqlstorage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import app.MainFrame;
import app.ProgresBarFrame;
import sablony.errorwin.ExceptionWin;
import sablony.storage.DateStor;
import storage.SkladOdkazu;
import thread.ScriptRunner;
/**
 * Objekt pro ukládání Connection a CallableStatements pro komunikaci s databází.
 * @author Havlicek
 *
 */
public class SQLStor {
	private Connection conn = null;
	private CallableStatement [][] cst = null;
	private Date [][] naposledyPouzito = null;
	private CallableStatement c;
	private ResultSet rs = null;
	private SkladOdkazu sklad;
	private MainFrame hlavniOkno;
	private static final int dobaNaZavrenyPripojeni = 1*60000;
	private static final int maxDelkaRetezce = 30;
	private static final int maxDelkaPaganyrky = 5;
	private static final int maxDelkaPoznamkyUZakazky = 45, maxDelkaVady = 45, maxDelkaPoznamkyUModelu = 50;
	private static final int maxDelkamaterialu = 25, maxDelkaVinika = 25;
	private static final int maxDelkaVlastnihoMaterialu = 10;
	private static final int maxPocetKusuNovaZakazka = 1500;
	private static final double maxCena = 1e9;
	public static final int maxDelkaCislaFaktury = 19;
	public static final int maxDelkaCislaTavby = 10;
	public static final int maxDelkaTeplotyLiti = 20;
	private ProgresBarFrame prgbar = new ProgresBarFrame();
	private final int POCET_TYDNU_VYTIZENI_KAPACIT = 10;
	
	/**
	 * Statusy pro smazani fyzickeho kusu
	 */
	public static final int USPECH = 1;
	public static final int SPATNEID = 2;
	public static final int SQLNULL = 0;
	
	/**
	 * Sql prikazy
	 */
	private final String [][] sqlPrikazy = {
			{"{CALL pomdb.novyZakaznik(?)}", "{CALL pomdb.novyModel(?,?,?,?,?,?,?,?,?)}", "{CALL pomdb.novaZakazka(?,?,?,?,?,?,?,?,?,?,?,?)}"},   // insert
			{"{CALL pomdb.vyberZakazniky(?)}", "{CALL pomdb.vyberModely(?,?,?,?,?,?,?,?)}", "{CALL pomdb.vyberZakazky2(?,?,?,?,?,?,?,?)}",
				"{CALL pomdb.vyberFyzKusy(?,?)}", "{CALL pomdb.vyberZmetky(?,?,?,?,?,?,?,?)}"},	//select
			{"{CALL pomdb.upravZakaznika(?,?)}", "{CALL pomdb.upravModel(?,?,?,?,?,?,?,?,?,?)}", "{CALL pomdb.upravZakazku(?,?,?,?,?,?,?,?,?,?,?,?,?)}"},  //update
			{"{CALL pomdb.zadej_cislo_faktury_cislo_tavby_prohlizeci(?,?,?)}", "{CALL pomdb.zadejPlanovanyDatumLiti(?,?)}", "{CALL pomdb.zadejOdlitek(?,?,?,?,?,?,?,?,?,?,?,?)}",
				"{CALL pomdb.zadejUdajeOZmetku(?,?,?,?)}", "{CALL pomdb.zadejDilciTerminy(?,?,?,?)}"}, // "{CALL pomdb.zadejDatumVycistenehoKusu(?,?,?)}"
			{"{CALL pomdb.pridejVinika(?)}", "{CALL pomdb.pridejVadu(?)}", "{CALL pomdb.planovaniRozvrh(?,?)}", "{CALL pomdb.generujKusy(?)}",
				"{CALL pomdb.planovaniRozvrhVycisteno(?,?)}", "{CALL pomdb.kapacitniPropocet(?,?)}", "{CALL pomdb.uzavriZakazku(?,?,?,?,?,?,?,?)}",
				"{CALL pomdb.obnovZakazku(?)}", "{CALL pomdb.uprav_vinika(?,?)}", "{CALL pomdb.uprav_vadu(?,?)}"},
			{"{CALL pomdb.vypisOdlituVKgKcOdDo(?,?)}", "{CALL pomdb.vypisZpozdeneVyroby(?)}", "{CALL pomdb.vypisDleTerminuExpediceCisloTydne(?,?)}", "{CALL pomdb.vypisPolozekSOdhadHmot()}", "{CALL pomdb.vypisMzdySlevacu(?,?)}",
				"{CALL pomdb.vypisOdlitychKusuOdDo(?,?,?,?)}", "{CALL pomdb.vypisVycistenychKusuOdDo(?,?)}", "{CALL pomdb.vypisRozpracovaneVyroby()}", "{CALL pomdb.vypisExpedovanychKusuOdDo(?,?)}", "{CALL pomdb.vypisKusuNaSkladu()}",
				"{CALL pomdb.vypisStavNeuzavrenychZakazek(?,?,?,?,?,?,?)}", "{CALL pomdb.vypisDenniOdlitychKusu(?)}", "{CALL pomdb.vypisZmetky(?,?)}", "{CALL pomdb.vypisVinikyVKgKc(?,?)}",
				"{CALL pomdb.vypisStavNeuzavrenychZakazek_short(?,?,?,?,?,?,?)}", "{CALL pomdb.VypisStavZakazek(?,?,?,?,?)}", "{CALL pomdb.vypisVytizeniKapacit(?,?,?,?,?)}"},
			{"{CALL pomdb.liciPlanZakl(?,?,?)}", "{CALL pomdb.liciPlanPlanovaci(?,?,?)}", "{CALL pomdb.vyberDilciTerminy(?)}", "{CALL pomdb.vyberDilciTerminySeJmeny(?)}", 
				"{CALL pomdb.plan_expedice()}"},
			{"{CALL pomdb.smaz_fyz_kus(?,?)}"},
			{"{CALL pomdb.zalohaDatabaze()}","{CALL pomdb.grant_user(?,?,?)}"}
	};
	/**
	 * Prikazy pro vybrani viniku a vad a vlastních materialu
	 */
	private final String [] vadyVinici = {
		"{CALL pomdb.vyberViniky(?)}",
		"{CALL pomdb.vyberVady(?)}",
		"{CALL pomdb.vyberVlastniMaterialy()}"
		};
	/**
	 * Timer ktery generuje kazdou chvilku akci ktera zkontroluje zda ma uzavøit statements.
	 */
	private Timer casovac;
	
	/**
	 * Úložištì pro objekt Connection a pro objekty CallableStatement. Tato tøída je implementována pøesnì na míru mojí databáze.
	 * @param sklad Sklad, kde jsou uloženy vìtšina promìnných, respektive jejich odkazy
	 */
	public SQLStor(SkladOdkazu sklad){
		this.sklad = sklad;
		this.conn = this.sklad.getConn();
		this.hlavniOkno = sklad.getHlavniOkno();
		prgbar.setVisible(false);
		cst = new CallableStatement[sqlPrikazy.length][];
		naposledyPouzito = new Date[sqlPrikazy.length][];
		for(int i = 0; i < sqlPrikazy.length; i++){
			cst[i] = new CallableStatement[sqlPrikazy[i].length];
			naposledyPouzito[i] = new Date[sqlPrikazy[i].length];
			for(int j = 0; j < sqlPrikazy[i].length; j++){
				cst[i][j] = null;
				naposledyPouzito[i][j] = null;
			}
		}
		PosluchacCasovace posluchac = new PosluchacCasovace();
        casovac = new Timer(dobaNaZavrenyPripojeni, posluchac);
        casovac.start();
	}
	
	/**
	 * Slouží pro generováni nových kusù místo zmetkù v dané zakázce
	 * @param idZakazky id zakázky
	 * @throws SQLException 
	 */
	public void gennerujNoveKusy(int idZakazky) throws SQLException{
		int i = 4, j = 3;
		if(idZakazky <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zakázky je špatnì");
			return;
		}
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky);
		c.execute();
		JOptionPane.showMessageDialog(hlavniOkno, "Nové kusy generovány");
	}
	
	/**
	 * @param jmeno nové jmeno zakaznika
	 * @throws SQLException
	 */
	
	public void novyZakaznik(String jmeno) throws SQLException{
		int i = 0, j = 0;
		if(jmeno == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je prázdné");
			return;
		}
		else if(jmeno.equalsIgnoreCase("") || jmeno.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je prázdné nebo moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return;
		}
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setString(1, jmeno);
		ResultSet rs = c.executeQuery();
		if(rs.next()){
			JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Zákazník byl úspìšnì pøidán do databáze. Id: "+rs.getInt(1));
		} else {
			JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Zákazník nebyl úspìšnì pøidán do databáze!!!");
		}
		
	}
	
	/**
	 * 
	 * @param jmeno
	 * @param cisloModelu
	 * @param material
	 * @param materialVlastni
	 * @param hmotnost
	 * @param isOdhadHmot
	 * @param formovna
	 * @param norma
	 * @param poznamkaModel
	 * @throws SQLException
	 */
	
	public void novyModel(String jmeno, String cisloModelu, String material, String materialVlastni, double hmotnost,
			boolean isOdhadHmot,  String formovna, double norma, String poznamkaModel) throws SQLException{
		int i = 0, j = 1;
		if(jmeno == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno modelu je prázdné");
			return;
		}
		else if(jmeno.equalsIgnoreCase("") || jmeno.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno modelu je prázdné nebo moc velké");
			return;
		}
		if(cisloModelu == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo modelu je prázdné");
			return;
		}
		else if(cisloModelu.equalsIgnoreCase("") || cisloModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo modelu je prázdné nebo moc velké");
			return;
		}
		if(material == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno materiálu je prázdné");
			return;
		}
		else if(material.equalsIgnoreCase("") || material.length() > maxDelkamaterialu){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno materiálu je prázdné nebo moc velké");
			return;
		}
		if(materialVlastni == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno vlastního materiálu je prázdné");
			return;
		}
		else if(materialVlastni.equalsIgnoreCase("") || materialVlastni.length() > maxDelkaVlastnihoMaterialu){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno vlastního mareriálu je prázdné nebo moc velké");
			return;
		}
		if(hmotnost <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Hmotnost je menší nebo rovno nule");
			return;
		}
		if(formovna == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Formovna je špatnì zapsaná");
			return;
		}
		else if(formovna.equalsIgnoreCase("") || formovna.length() > 1){
			JOptionPane.showMessageDialog(hlavniOkno, "Formovna je špatnì zapsaná");
			return;
		}
		if(norma <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Norma je menší nebo rovno nule");
			return;
		}
		if(poznamkaModel != null){
			if(poznamkaModel.length() > maxDelkaPoznamkyUModelu){
				JOptionPane.showMessageDialog(hlavniOkno, "Poznámka je moc dlouhá");
				return;
			}
		}	
		
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setString(1, jmeno); c.setString(2, cisloModelu); c.setString(3, material); c.setString(4, materialVlastni);
		c.setDouble(5, hmotnost); c.setBoolean(6, isOdhadHmot); c.setString(7, formovna); c.setDouble(8, norma);c.setString(9, poznamkaModel);
		ResultSet rs = c.executeQuery();
		if(rs.next()){
			JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Model byl úspìšnì pøidán do databáze, jeho ID je: "+rs.getInt(1));
		} else {
			JOptionPane.showMessageDialog(sklad.getHlavniOkno(),"Model nebyl úspìšnì pøidán do databáze!!!");
		}
		
	}
	
	/**
	 * 
	 * @param idZakaznika
	 * @param idModelu
	 * @param cisloObjednavky
	 * @param datumPrijetiZakazky
	 * @param pocetKusu
	 * @param paganyrka
	 * @param cena
	 * @param isCZK
	 * @param isZakus
	 * @param KurzEuNaCZK
	 * @param poznamka
	 * @param datumTerminuExpedice
	 * @param seznamDilTerminu
	 * @return Id nové zakázky nebo -1 pokud se to nepodaøí
	 * @throws SQLException
	 */
	public int novaZakazka(int idZakaznika, int idModelu, String cisloObjednavky, Date datumPrijetiZakazky, int pocetKusu, String paganyrka,
			double cena, boolean isCZK, boolean isZakus, double KurzEuNaCZK, String poznamka, Date datumTerminuExpedice, DefaultListModel<DateStor> seznamDilTerminu) throws SQLException{
		if(idZakaznika < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zákazníka je menší než nula");
			return -1;
		}
		if(idModelu < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id modelu je menší než nula");
			return -1;
		}
		if(cisloObjednavky == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo objednávky je prázdné");
			return -1;
		}else if(cisloObjednavky.equalsIgnoreCase("") || maxDelkaRetezce < cisloObjednavky.length()){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo objednávky je moc velké nebo je prázdné, mùže obsahovat pouze "+maxDelkaRetezce+" znakù");
			return -1;
		}
		if(datumPrijetiZakazky == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum pøijetí objednávky je špatnì");
			return -1;
		}
		if(pocetKusu <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Poèet kusù je menší nebo rovno nule");
			return -1;
		}else if(pocetKusu > maxPocetKusuNovaZakazka){
			JOptionPane.showMessageDialog(hlavniOkno, "Databáze je nastavena na maximální poèet kusù v objednávce na "+maxPocetKusuNovaZakazka+" kusù, aby omylem nedošlo k vygenerování pøilíš nových kusù a zahlcení databáze. \n V pøípadì, že jednáte to úmyslnì rozdìltì zakázku na více èástí.");
			return -1;
		}
		if(paganyrka != null){
			if(paganyrka.length() > maxDelkaPaganyrky){
				JOptionPane.showMessageDialog(hlavniOkno, "Paganýrka je moc mùže obsahovat max "+maxDelkaPaganyrky+" znakù");
				return -1;
			}
		}
		if(!isCZK){
			if(KurzEuNaCZK <= 0){
				JOptionPane.showMessageDialog(hlavniOkno, "Kurz je menší nebo rovno nule");
				return -1;
			}
		}
		if(!overCenu(isCZK, KurzEuNaCZK, cena)){
			JOptionPane.showMessageDialog(hlavniOkno, "Cena "+cena+" je mimo rozsah 0 až "+(int) maxCena+ ", bez krajních hodnot");
			return -1;
		}		
		if(poznamka != null){
			if(poznamka.length() > maxDelkaPoznamkyUZakazky){
				JOptionPane.showMessageDialog(hlavniOkno, "Poznámka je moc mùže obsahovat max "+maxDelkaPoznamkyUZakazky+" znakù");
				return -1;
			}
		}
		if(datumTerminuExpedice == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum expedice je špatnì"); 
			return -1;
		} else if (datumPrijetiZakazky.after(datumTerminuExpedice)) {
			int response = JOptionPane.showConfirmDialog(hlavniOkno, "Datum expedice je døív než byla pøijata zakazka, chcete pokraèovat?", "Upozornìní", JOptionPane.YES_NO_OPTION);
			if(response == 0){
				JOptionPane.showMessageDialog(hlavniOkno, "Zadavání pøerušeno"); 
				return -1;
			}
		}
		if(seznamDilTerminu == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Seznam dilèích termínu je null"); 
			return -1;
		}		
		int i = 0, j = 2;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		java.sql.Date  pomDate = new java.sql.Date (datumPrijetiZakazky.getTime());
		c.setInt(1, idZakaznika); c.setInt(2, idModelu); c.setString(3, cisloObjednavky); c.setInt(4, pocetKusu); c.setDate(5, pomDate);
		if(paganyrka == null || paganyrka.equalsIgnoreCase("")){
			c.setNull(6, java.sql.Types.VARCHAR);
		}
		else {
			c.setString(6, paganyrka);
		}
		c.setDouble(7, cena); c.setBoolean(8, isCZK); c.setBoolean(9, isZakus);
		
		if(isCZK){
			c.setNull(10, java.sql.Types.DOUBLE);
		}
		else{
			c.setDouble(10, KurzEuNaCZK);
		}
		if(poznamka == null || poznamka.equalsIgnoreCase("")){
			c.setNull(11, java.sql.Types.VARCHAR);
		}else {
			c.setString(11, poznamka);
		}
		pomDate = new java.sql.Date (datumTerminuExpedice.getTime());
		c.setDate(12, pomDate);
		rs = c.executeQuery();
		for(i = 0; i < seznamDilTerminu.getSize(); i++){
			//asdas
		}
		int id = -1;
		if (rs.next()) {
			if (!rs.getBoolean(1)) {
				JOptionPane.showMessageDialog(hlavniOkno,"Duplicita, stejná zakázka už existuje (nebere se v podtaz poznámka, ta mùže být ruzná) \n nebo"
										+ "jste zadali vice jak 1500 kusu v nove zakazce");
			} else {
				id = rs.getInt(2);
				JOptionPane.showMessageDialog(hlavniOkno, "Pøidáno, id nové zakázky je: "+id);
			}
		} else {
			JOptionPane.showMessageDialog(hlavniOkno, "Chyba v SQLStor novaZakazka() radek 340, dotaz nevrátil zda byl vykonán");
		}
		return id;
		
	}
	
	/**
	 * 
	 * @return Objekt ResultSet všech zakazniku v databazi, null jen tehdy pokud jsou parametry ve Stringu moc dlouhe (viz nastaveni databaze)
	 * @throws SQLException 
	 */

	
	public ResultSet vyberZakazniky(String jmeno) throws SQLException{
		if(jmeno == null)jmeno = "";
		if(jmeno.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je prázdné nebo moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		int i = 1,j = 0;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setString(1, jmeno);
		return c.executeQuery();
	}

	/**
	 * 
	 * @param jmenoZakaznika pokud nechcete vyhledavat podle jmena zákazníka, zadejte "" a ne null
	 * @param cisloModelu
	 * @param nazevModelu
	 * @param idModelu
	 * @param datumZakazky
	 * @param formovna
	 * @param idZakazky
	 * @return
	 * @throws SQLException
	 */
	
	public ResultSet vyberModely(String jmenoZakaznika, String cisloModelu, String nazevModelu, int idModelu, java.util.Date datumZakazky, String formovna, int idZakazky, boolean vcetneUzavrenych) throws SQLException{
		if(jmenoZakaznika == null){
			jmenoZakaznika = "";
		}else if(jmenoZakaznika.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je prázdné nebo moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(cisloModelu == null) {
			cisloModelu = "";
		}else if(cisloModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo modelu je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(nazevModelu == null){
			nazevModelu = "";
		}else if(nazevModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(idModelu < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id modelu je menší než nula");
			return null;
		}		
		if(formovna == null){
			formovna = "";
		} else if (formovna.length() != 1){
			formovna = "";
		}
		if(idZakazky < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id modelu je menší než nula");
			return null;
		}
		
		int i = 1,j = 1;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setString(1, jmenoZakaznika); c.setString(2, cisloModelu); c.setString(3, nazevModelu); c.setInt(4, idModelu);
		
		if(datumZakazky == null) {
			c.setNull(5, java.sql.Types.DATE);		
		}else {
			java.sql.Date sqlDate = new java.sql.Date(datumZakazky.getTime());
			c.setDate(5, sqlDate);  // datumZakazky 
		}		
		c.setString(6, formovna); c.setInt(7, idZakazky); c.setBoolean(8, vcetneUzavrenych);
		return c.executeQuery();
	}
	
	/**
	 * 
	 * @param idZakazky
	 * @param jmenoZakaznika
	 * @param cisloModelu
	 * @param nazevModelu
	 * @param idModelu
	 * @param datumZakazky
	 * @param cisloObjednavky
	 * @return
	 * @throws SQLException
	 */
	
	public ResultSet vyberZakazky(int idZakazky, String jmenoZakaznika, String cisloModelu, String nazevModelu, int idModelu, java.util.Date datumZakazky, String cisloObjednavky, boolean vcetneUzavrenych) throws SQLException{
		if(idZakazky < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zakázky je menší než nula");
			return null;
		}
		if(jmenoZakaznika == null){
			jmenoZakaznika = "";
		}else if(jmenoZakaznika.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(cisloModelu == null) {
			cisloModelu = "";
		}else if(cisloModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo modelu je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(nazevModelu == null){
			nazevModelu = "";
		}else if(nazevModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(cisloObjednavky == null){
			cisloObjednavky = "";
		}else if(cisloObjednavky.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo objednávky je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(idModelu < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id modelu je menší než nula");
			return null;
		}		
		int i = 1,j = 2;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky); c.setString(2, jmenoZakaznika); c.setString(3, cisloModelu); c.setString(4, nazevModelu); c.setInt(5, idModelu);
		
		if(datumZakazky == null) {
			c.setNull(6, java.sql.Types.DATE);		
		}else {
			java.sql.Date sqlDate = new java.sql.Date(datumZakazky.getTime());
			c.setDate(6, sqlDate);  // datumZakazky 
		}
		c.setString(7, cisloObjednavky);
		c.setBoolean(8, vcetneUzavrenych);
		return c.executeQuery();
	}
	
	/**
	 * 
	 * @param idZakazky
	 * @return
	 * @throws SQLException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	
	public TransClass vyberFyzKusy(int idZakazky) throws SQLException, InterruptedException, ExecutionException{
		if(idZakazky < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zakázky je v programu špatnì zapsáno");
			return null;
		}
		int i = 1, j = 3;
		if (cst[i][j] == null) {
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky);
		c.registerOutParameter(2, java.sql.Types.SMALLINT);
		ResultSet rs = c.executeQuery();
		TransClass s = new TransClass(c.getInt(2), rs);
		return s;
	}
	
	/**
	 * Ještì se musí implementovat
	 * @param idZakazky
	 * @param jmenoZakaznika
	 * @param cisloModelu
	 * @param nazevModelu
	 * @param idModelu
	 * @param datumZakazky
	 * @param cisloObjednavky
	 * @return
	 * @throws SQLException
	 */
	
	public ResultSet vyberZmetky(int idZakazky, String jmenoZakaznika, String cisloModelu, String nazevModelu, int idModelu, java.util.Date datumZakazky, String cisloObjednavky, boolean vcetneUzav) throws SQLException{
		if(idZakazky < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zakázky je menší než nula");
			return null;
		}
		if(jmenoZakaznika == null){
			jmenoZakaznika = "";
		}else if(jmenoZakaznika.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(cisloModelu == null) {
			cisloModelu = "";
		}else if(cisloModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo modelu je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(nazevModelu == null){
			nazevModelu = "";
		}else if(nazevModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(cisloObjednavky == null){
			cisloObjednavky = "";
		}else if(cisloObjednavky.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo objednávky je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(idModelu < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id modelu je menší než nula");
			return null;
		}		
		int i = 1,j = 4;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky); c.setString(2, jmenoZakaznika); c.setString(3, cisloModelu); c.setString(4, nazevModelu); c.setInt(5, idModelu);
		
		if(datumZakazky == null) {
			c.setNull(6, java.sql.Types.DATE);		
		}else {
			java.sql.Date sqlDate = new java.sql.Date(datumZakazky.getTime());
			c.setDate(6, sqlDate);  // datumZakazky 
		}
		c.setString(7, cisloObjednavky); c.setBoolean(8, vcetneUzav);
		return c.executeQuery();
	}
	
	/**
	 * 
	 * @return ResultSet s Viniky, které uložím do tøídy skladodkazu
	 * @throws SQLException
	 */
	public ResultSet vyberViniky(String jmeno) throws SQLException{
		CallableStatement s = conn.prepareCall(vadyVinici[0]);
		if(jmeno == null){
			s.setNull("jmenoVinika", java.sql.Types.VARCHAR);
		} else {
			s.setString("jmenoVinika", jmeno);
		}
		rs = s.executeQuery();
		return rs;
	}
	
	/**
	 * 
	 * @return ResultSet s Vady, které uložím do tøídy skladodkazu
	 * @throws SQLException
	 */
	public ResultSet vyberVady(String jmeno) throws SQLException{
		CallableStatement s = conn.prepareCall(vadyVinici[1]);
		if(jmeno == null){
			s.setNull("jmenoVady", java.sql.Types.VARCHAR);
		} else {
			s.setString("jmenoVady", jmeno);
		}
		rs = s.executeQuery();
		return rs;
	}
	
	/**
	 * Upravi zakazníka
	 * @param idZakaznika Id zákazníka v databazi
	 * @param noveJmeno Nové jmeno
	 * @throws SQLException 
	 */
	
	public void updateZakaznika(int idZakaznika, String noveJmeno) throws SQLException{
		if(idZakaznika < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zákazníka je špatnì v pragramu zapsané");
			return;
		}
		int i = 2, j = 0;
		if(noveJmeno == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Nové jméno je prázdné");
			return;
		}else if(noveJmeno.equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(hlavniOkno, "Nové jméno je prázdné");
			return;
		}
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakaznika);
		c.setString(2, noveJmeno);
		c.execute();
	}
	
	/**
	 * 
	 * @param idModelu
	 * @param jmenoModelu
	 * @param cisloModelu
	 * @param material
	 * @param materialVlastni
	 * @param hmotnost
	 * @param isOdhadHmot
	 * @param formovna
	 * @param norma
	 * @param poznamkaModel
	 * @throws SQLException
	 */
	public void updateModel(int idModelu, String jmenoModelu, String cisloModelu, String material, String materialVlastni,
			double hmotnost, boolean isOdhadHmot, String formovna, double norma, String poznamkaModel) throws SQLException{
		if(idModelu < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id modelu je špatnì zapsané v programu");
			return;
		}
		if(jmenoModelu == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno modelu je prázdné");
			return;
		}else if(jmenoModelu.length() > maxDelkaRetezce || jmenoModelu.equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno modelu je prázdné nebo moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return;
		}
		if(cisloModelu == null) {
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo modelu je prázdné");
			return;
		}else if(cisloModelu.length() > maxDelkaRetezce || cisloModelu.equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo modelu je prázdné nebo moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return;
		}
		if(material == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Material je prázdný");
			return;
		}else if(material.length() > maxDelkamaterialu || material.equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(hlavniOkno, "Material je prázdný nebo moc dlouhý, mùže obsahovat max "+maxDelkamaterialu+" znakù");
			return;
		}
		if(materialVlastni == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Material je prázdný");
			return;
		}else if(materialVlastni.length() > maxDelkamaterialu || materialVlastni.equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(hlavniOkno, "Vlastní material je prázdný nebo moc dlouhý, mùže obsahovat max "+maxDelkamaterialu+" znakù");
			return;
		}
		if(hmotnost <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Hmotnost je menší nebo rovno nule");
			return;
		}
		if(formovna == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Formovna je špatnì zapsána");
			return;
		}
		else if( formovna.length() != 1){
			JOptionPane.showMessageDialog(hlavniOkno, "Formovna je špatnì zapsána");
			return;
		}
		if(poznamkaModel != null){
			if(poznamkaModel.length() > maxDelkaPoznamkyUModelu){
				JOptionPane.showMessageDialog(hlavniOkno, "Poznámka je moc dlouhá");
				return;
			}
		}			
		
		
		
		
		int i = 2, j = 1;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idModelu); c.setString(2, jmenoModelu); c.setString(3, cisloModelu); c.setString(4, material); c.setString(5, materialVlastni);
		c.setDouble(6, hmotnost); c.setBoolean(7, isOdhadHmot); c.setString(8, formovna); c.setDouble(9, norma);c.setString(10, poznamkaModel);
		c.execute();
	}
	
	/**
	 * 
	 * @param idZakazky
	 * @param idZakaznika
	 * @param idModelu
	 * @param cisloObjednavky
	 * @param datumPrijetiZakazky
	 * @param pocetKusu
	 * @param paganyrka
	 * @param cena
	 * @param isCZK
	 * @param isZakus
	 * @param KurzEuNaCZK
	 * @param poznamka
	 * @param datumTerminuExpedice
	 * @throws SQLException
	 */
	
	public void updateZakazku(int idZakazky, int idZakaznika, int idModelu, String cisloObjednavky, Date datumPrijetiZakazky, int pocetKusu, String paganyrka,
			double cena, boolean isCZK, boolean isZakus, double KurzEuNaCZK, String poznamka, Date datumTerminuExpedice) throws SQLException{
		if(idZakazky < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zákazníka je menší než nula");
			return;
		}
		if(idZakaznika < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zákazníka je menší než nula");
			return;
		}
		if(idModelu <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id modelu je menší než nula");
			return;
		}
		if(cisloObjednavky == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo objednávky je prázdné");
			return;
		}else if(cisloObjednavky.equalsIgnoreCase("") || maxDelkaRetezce < cisloObjednavky.length()){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo objednávky je moc velké nebo je prázdné, mùže obsahovat pouze "+maxDelkaPaganyrky+" znakù");
			return;
		}
		if(datumPrijetiZakazky == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum pøijetí objednávky je špatnì");
			return;
		}
		if(pocetKusu <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Poèet kusù je menší nebo rovno nule");
			return;
		}
		if(paganyrka != null){
			if(paganyrka.length() > maxDelkaPaganyrky){
				JOptionPane.showMessageDialog(hlavniOkno, "Paganýrka je moc mùže obsahovat max "+maxDelkaPaganyrky+" znakù");
				return;
			}
		}
		if(!isCZK){
			if(KurzEuNaCZK <= 0){
				JOptionPane.showMessageDialog(hlavniOkno, "Kurz je menší nebo rovno nule");
				return;
			}
		}
		if(!overCenu(isCZK, KurzEuNaCZK, cena)){
			JOptionPane.showMessageDialog(hlavniOkno, "Cena v czk je mimo rozsah 0 až "+(int) maxCena+ ", bez krajních hodnot");
			return;
		}
		if(poznamka != null){
			if(poznamka.length() > maxDelkaPoznamkyUZakazky){
				JOptionPane.showMessageDialog(hlavniOkno, "Poznámka je moc mùže obsahovat max"+maxDelkaPoznamkyUZakazky+" znakù");
				return;
			}
		}
		if(datumTerminuExpedice == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum expedice je špatnì"); 
			return;
		} else if (datumPrijetiZakazky.after(datumTerminuExpedice)) {
			int response = JOptionPane.showConfirmDialog(hlavniOkno, "Datum expedice je døív než byla pøijata zakazka, chcete pokraèovat?", "Upozornìní", JOptionPane.YES_NO_OPTION);
			//System.out.println(response);
			if(response == 1){
				JOptionPane.showMessageDialog(hlavniOkno, "Zadavání pøerušeno"); 
				return;
			}
		}
		int i = 2, j = 2;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		java.sql.Date pomDate = new java.sql.Date (datumPrijetiZakazky.getTime());
		
		c.setInt(1, idZakazky);
		c.setInt(2, idZakaznika); c.setInt(3, idModelu); c.setString(4, cisloObjednavky); c.setInt(5, pocetKusu); c.setDate(6, pomDate);
		if(paganyrka == null || paganyrka.equalsIgnoreCase("")){
			c.setNull(7, java.sql.Types.VARCHAR);
		}
		else {
			c.setString(7, paganyrka);
		}
		c.setDouble(8, cena); c.setBoolean(9, isCZK); c.setBoolean(10, isZakus);
		
		if(isCZK){
			c.setNull(11, java.sql.Types.DOUBLE);
		}
		else{
			c.setDouble(11, KurzEuNaCZK);
		}
		if(poznamka == null || poznamka.equalsIgnoreCase("")){
			c.setNull(12, java.sql.Types.VARCHAR);
		}else {
			c.setString(12, poznamka);
		}
		pomDate = new java.sql.Date (datumTerminuExpedice.getTime());
		c.setDate(13, pomDate);
		rs = c.executeQuery();
		if(rs.next()){
			boolean pom = rs.getBoolean(1);
			if(pom){
				JOptionPane.showMessageDialog(hlavniOkno, "Zakázka byla úspìšnì upravena");
			} else {
				JOptionPane.showMessageDialog(hlavniOkno, "Nepodaøilo se upravit zakázku. Mohlo to být zpùsobeno tím, že jste zadali ménì objednaných kusù než bylo, to nelze.");
			}
		}
	}
	
	public Statement liciPlanZakl(int cisloTydne, int rok, String formovna) throws SQLException{
		if(cisloTydne < 1 || cisloTydne > 53){
			JOptionPane.showMessageDialog(hlavniOkno, "Zadejte èíslo týdne musí být v rozmezí 1 - 53");
			return null;
		}
		if(rok < 1800 || rok > 9999){
			JOptionPane.showMessageDialog(hlavniOkno, "Rok musí být v rozmezí 1800 - 9999.");
			return null;
		}
		if(formovna.length() != 1){
			JOptionPane.showMessageDialog(hlavniOkno, "Formovna je špatnì. Má mít jen 1 znak");
			return null;
		}
		int i = 6, j = 0;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, cisloTydne);
		c.setInt(2, rok);
		c.setString(3,formovna);
		c.execute();
		return c;
	}
	
	public Statement liciPlanovaci(int cisloTydne, int rok, String formovna) throws SQLException{
		if(cisloTydne < 1 || cisloTydne > 53){
			JOptionPane.showMessageDialog(hlavniOkno, "Zadejte èíslo týdne musí být v rozmezí 1 - 53");
			return null;
		}
		if(rok < 1800 || rok > 9999){
			JOptionPane.showMessageDialog(hlavniOkno, "Rok musí být v rozmezí 1800 - 9999. Co to sakra chcete za výpis :D (SQLStor.java 820)");
			return null;
		}
		if(formovna.length() != 1){
			JOptionPane.showMessageDialog(hlavniOkno, "Formovna je špatnì. Má mít jen 1 znak");
			return null;
		}
		int i = 6, j = 1;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, cisloTydne);
		c.setInt(2, rok);
		c.setString(3, formovna);
		c.execute();
		return c;
	}
	
	public Statement planExpedice() throws SQLException{
		int i = 6, j = 4;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.execute();
		return c;
	}
	
	/**
	 * 
	 * @param idZakazky
	 * @param jmenoZakaznika
	 * @param cisloModelu
	 * @param nazevModelu
	 * @param idModelu
	 * @param datumZakazky
	 * @param cisloObjednavky
	 * @return
	 * @throws SQLException
	 */
	public ResultSet vypisStavuNeuzavrenychZakazek(int idZakazky, String jmenoZakaznika, String cisloModelu, String nazevModelu, int idModelu, java.util.Date datumZakazky, String cisloObjednavky) throws SQLException{
		if(idZakazky < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zakázky je menší než nula");
			return null;
		}
		if(jmenoZakaznika == null){
			jmenoZakaznika = "";
		}else if(jmenoZakaznika.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(cisloModelu == null) {
			cisloModelu = "";
		}else if(cisloModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo modelu je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(nazevModelu == null){
			nazevModelu = "";
		}else if(nazevModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(cisloObjednavky == null){
			cisloObjednavky = "";
		}else if(cisloObjednavky.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo objednávky je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(idModelu < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id modelu je menší než nula");
			return null;
		}		
		int i = 5, j = 10;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky); c.setString(2, jmenoZakaznika); c.setString(3, cisloModelu); c.setString(4, nazevModelu); c.setInt(5, idModelu);
		
		if(datumZakazky == null) {
			c.setNull(6, java.sql.Types.DATE);		
		}else {
			java.sql.Date sqlDate = new java.sql.Date(datumZakazky.getTime());
			c.setDate(6, sqlDate);  // datumZakazky 
		}
		c.setString(7, cisloObjednavky);
		rs = c.executeQuery();
		return rs;
	}
	
	public ResultSet vypisStavuNeuzavrenychZakazekShort(int idZakazky, String jmenoZakaznika, String cisloModelu, String nazevModelu, int idModelu, java.util.Date datumZakazky, String cisloObjednavky) throws SQLException{
		if(idZakazky < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zakázky je menší než nula");
			return null;
		}
		if(jmenoZakaznika == null){
			jmenoZakaznika = "";
		}else if(jmenoZakaznika.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(cisloModelu == null) {
			cisloModelu = "";
		}else if(cisloModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo modelu je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(nazevModelu == null){
			nazevModelu = "";
		}else if(nazevModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(cisloObjednavky == null){
			cisloObjednavky = "";
		}else if(cisloObjednavky.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo objednávky je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(idModelu < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id modelu je menší než nula");
			return null;
		}		
		int i = 5, j = 14;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky); c.setString(2, jmenoZakaznika); c.setString(3, cisloModelu); c.setString(4, nazevModelu); c.setInt(5, idModelu);
		
		if(datumZakazky == null) {
			c.setNull(6, java.sql.Types.DATE);		
		}else {
			java.sql.Date sqlDate = new java.sql.Date(datumZakazky.getTime());
			c.setDate(6, sqlDate);  // datumZakazky 
		}
		c.setString(7, cisloObjednavky);
		rs = c.executeQuery();
		return rs;
	}
	
	
	public ResultSet vypisStavuZakazek(String jmenoZakaznika, String cisloModelu, String nazevModelu, int idModelu, java.util.Date datumZakazky) throws SQLException{
		if(jmenoZakaznika == null){
			jmenoZakaznika = "";
		}else if(jmenoZakaznika.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(cisloModelu == null) {
			cisloModelu = "";
		}else if(cisloModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo modelu je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(nazevModelu == null){
			nazevModelu = "";
		}else if(nazevModelu.length() > maxDelkaRetezce){
			JOptionPane.showMessageDialog(hlavniOkno, "Jméno zákazníka je moc dlouhé, mùže obsahovat max "+maxDelkaRetezce+" znakù");
			return null;
		}
		if(idModelu < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id modelu je menší než nula");
			return null;
		}		
		int i = 5, j = 15;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setString(1, jmenoZakaznika); c.setString(2, cisloModelu); c.setString(3, nazevModelu); c.setInt(4, idModelu);
		
		if(datumZakazky == null) {
			c.setNull(5, java.sql.Types.DATE);		
		}else {
			java.sql.Date sqlDate = new java.sql.Date(datumZakazky.getTime());
			c.setDate(5, sqlDate);  // datumZakazky 
		}
		rs = c.executeQuery();
		return rs;
	}
	
	public Statement denniVypisOdlitku(Date datum) throws SQLException{
		if(datum == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Zadejte správnì datum.");
			return null;
		}
		int i = 5, j = 11;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		java.sql.Date pomDate;
		pomDate = new java.sql.Date(datum.getTime());
		c.setDate("datum", pomDate);
		c.execute();
		return c;
	}
	
	public ResultSet vypisOdlitkuVKgKc(Date od, Date do_) throws SQLException{
		if(od == null || do_ == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Zadejte správnì oba datumy.");
			return null;
		}
		int i = 5, j = 0;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		java.sql.Date pomDate;
		pomDate = new java.sql.Date (od.getTime());
		c.setDate("od", pomDate);
		pomDate = new java.sql.Date (do_.getTime());
		c.setDate("do_", pomDate);
		rs = c.executeQuery();
		return rs;
	}
	
	public ResultSet vypisZpozdeniVyroby(Date od) throws SQLException{
		int i = 5, j = 1;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		
		if(od == null){
			c.setNull("od", java.sql.Types.DATE);
		}else {
			java.sql.Date pomDate = new java.sql.Date (od.getTime());
			c.setDate("od", pomDate);
		}
		rs = c.executeQuery();
		return rs;
	}
	
	/**
	 * Vypsat vsechny zakazky, ktere se expeduji v dany tyden, respektive maji termin expedice v danem tydnu
	 * @param cisloTydne 
	 * @param rok
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet vypisDleTerminuExpedice(int cisloTydne, int rok) throws SQLException{
		if(cisloTydne < 1 || cisloTydne > 53){
			JOptionPane.showMessageDialog(hlavniOkno, "Zadejte èíslo týdne musí být v rozmezí 1 - 53");
			return null;
		}
		if(rok < 1800 || rok > 9999){
			JOptionPane.showMessageDialog(hlavniOkno, "Rok musí být v rozmezí 1800 - 9999. Co to sakra chcete za výpis :D (SQLStor.java 820)");
		}
		int i = 5, j = 2;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, rok);
		cal.set(Calendar.WEEK_OF_YEAR, cisloTydne);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		java.util.Date datum = cal.getTime();
		java.sql.Date pomDate = new java.sql.Date (datum.getTime());
		c.setDate(1, pomDate);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		datum = cal.getTime();
		pomDate = new java.sql.Date (datum.getTime());
		c.setDate(2, pomDate);

		rs = c.executeQuery();
		return rs;
	}
	
	/**
	 * Vypise vsechny modely s odhadovanou hmotnosti
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet vypisPolozekSOdhadHmot() throws SQLException{
		int i = 5, j = 3;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		rs = c.executeQuery();
		return rs;
	}
	
	/**
	 * Vypis mzdy slevacu od-do
	 * @param datumOd
	 * @param datumDo
	 * @return
	 * @throws SQLException
	 */
	public Statement vypisMzdySlevacu(Date datumOd, Date datumDo) throws SQLException{
		if(datumOd == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum nesmí být prázdné");
			return null;
		}
		if(datumDo == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum nesmí být prázdné");
			return null;
		}
		int i = 5, j = 4;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		java.sql.Date pomDate = null;
		pomDate = new java.sql.Date (datumOd.getTime());
		c.setDate("datum_od", pomDate);
		pomDate = new java.sql.Date (datumDo.getTime());
		c.setDate("datum_do", pomDate);
		c.execute();
		return c;
	}
	
	/**
	 * Vypise vsechny kusy co nejsou zmetky, nejsou v uzavrene zakazce a maji odlito = true v danem terminu 
	 * @param od
	 * @param do_
	 * @param formovna
	 * @param vlastni_materialy Vytvoøí z techto jmen regularni vyraz, ktery potom pošle do databaze pro shody.
	 * @return
	 * @throws SQLException
	 */
	public Statement vypisOdlitychKusuOdDoRegEx(Date od, Date do_, String formovna, String [] vlastni_materialy) throws SQLException{
		if(od == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum od nesmí být prázdné");
			return null;
		}
		if(do_ == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum do nesmí být prázdné");
			return null;
		}
		if(formovna == null){
			formovna = "";
		}
		if(vlastni_materialy == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Vlastni materialy nesmí být prazdné :P");
			return null;
		}
		int i = 5, j = 5;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		// tvorba regularniho vyrazu
		String regEx = "";
		if(vlastni_materialy[0] == null){
			vlastni_materialy[0] = "Vše";
		}
		if(vlastni_materialy[0].equals("Vše")){
			regEx = "."; // beru vse
		} else {
			regEx = "^"+vlastni_materialy[0]+"$";
			for(int m = 1; m < vlastni_materialy.length; m++){
				regEx += "|^"+vlastni_materialy[m]+"$";
			}
		}
		if(regEx.length() > 500){
			JOptionPane.showMessageDialog(hlavniOkno, "Je vybráno více materiálù než je dovoleno.");
			return null;
		}
		c = cst[i][j];
		java.sql.Date pomDate = new java.sql.Date (od.getTime());
		c.setDate("od", pomDate);
		pomDate = new java.sql.Date (do_.getTime());
		c.setDate("do_", pomDate);
		c.setString("formovna", formovna);
		c.setString("vlastni_material_reg_ex", regEx);
		c.execute();
		return c;
	}
	
	/**
	 * Vypise vsechny kusy co nejsou zmetky, nejsou v uzavrene zakazce a maji odlito = true a vycisteno = true v danem terminu 
	 * @param od
	 * @param do_
	 * @return
	 * @throws SQLException 
	 */
	public Statement vypisVycistenychKusuOdDo(Date od, Date do_) throws SQLException{
		if(od == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum od nesmí být prázdné");
			return null;
		}
		if(do_ == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum do nesmí být prázdné");
			return null;
		}
		int i = 5, j = 6;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		java.sql.Date pomDate = new java.sql.Date (od.getTime());
		c.setDate("od", pomDate);
		pomDate = new java.sql.Date (do_.getTime());
		c.setDate("do_", pomDate);
		c.executeQuery();
		return c;
	}
	
	/**
	 * Vypise a seète všechny fyzKusy zmetek = false, odlito = true a vycisteno = false a uzavreno = false
	 * @return
	 * @throws SQLException 
	 */
	public Statement vypisRozpracovaneVyroby() throws SQLException{
		int i = 5, j = 7;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.executeQuery();
		return c;
	}
	
	/**
	 * Vypise a secte vsechny fyzKusy, ktere byly expedovany = true a jejich datum Dilci termin v rozmezi od - do 
	 * @param od
	 * @param do_
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet vypisExpedovanychKusuOdDo(Date od, Date do_) throws SQLException{
		if(od == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum od nesmí být prázdné");
			return null;
		}
		if(do_ == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum do nesmí být prázdné");
			return null;
		}
		int i = 5, j = 8;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		java.sql.Date pomDate = new java.sql.Date (od.getTime());
		c.setDate("od", pomDate);
		pomDate = new java.sql.Date (do_.getTime());
		c.setDate("do_", pomDate);
		rs = c.executeQuery();
		return rs;
	}
	
	/**
	 * Vypise vysechny kusy co jsou zmetek = false, vycisteno = true odlito = true, expedovano = false, uzavreno = false
	 * @return
	 * @throws SQLException 
	 */
	public Statement vypisSkladuKDnesnimuDni() throws SQLException{
		int i = 5, j = 9;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.execute();
		return c;
	}
	
	public ResultSet vypisZmetky(Date od, Date do_) throws SQLException{
		if(od == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum od nesmí být prázdné");
			return null;
		}
		if(do_ == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum do nesmí být prázdné");
			return null;
		}
		int i = 5, j = 12;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		java.sql.Date pomDate = new java.sql.Date (od.getTime());
		c.setDate("od", pomDate);
		pomDate = new java.sql.Date (do_.getTime());
		c.setDate("do_", pomDate);
		rs = c.executeQuery();
		return rs;
	}
	
	public Statement vypisVinikyVKgKc(Date od, Date do_) throws SQLException{
		if(od == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum od nesmí být prázdné");
			return null;
		}
		if(do_ == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Datum do nesmí být prázdné");
			return null;
		}
		int i = 5, j = 13;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		java.sql.Date pomDate = new java.sql.Date (od.getTime());
		c.setDate("od", pomDate);
		pomDate = new java.sql.Date (do_.getTime());
		c.setDate("do_", pomDate);
		c.execute();
		return c;
	}
	
	public ResultSet vypisVytizeniKapacit(int cisloTydne, int rok, int kapacita_M, int kapacita_S, int kapacita_T) throws SQLException{
		if(cisloTydne < 1 || cisloTydne > 53){
			JOptionPane.showMessageDialog(hlavniOkno, "Zadejte èíslo týdne musí být v rozmezí 1 - 53");
			return null;
		}
		if(rok < 1800 || rok > 9999){
			JOptionPane.showMessageDialog(hlavniOkno, "Rok musí být v rozmezí 1800 - 9999. (SQLStor.java 820)");
		}
		if(kapacita_M <= 0 || kapacita_S <= 0 || kapacita_T <=0){
			JOptionPane.showMessageDialog(hlavniOkno, "Kapacity obsahují èísla menší nebo rovno nule");
			return null;
		}
		int i = 5, j = 16;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, rok);
		cal.set(Calendar.WEEK_OF_YEAR, cisloTydne);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		java.util.Date datum = cal.getTime();
		java.sql.Date pomDate = new java.sql.Date (datum.getTime());
		c.setDate(1, pomDate);
		cal.add(Calendar.WEEK_OF_YEAR, POCET_TYDNU_VYTIZENI_KAPACIT); // na 10 tydnu se bude dìlat
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		datum = cal.getTime();
		pomDate = new java.sql.Date (datum.getTime());
		c.setDate(2, pomDate);
		c.setInt("kapacita_M", kapacita_M);
		c.setInt("kapacita_S", kapacita_S);
		c.setInt("kapacita_T", kapacita_T);		
		rs = c.executeQuery();
		return rs;
	}
	
	public void zadejCisloTavbyCisloFakturyTeplotuLiti(int idKusu, String cisloTavby, String cisloFaktury) throws SQLException{
		if(idKusu  < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id fyz. kusu je špatnì zapsaný");
			return;
		}
		if(cisloTavby != null){
			if(cisloTavby.length() > maxDelkaCislaTavby){
				JOptionPane.showMessageDialog(hlavniOkno, "Èíslo tavby mùže mít maximálnì "+maxDelkaCislaTavby+" znakù");
				return;
			} else if(cisloTavby.equalsIgnoreCase("")){
				cisloTavby = null;
			}
		}
		if(cisloFaktury != null){
			if(cisloFaktury.length() > maxDelkaCislaFaktury){
				JOptionPane.showMessageDialog(hlavniOkno, "Èíslo faktury mùže mít maximálnì "+maxDelkaCislaFaktury+" znakù");
				return;
			} else if(cisloFaktury.equalsIgnoreCase("")){
				cisloFaktury = null;
			}
		}
		
		int i = 3, j = 0;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt("idKusu", idKusu);
		if(cisloTavby == null){
			c.setNull("cisloTavby", java.sql.Types.VARCHAR);
		} else {
			c.setString("cisloTavby", cisloTavby);
		}
		if(cisloFaktury == null){
			c.setNull("cisloFaktury", java.sql.Types.VARCHAR);
		} else {
			c.setString("cisloFaktury", cisloFaktury);
		}
		c.execute();
	}
	
	public void zadejDilciTermin(int idZakazky, Date datum, int kolik, boolean isSplneno) throws SQLException{
		if(idZakazky  < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id fyz. kusu je špatnì zapsaný");
			return;
		}
		if(datum == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Dílèí termín nesmí být null");
			return;
		}
		if(kolik < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Poèet kusù nesmí být záporný");
			return;
		}
		int i = 3, j = 4;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky);
		java.sql.Date sqlDate = new java.sql.Date(datum.getTime());
		c.setDate(2, sqlDate);  // dilèí termín 
		c.setInt(3, kolik);
		c.setBoolean(4, isSplneno);
		c.execute();
	}
	
	/**
	 * 
	 * @param idKusu
	 * @param datumLiti
	 * @throws SQLException
	 */
	public void zadejPlanovanyDatumLiti(int idKusu, Date datumLiti) throws SQLException{
		if(idKusu < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id fyz. kusu je špatnì zapsaný");
			return;
		}
		int i = 3, j = 1;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idKusu);
		if(datumLiti == null) {
			c.setNull(2, java.sql.Types.DATE);		
		}else {
			java.sql.Date sqlDate = new java.sql.Date(datumLiti.getTime());
			c.setDate(2, sqlDate);  // datumZakazky 
		}
		c.execute();
	}
	
	/**
	 * 
	 * @param idFyzKusu
	 * @param isOdlito
	 * @param isVycisteno
	 * @throws SQLException 
	 */
	public void zadejOdlitek(int idFyzKusu, boolean isOdlito, Date datumOdliti, boolean isVycisteno, Date datumVycisteni, boolean isExpedovano,
			Date datumExpedice, boolean isZmetek, Date datumZadaniZmetku, String cisloTavby, String cisloFaktury, String teplotaLiti) throws SQLException{
		if(idFyzKusu < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id fyz. kusu je špatnì zapsaný");
			return;
		}
		if(cisloTavby != null){
			if(cisloTavby.length() > 10){
			JOptionPane.showMessageDialog(hlavniOkno, "Èíslo tavby nesmí být delší než 10 znakù");
			return;
			}
		}
		if(cisloFaktury != null){
			if(cisloFaktury.length() >= 19){
				JOptionPane.showMessageDialog(hlavniOkno, "Èíslo tavby nesmí být delší než 19 znakù");
				return;
			}
		}
		
		int i = 3, j = 2;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		
		c.setInt(1, idFyzKusu);
		c.setBoolean(2, isOdlito);
		if(datumOdliti == null){
			c.setNull(3, java.sql.Types.DATE);
		} else {
			c.setDate(3, new java.sql.Date(datumOdliti.getTime()));
		}
		c.setBoolean(4, isVycisteno);
		if(datumVycisteni == null){
			c.setNull(5, java.sql.Types.DATE);
		}  else {
			c.setDate(5, new java.sql.Date(datumVycisteni.getTime()));
		}
		c.setBoolean(6, isExpedovano);
		if(datumExpedice == null){
			c.setNull(7, java.sql.Types.DATE);
		} else {
			c.setDate(7, new java.sql.Date(datumExpedice.getTime()));
		}
		c.setBoolean(8, isZmetek);
		if(datumZadaniZmetku == null){
			c.setNull(9, java.sql.Types.DATE);
		} else {
			c.setDate(9, new java.sql.Date(datumZadaniZmetku.getTime()));
		}
		if(cisloTavby == null){
			c.setNull(10, java.sql.Types.VARCHAR);
		} else if(cisloTavby.equalsIgnoreCase("")){
			c.setNull(10, java.sql.Types.VARCHAR);
		} else {
			c.setString(10, cisloTavby);
		}
		if(cisloFaktury == null){
			c.setNull(11, java.sql.Types.VARCHAR);
		} else if(cisloFaktury.equalsIgnoreCase("")){
			c.setNull(11, java.sql.Types.VARCHAR);
		} else {
			c.setString(11, cisloFaktury);
		}
		
		if(teplotaLiti == null){
			c.setNull(12, java.sql.Types.VARCHAR);
		} else if(teplotaLiti.equalsIgnoreCase("")){
			c.setNull(12, java.sql.Types.VARCHAR);
		} else {
			c.setString(12, teplotaLiti);
		}
		
		c.execute();
	}
	
	/**
	 * Doplni udaje o zmetku, tzn. doplni vinika a vadu
	 * @throws SQLException 
	 */
	public boolean zadejVadyZmetku(int idFyzKusu, int idVinika, int idVady) throws SQLException{
		int i = 3, j = 3;
		if(idFyzKusu <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Špatnì Id fyzického kusu");
			return false;
		}
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idFyzKusu);
		if(idVinika <= 0){
			c.setNull(2, java.sql.Types.INTEGER);
		} else{
			c.setInt(2, idVinika);

		}
		if(idVady <= 0){
			c.setNull(3, java.sql.Types.INTEGER);
		} else {
			c.setInt(3, idVady);
		}
		
		c.registerOutParameter(4, java.sql.Types.TINYINT);
		c.executeQuery();
		boolean pom = c.getBoolean(4);
		return pom;
	}
	
	/**
	 * 
	 * @param vinik
	 * @throws SQLException
	 */
	public void pridejVinika(String vinik) throws SQLException{
		if(vinik == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Vinik je prazdny");
			return;
		}else if(vinik.length() > maxDelkaVinika || vinik.equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(hlavniOkno, "Vinik je moc dlouhý nebo je prázdný");
			return;
		}
		sklad.setVadyVinici(new String [2][]); // vinici se automaticky aktualizuou pro zadavani zmetku/odlitku
		int i = 4, j = 0;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setString(1, vinik);
		c.execute();
	}
	
	public void upravVinika(int idVinika, String vinik) throws SQLException{
		if(vinik == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Vinik je prazdny");
			return;
		}else if(vinik.length() > maxDelkaVinika || vinik.equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(hlavniOkno, "Vinik je moc dlouhý nebo je prázdný");
			return;
		}
		if(idVinika <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id viníka je spatnì");
			return;
		}
		sklad.setVadyVinici(new String [2][]); // vinici se automaticky aktualizuou pro zadavani zmetku/odlitku
		int i = 4, j = 8;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idVinika);
		c.setString(2, vinik);
		c.execute();
	}
	
	/**
	 * 
	 * @param vada
	 * @throws SQLException
	 */
	public void pridejVadu(String vada) throws SQLException{
		if(vada == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Vada je prázdná");
			return;
		}else if(vada.length() > maxDelkaVady|| vada.equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(hlavniOkno, "Vada je moc dlouhá nebo je prázdná");
			return;
		}
		sklad.setVadyVinici(new String [2][]); // vady se automaticky aktualizuou pro zadavani zmetku/odlitku
		int i = 4, j = 1;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];	
		c.setString(1, vada);
		c.execute();
	}
	
	public void upravVadu(int idVady, String vada) throws SQLException{
		if(vada == null){
			JOptionPane.showMessageDialog(hlavniOkno, "Vada je prázdná");
			return;
		}else if(vada.length() > maxDelkaVady|| vada.equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(hlavniOkno, "Vada je moc dlouhá nebo je prázdná");
			return;
		}
		if(idVady <= 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id vady je spatnì");
			return;
		}
		sklad.setVadyVinici(new String [2][]); // vady se automaticky aktualizuou pro zadavani zmetku/odlitku
		int i = 4, j = 9;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];	
		c.setInt(1, idVady);
		c.setString(2, vada);
		c.execute();
	}
	
	/**
	 * 
	 * @param idZakazky
	 * @return
	 * @throws SQLException
	 */
	public TransClass planovaniRozvrh(int idZakazky) throws SQLException{
		int i = 4, j = 2;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky);
		c.registerOutParameter(2, java.sql.Types.INTEGER);
		rs = c.executeQuery();
		int pocet = c.getInt(2);
		TransClass ts = new TransClass(pocet,rs);
		return ts;
	}
	
	/**
	 * 
	 * @param idZakazky
	 * @return
	 * @throws SQLException
	 */
	public TransClass planovaniRozvrhVycisteno(int idZakazky) throws SQLException{
		int i = 4, j = 4;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky);
		c.registerOutParameter(2, java.sql.Types.INTEGER);
		rs = c.executeQuery();
		int pocet = c.getInt(2);
		TransClass ts = new TransClass(pocet,rs);
		return ts;
	}
	
	/**
	 * 
	 * @param cisloTydne
	 * @param rok
	 * @param formovna
	 * @return
	 * @throws SQLException
	 */
	public ResultSet kapacitniPropocet(int cisloTydne, int rok, String formovna) throws SQLException{
		int i = 4, j = 5;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		if(formovna.length() != 1){
			JOptionPane.showMessageDialog(hlavniOkno, "V kapacitnim propoètu je chyba, kapacitniPropocet() SQLStor.java");
			return null;
		}
		Calendar pom = Calendar.getInstance();
		pom.set(Calendar.DAY_OF_WEEK, 2);
		pom.set(Calendar.YEAR, rok);
		pom.set(Calendar.WEEK_OF_YEAR, cisloTydne);
		pom.set(Calendar.DAY_OF_WEEK, 2);
		java.sql.Date sqlDate = new java.sql.Date(pom.getTime().getTime()); 
		c = cst[i][j];
		c.setDate(1, sqlDate);
		c.setString(2, formovna);
		rs = c.executeQuery();
		return rs;
	}
	
	/**
	 * 
	 * @param idZakazky
	 * @return null pokud je cislo zakazky zadáno špatnì jinak resultset s dílèími termíny
	 * @throws SQLException
	 */
	public ResultSet vyberDilciTerminy(int idZakazky) throws SQLException{
		if(idZakazky < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zakázky nemùže být zaporné èíslo");
			return null;
		}
		int i = 6, j = 2;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky);
		rs = c.executeQuery();
		return rs;
	}
	
	/**
	 * 
	 * @param idZakazky
	 * @return Vratí ResultSet se seznamem zákazníku vèetnì jejich dílèích termíny
	 * @throws SQLException
	 */
	public ResultSet vyberDilciTerminySeJmeny(int idZakazky) throws SQLException{
		if(idZakazky < 0){
			JOptionPane.showMessageDialog(hlavniOkno, "Id zakázky nemùže být zaporné èíslo");
			return null;
		}
		int i = 6, j = 3;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky);
		rs = c.executeQuery();
		return rs;
	}
	
	/**
	 * Vratí seznam všech vlastním materiálù v databázi.
	 * @return ResultSet seznam materialu
	 * @throws SQLException
	 */
	public ResultSet vyberVlastniMaterialy() throws SQLException{
		int i = 2;
		CallableStatement s = conn.prepareCall(vadyVinici[i]);
		rs = s.executeQuery();
		return rs;
	}
	
	/**
	 * Pokusí se vyslat žádost o uzavøení zakázky na server. Pokud se to podaøí využije globální
	 * promìné hlavniOkno, která odkazuje na Hlavní JFrame a zobrazí pomocí  {@code JOptionPane.showMessageDialog(.,.);} 
	 * zprávu o úspìšném èi neúspìšném uzavøení zakázky.
	 * @param idZakazky ID zakázky, kterou chceme uzavøít.
	 * @return  {@code true} pokud zakázka byla uzavøena, {@code false} pokud nebyla
	 * @throws SQLException 
	 */
	public boolean uzavriZakazku(int idZakazky) throws SQLException{
		int i = 4, j = 6;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky);
		c.registerOutParameter(2, java.sql.Types.INTEGER);
		c.registerOutParameter(3, java.sql.Types.INTEGER);
		c.registerOutParameter(4, java.sql.Types.INTEGER);
		c.registerOutParameter(5, java.sql.Types.INTEGER);
		c.registerOutParameter(6, java.sql.Types.INTEGER);
		c.registerOutParameter(7, java.sql.Types.INTEGER);
		
		ResultSet rs = c.executeQuery();
		
		int pocetOdlitych, pocetVycistenych, pocetExpedovanych, pocetNeodlitych, pocetZmetkuBezVady, pocetNedokoncenychDilcichTerminu, objednanoKs;
		pocetOdlitych = c.getInt(2);
		pocetVycistenych = c.getInt(3);
		pocetExpedovanych = c.getInt(4);
		pocetNeodlitych = c.getInt(5);
		pocetZmetkuBezVady = c.getInt(6);
		pocetNedokoncenychDilcichTerminu = c.getInt(7);
		objednanoKs = c.getInt(8);
		JOptionPane.showMessageDialog(hlavniOkno, "Objednáno: "+objednanoKs+" Poèet odlitých: "+pocetOdlitych+ " Poèet vyèištìných: "+pocetVycistenych+
				" Poèet expedovaných: "+pocetExpedovanych+" Poèet neodlitých: "+pocetNeodlitych + 
				" Poèet kusù bez uvedené vady: "+pocetZmetkuBezVady + " Poèet nedokonèených dílèích termínù: "+ pocetNedokoncenychDilcichTerminu);
		
		boolean uspech = false;
		if(rs.first()){
			uspech = rs.getBoolean(1);
		}
		rs.close();
		if(uspech){
			JOptionPane.showMessageDialog(hlavniOkno, "Zakázka byla úspìšnì uzavøena");
		} else {
			JOptionPane.showMessageDialog(hlavniOkno, "Zakázka nebyla uzavøena");
		}
		return uspech;
	}
	
	/**
	 * Metoda pro obnovení zakázky z archivu
	 * @param idZakazky èíslo dané zakázky
	 * @return true pokud se podaøilo jinak false
	 * @throws SQLException
	 */
	public boolean obnovZakazku(int idZakazky) throws SQLException{
		int i = 4, j = 7;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idZakazky);
		
		ResultSet rs = c.executeQuery();
		boolean uspech = false;
		if(rs.first()){
			uspech = rs.getBoolean(1);
		}
		rs.close();
		if(uspech){
			JOptionPane.showMessageDialog(hlavniOkno, "Zakázka byla úspìšnì obnovena");
		}
		return uspech;
	}
	
	/**
	 * Metoda která nám ovìøí, že cena v èeských korunách je menší než je daný limit. (cca 1 miliarda)
	 * @param isCzk zda je cena v kè nebo eurech, vypisuje upozornìní na hlavní okno pokud je kurz špatnì
	 * @param kurz pøípadný kurz z eu na czk
	 * @param cena cena za produkt
	 * @return true pokud je vše v rozmezí, jinak false
	 */
	private boolean overCenu(boolean isCzk, double kurz, double cena){
		if(cena <= 0)return false;
		if(isCzk){
			if(cena > maxCena){
				return false;
			}
		} else {
			if(kurz <= 0){
				JOptionPane.showMessageDialog(hlavniOkno, "Kurz");
				return false;
			}
			else {
				if(cena * kurz >= maxCena){
					return false;
				}
			}
		}
		return true;
	}
	
	public Statement zalohaDB() throws SQLException{
		int i = 8, j = 0;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		
		c.execute();
		return c;
	}
	
	public static final int obnovaUspech = 0;
	public static final int obnovaMyWindowWaring = 1;
	public static final int obnovaErrorWin = 2;
	
	/**
	 * Myslim že hotovo, ted už jen otestovat.
	 * @param obnovDBSqlFile
	 * @param seznamZakazekCSVFile
	 * @return
	 * @throws Exception
	 */
	public int obnovaDB(File obnovDBSqlFile, File seznamZakazekCSVFile) throws Exception{
		// 1. overime že mame všechny soubory
		System.out.println(obnovDBSqlFile + " lol "+ seznamZakazekCSVFile);
		String [] tables = {"zakaznici","vady","vinici", "seznam_modelu", "seznam_zakazek", "dilci_terminy", "fyzkusy", "zmetky_vady"};
		String parentDir = seznamZakazekCSVFile.getParent();
		String date = getDateFromName(seznamZakazekCSVFile.getName());
		
		if(date == null) throw new Exception("špatny název souboru");
		
		for(int i = 0; i < tables.length; i++){
			String path = parentDir + "\\"+ date +"_"+ tables[i] + ".csv";
			File pom = new File(path);
			if(!pom.isFile()){
				JOptionPane.showMessageDialog(hlavniOkno, "Soubor "+path+" nebyl nalezen");
				return obnovaMyWindowWaring;
			}
		}
		
		// 2. spustíme skript pro smazání, resp vyprazdnìní DB
		try{
			ScriptRunner sr = new ScriptRunner(this.conn, false, true);	
			sr.setLogWriter(null);
			sr.runScript(new InputStreamReader(new FileInputStream(obnovDBSqlFile), "UTF-8"));
		} catch (Exception e) {
			if(e.getLocalizedMessage().startsWith("Access denied for user")){
				JOptionPane.showMessageDialog(hlavniOkno, "Na tuto operaci nemate pravomoce");
				return obnovaMyWindowWaring;
			} else {
				ExceptionWin.showExceptionMessage(e);
				return obnovaErrorWin;
			}
		}
		// 3. nahrajeme data pomocí Load data infile
		String format =
				"LOAD DATA LOCAL INFILE '"+ seznamZakazekCSVFile.getParent().replace("\\", "\\\\")+ "\\\\"
				+ "%s_%s.csv' \n"
				+ "INTO TABLE pomdb.%s \n"
				+ "CHARACTER SET 'cp1250' \n"
				+ "FIELDS TERMINATED BY ',' ENCLOSED BY '\"' \n"
				+ "LINES TERMINATED BY '\\n';";
		System.out.println(format);
		String querry = null;
		Statement st = this.conn.createStatement();
		
		// querry = String.format(format, "30_06_2015", "vinici", "vinici");
		for(int i = 0; i < tables.length; i++){
			querry = String.format(format, date, tables[i], tables[i]);
			//System.out.println(querry);
			st.executeQuery(querry);
		}
		st.close();
		// done
		return obnovaUspech;
	}
	
	private String getDateFromName(String name){
		// vzor "30_06_2015_vinici"
		Pattern pattern = Pattern.compile("(\\d{4}_\\d{2}_\\d{2})");
		Matcher matcher = pattern.matcher(name);
		if (matcher.find())
		{
		    return matcher.group();
		}
		return null;
	}
	
	/**
	 * Metoda, která vytvoøí uživatele, pokud již neexistuje, a pøidìlí mu daná privilegia dle cisla povolani.
	 * @param jmeno uzivatele
	 * @param host ip adresa uzivatele
	 * @param heslo heslo uzivatele
	 * @param cisloPovolani cislo povolani
	 * @throws SQLException pokud nemate opravnìní nebo nespecifikovana SQL chyba
	 */
	public void pridejUzivateleSPrivilegii(String jmeno, String host, String heslo, int cisloPovolani) throws SQLException{
		final String acesDenied = "execute command denied to user";
		jmeno = escapeSQL(jmeno);
		host = escapeSQL(host);
		heslo = escapeSQL(heslo);
		String querry = "create USER \'"+jmeno+"\'@\'"+host+"\' IDENTIFIED BY \'"+heslo+"\'";
		Statement st = this.conn.createStatement();
		try {
			st.execute(querry); // vytvoreni uzivatele
			st.close();
		} catch(SQLException e){
			st.close();
			if(e.getLocalizedMessage().startsWith(acesDenied)){ // nemame opravneni
				throw e;
			} else if (e.getLocalizedMessage().startsWith("Operation CREATE USER failed for")){
				// v poradku, uzivatel už existuje
			} else {
				throw e;
			}
		}
		
		
		// prirazeni privilegii novemu uzivateli
		int i = 8, j = 1;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		String m = sqlPrikazy[i][j];
		c = cst[i][j];
		c.setString(1, jmeno);
		c.setString(2, host);
		c.setInt(3, cisloPovolani);
		
		c.execute();
	}
	
	/**
	 * <p>Metoda pro smazani jednoho fyzického kusu v databázi</p>
	 * Statusy pro vraceni:
	 * <ol>
	 * 	<li>Uspesny prubeh</li>
	 * <ol>
	 * @param idKusu id fyzického kusu
	 * @return daný status smazani
	 * @throws SQLException 
	 */
	public int smazFyzickyKus(int idKusu) throws SQLException{
		int i = 7, j = 0;
		if(cst[i][j] == null){
			cst[i][j] = conn.prepareCall(sqlPrikazy[i][j]);
			naposledyPouzito[i][j] = new Date();
		}
		c = cst[i][j];
		c.setInt(1, idKusu);
		c.execute();
		int rst = c.getInt(2);
		if(rst== USPECH){
			return USPECH;
		} else if (rst == SQLNULL){
			return SQLNULL;
		} else {
			return SPATNEID;
		}
	}
	
	
	/**
	 * Zavøe veškeré spojení s databází
	 * @throws SQLException
	 */
	public void closeConnections() throws SQLException {
		for (int i = 0; i < cst.length; i++) {
			for (int j = 0; j < cst[i].length; j++) {
				if (cst[i][j] != null){
					cst[i][j].close();
					cst[i][j] = null;
				}
			}
		}
		if(conn != null){
			conn.close();
			conn = null;
		} else{
			JOptionPane.showMessageDialog(hlavniOkno, "Spojeni s databazí už bylo døíve pøerušeno");
		}
		this.casovac.stop();
	}
	
	
	private class PosluchacCasovace implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	Date tedDate = new Date();
        	System.out.println(tedDate.getTime());
        	System.out.println("mažu Callable statement");
        	for(int i = 0; i < naposledyPouzito.length; i++){
    			for(int j = 0; j < naposledyPouzito[i].length; j++){
    				if(naposledyPouzito[i][j] == null)continue;
    				System.out.println("zkousim");
    				if(tedDate.getTime() - naposledyPouzito[i][j].getTime() > dobaNaZavrenyPripojeni){
    					try {
							cst[i][j].close();
							cst[i][j] = null;
							System.out.println("Callable statement "+i+" " +j+" zrušeno");
							naposledyPouzito[i][j] = null;
						} catch (SQLException e1) {
							ExceptionWin.showExceptionMessage(e1); 
						}
    				}
    			}
    		}
        }
    }

	/**
	 * Metoda, kterou jsem stahnul na netu pro escapovani SQL pøíkazù.
	 * @param s string ktery chceme escapovat a vlozit do SQL pøíkazu
	 * @return escapovany string
	 */
	public static String escapeSQL(String s) {
		int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\\':
			case '\"':
			case '\'':
			case '\0': {
				newLength += 1;
			}
				break;
			}
		}
		if (length == newLength) {
			// nothing to escape in the string
			return s;
		}
		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\\': {
				sb.append("\\\\");
			}
				break;
			case '\"': {
				sb.append("\\\"");
			}
				break;
			case '\'': {
				sb.append("\\\'");
			}
				break;
			case '\0': {
				sb.append("\\0");
			}
				break;
			default: {
				sb.append(c);
			}
			}
		}
		return sb.toString();
	}

}
