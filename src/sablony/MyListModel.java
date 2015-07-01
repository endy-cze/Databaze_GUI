package sablony;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;

import sablony.storage.DateStor;

public class MyListModel extends DefaultListModel<DateStor> {
	
	public MyListModel(ResultSet rs, SimpleDateFormat sdf) throws SQLException{
		super();
		ResultSetMetaData meta = rs.getMetaData();
		int pocetSloupcu = meta.getColumnCount();
		DateStor tmp = null;
		Date datum;
		int pocetKusu;
		if(pocetSloupcu == 2){
			while(rs.next()){
				datum = rs.getDate(1);
				pocetKusu = rs.getInt(2);
				tmp = new DateStor(datum, pocetKusu, sdf);
				this.addElement(tmp);
			}
		} else {
			throw new SQLException("Špatný ResultSet");
		}
	}

}
