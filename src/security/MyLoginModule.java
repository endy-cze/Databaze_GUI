package security;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.swing.JFrame;

import security.conect.CreateConectionToMySQL;


public class MyLoginModule implements LoginModule {
	private CallbackHandler cbh;
	private boolean connected = false;
	private Connection conn;
	private Subject sbj;
	
	@Override
	public boolean abort() throws LoginException {
		try {
			this.conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean commit() throws LoginException {
		return connected;
	}

	@Override
	public void initialize(Subject arg0, CallbackHandler cbh,
			Map<String, ?> arg2, Map<String, ?> arg3) {
		this.cbh = cbh;
		this.sbj = arg0;
	}

	@Override
	public boolean login() throws LoginException {
		Callback[] callBackArray = new Callback[2];
		callBackArray[0] = new NameCallback("User name");
		callBackArray[1] = new PasswordCallback("Password", false);
		
		try {
			cbh.handle(callBackArray);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedCallbackException e) {
			e.printStackTrace();
		}
		String name = ((NameCallback) callBackArray[0]).getName();
		char [] pass = ((PasswordCallback) callBackArray[1]).getPassword();
		
		
		
		/*
		 * Bulshit k nicemu Jframe zrusit
		 * jen kvuli tomu bay to nepsalo error
		 */
		JFrame pom = new JFrame();
		//CreateConectionToMySQL conn = new CreateConectionToMySQL(pom);
		CreateConectionToMySQL conn = null;
		Connection mysqlConn = null;
		this.conn = mysqlConn;
		if(mysqlConn != null){
			connected= true;
			//security
			Arrays.fill(pass,'0');
		} else {
			connected= false;	
			//security
			Arrays.fill(pass,'0');	
			throw new FailedLoginException("Authentitacion failed...");
		}
		
		return connected;
	}

	@Override
	public boolean logout() throws LoginException {
		try {
			this.conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
