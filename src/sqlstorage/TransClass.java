package sqlstorage;

import java.sql.ResultSet;

public class TransClass {
	public int  pocetNenaplanovanychKusu;
	public ResultSet rs;
	
	public TransClass(int  pocetNenaplanovanychKusu,ResultSet rs){
		this.pocetNenaplanovanychKusu = pocetNenaplanovanychKusu;
		this.rs = rs;
	}

	
}
