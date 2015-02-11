package security;

import java.sql.Connection;
import java.util.Arrays;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.swing.JFrame;

import sablony.errorwin.ExceptionWin;

public class CheckUser {
	private Connection conn;
	

	public boolean trololo(JFrame okno,String jmeno, char [] heslo){
		System.setProperty("java.security.auth.login.config", "jaas.config");
		LoginContext lc = null;
		try {
			lc = new LoginContext("Myjaas", new MyCallBackHandler(okno,jmeno,heslo));			
		} catch (LoginException e) {
			e.printStackTrace();
		}
		
		try {
			lc.login();
			Arrays.fill(heslo,'0');
			return true;
		} catch (LoginException e) {
			ExceptionWin ex = new ExceptionWin(e);
		}
		return false;
	}
	
	public Connection getConnection(){
		return this.conn;
	}
	
	

}
