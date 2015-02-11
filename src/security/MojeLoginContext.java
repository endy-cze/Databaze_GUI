package security;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class MojeLoginContext extends LoginContext {

	public MojeLoginContext(String arg0, CallbackHandler arg1)
			throws LoginException {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	public void pom(){
		try {
			this.login();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
