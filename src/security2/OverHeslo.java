package security2;
import javax.swing.JFrame;

import app.MainFrame;
import app.ProgresBarFrame;
import security.conect.CreateConectionToMySQL;

public class OverHeslo {
	private CreateConectionToMySQL connToMysql;
	private MainFrame hlavniOkno = null;
	
	public OverHeslo(JFrame okno, ProgresBarFrame mujBar, String url){
		connToMysql = new CreateConectionToMySQL(okno, mujBar, url);
	}
	
	public boolean over2(String jmeno, char [] heslo){
		hlavniOkno = connToMysql.getMainFrame(jmeno, heslo);
		if(hlavniOkno != null){
			heslo = null;
			return true;
		}
		heslo = null;
		return false;
	}
	
	public MainFrame getHlavniOkno(){
		return this.hlavniOkno;
	}
}
