package security;

import java.io.IOException;
import java.util.Arrays;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.JFrame;

public class MyCallBackHandler implements CallbackHandler {
	private JFrame okno = null;
	private String jmeno;
	private char [] heslo;
	
	public MyCallBackHandler(JFrame okno,String jmeno, char [] heslo){
		this.okno = okno;
		this.heslo = heslo;
		this.jmeno = jmeno;
		heslo = null;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		NameCallback nameCallback = null;
		PasswordCallback passCallback = null;
		
		for(int i = 0; i < callbacks.length; i++){
			if (callbacks[i] instanceof NameCallback){
				nameCallback = (NameCallback) callbacks[i];
				nameCallback.setName(jmeno);
				jmeno = null;
				System.gc();
				
			} else if (callbacks[i] instanceof PasswordCallback) {
				passCallback = (PasswordCallback) callbacks[i]; 
				passCallback.setPassword(heslo);
				heslo = null;
				System.gc();
			}
			
		}

	}
}
