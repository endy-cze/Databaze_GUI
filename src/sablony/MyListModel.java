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
		boolean isCompleted;
		if(pocetSloupcu == 3){
			while(rs.next()){
				datum = rs.getDate(1);
				pocetKusu = rs.getInt(2);
				isCompleted = rs.getBoolean(3);
				this.addElement(new DateStor(datum, pocetKusu, sdf, isCompleted));			
			}
		} else {
			throw new SQLException("Špatný ResultSet");
		}
	}

}
