package sablony;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tøída slouící pro záznamu zmìny ve fyzické tabulky pro Planování a potom se pouije
 *  pro generování rozvrhu v tøíde <code>Planovani</code>.
 * @author Havlicek
 *
 */
public class ZmenaHodnoty {
	
	private String puvHodn;
	private String novaHodn;
	private int radek;
	private Date novaHodnDate;
	private Date staraHodnDate;
	
	/**
	 * Novı záznam zmìny
	 * @param novaHodn nová hodnota <code>String</code>, dle formatu "dd.MM.yyyy", nekontroluje se asi by mìlo
	 * @param puvHodn pùvodní hodnota <code>String</code>, dle formatu "dd.MM.yyyy", nekontroluje se
	 * @param radek øádek kam se nová hodnota umísuje v tabulce <code>tableFyzkusy</code> v <code>Planovani</code>
	 * @throws ParseException 
	 */
	public ZmenaHodnoty(String novaHodn, String puvHodn, int radek, SimpleDateFormat sdf) throws ParseException{
		this.novaHodn = novaHodn;
		this.puvHodn = puvHodn;
		this.radek = radek;
		this.novaHodnDate = sdf.parse(novaHodn);
		try{
			this.staraHodnDate = sdf.parse(puvHodn);
		} catch (ParseException e){
			this.staraHodnDate = null;
		}
		
	}

	public String getPuvHodn() {
		return puvHodn;
	}

	public String getNovaHodn() {
		return novaHodn;
	}

	public int getRadek() {
		return radek;
	}
	
	public Date getDate(){
		return this.novaHodnDate;
	}
	
	public Date getOldDate(){
		return this.staraHodnDate;
	}
	
}
