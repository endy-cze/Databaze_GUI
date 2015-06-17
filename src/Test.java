import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import sablony.ZmenaHodnoty;
import sablony.tabulka.ColorCellTable;


public class Test {

	private static final int CTVRTEK = 4;
	private static final int hodinaDriv = 3;
	private static final int hodinaPozdeji = 8;

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		

	}

	private int radekTydnuPredExistujiciRozvrh(ZmenaHodnoty [] zmenyHodnot, ColorCellTable tableGenericka, Calendar zjistiRokDatum){
		Calendar prvniNoveDatum = (Calendar) zjistiRokDatum.clone();
		Calendar prvniDatumZTabulky = zjistiRokDatum;
		
		if(zmenyHodnot[0] == null){return 0;}// je to seøazene, takže pokud je prvni null, null jsou všechny
		if(tableGenericka.getRowCount() <= 0){return 0;} // tabulka genericka je prazdna
		
		int pocetTydnu = 0;
		
		// nastaveni prvniho datumu z generické tabulky
		int rokPrvniRadka = Integer.parseInt((String)tableGenericka.getValueAt(0, 12)); // prvni rok v rozvrhu
		int tydenPrvniRadka = Integer.parseInt((String)tableGenericka.getValueAt(0, 1)); // prvni tyden v rozvrhu
		this.set(prvniDatumZTabulky, rokPrvniRadka, tydenPrvniRadka, CTVRTEK, hodinaDriv);
		
		// nastaveni prvniho noveho(zmeneneho) data
		prvniNoveDatum.setTime(zmenyHodnot[0].getDate()); // prvni nove upravene datum
		this.set(prvniNoveDatum, prvniNoveDatum.get(Calendar.YEAR), prvniNoveDatum.get(Calendar.WEEK_OF_YEAR), CTVRTEK, hodinaPozdeji);

		if(prvniNoveDatum.compareTo(prvniDatumZTabulky) > 0 ){ // pokud je nove datum dele nez prvni datum z genericke tabulky je to true 
			return 0; // prvni nove datum je po prvním datu generické tabulky. Nebudeme generovat øady pøed.
		}
		
		while(prvniNoveDatum.compareTo(prvniDatumZTabulky) > 0 ){
			pocetTydnu++;
			prvniNoveDatum.add(Calendar.WEEK_OF_YEAR, 1);
		}
		
		return pocetTydnu;
	}
	
	private Calendar set(Calendar cal, int year, int weekOfYear, int dayOfTheWeek, int hour){
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK, dayOfTheWeek);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal;
	}

}
