package sablony.storage;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class DateStor extends JCheckBox implements Comparable<DateStor>{
	private SimpleDateFormat sdf = null;
	private Date datum;
	private int pocetKusu;
	private boolean puvHodnotaCompleted;

	public DateStor(Date datum, int pocetKusu, SimpleDateFormat sdf, boolean isCompleted){
		this.datum = datum;	
		this.pocetKusu = pocetKusu;
		this.sdf = sdf;
		this.setSelected(isCompleted);
		this.puvHodnotaCompleted = isCompleted;
		this.setText(toString());
	}
	
	public String toString(){
		String date = null;
		date = " "+sdf.format(datum)+ " kusù: "+pocetKusu;
		return date;
	}
	
	public Date getDate(){
		return this.datum;
	}
	
	public int getPocetKusu(){
		return this.pocetKusu;
	}
	
	public boolean isEqualDate(DateStor date){
		String tohle = sdf.format(this.datum);
		String druhe = sdf.format(date.getDate());
		return tohle.equalsIgnoreCase(druhe);
	}
	
	public boolean isZmeneno(){
		if(puvHodnotaCompleted != this.isSelected()){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(DateStor o) {
		if(this.isEqualDate(o)){
			return 0;
		}
		boolean pom = this.datum.after(o.getDate());
		if(pom){
			return 1;
		} else {
			return -1;
		}
	}
}
