package sConect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import app.MainFrame;
import app.ProgresBarFrame;
import sablony.errorwin.ExceptionWin;

/**
 * Tøída pro vytváøení spojení mezi databází. Pomocí této tøídy získáme tøídu java.sql.Connection pro komnikaci s databází
 * @author Ondøej Havlíèek
 *
 */
public class CreateConectionToMySQL implements ActionListener {
	private final String url;
	private final String driver = "com.mysql.jdbc.Driver";
	private final String spatneHeslo= "Access denied for user";
	private final String neniPripojeni= "Communications link failure";
	private JFrame loginWindow;
	private ProgresBarFrame prgbarFrame;
	private Task t;
	private static final String cancelZprava = "java.util.concurrent.CancellationException";
	
	/**
	 * Konstruktor, vyplní se pouze údaje potøebné k pøipojení.
	 * @param okno JFrame, na který by se pøípadnì zobrazil dialog s vyvolanou vyjimkou.
	 * @param progresBar Progressbar na který budeme zobrazovat vývoj pøipojování
	 * @param url kompletní IP adresa serveru
	 */
	public CreateConectionToMySQL(JFrame okno, ProgresBarFrame progresBar, String url){
		this.url = url;
		this.loginWindow = okno;
		prgbarFrame = progresBar;
		prgbarFrame.addListener(this);
	}
	
	/**
	 * Metoda vracející odkaz na vytvoøenou aplikaci. Spuští se zde thread {@link sConect.CreateConectionToMySQL.Task Task}
	 * @param userName uživatelské jméno pro pøipojení do databáze
	 * @param pass heslo v char [] field.
	 * @return
	 */
	public void executeVytvor(String userName, char [] pass){
		t = new Task(userName, pass);
		t.execute();
	}
	
	
	/**
	 * <p>Tøída rozšiøující <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/SwingWorker.html">SwingWorker</a>,
	 *  která vytváøí celou aplikaci v pozadí, zatímco zobrazuje stav vytváøení pomocí {@link app.ProgresBarFrame}</p>
	 * <p>Trochu zbyteèná tøída, protože už tuhle tøídu vytváøim ve <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/SwingWorker.html">SwingWorker</a>,
	 * ale tak nic se nestane. Bylo by moc práce to zase pøepisovat a dávat do jednoho.</p>
	 * @author Ondøej Havlíèek
	 */
	private class Task extends SwingWorker<MainFrame, Void> {
        /*
         * Main task. Executed in background thread.
         */
		private String userName;
		private char [] pass;
		private Task(String jmeno, char [] pass){
			prgbarFrame.setVisible(true);
			this.pass = pass;
			this.userName = jmeno;
		}
		
        @Override
        public MainFrame doInBackground() {  
            prgbarFrame.setPripojuji();
        	// pripojuju
        	String password;
        	Connection conn = null;
        	try{
        		password = new String(pass);
        		Arrays.fill(pass,'0');
        		Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url,userName,password);
				password = " ";
        		password = null;
        	} catch(Exception e){
        		if(e.getMessage().startsWith(spatneHeslo)){
        			prgbarFrame.setVisible(false);
        			if(!isCancelled()){
        				JOptionPane.showMessageDialog(loginWindow, "\u0160patn\u00E9 heslo");
        			}
				}
				else if(e.getMessage().startsWith(neniPripojeni)) {
					prgbarFrame.setVisible(false);
					if(!isCancelled()){
						JOptionPane.showMessageDialog(loginWindow, "Není zapnutý server (nebo špatná IP adresa serveru)");
					}
				}
				else {
					prgbarFrame.setVisible(false);
					ExceptionWin.showExceptionMessage(e);
				}
        		if(conn != null){
        			try {
						conn.close();
					} catch (SQLException e1) {
						ExceptionWin.showExceptionMessage(e1);
					}
        			conn = null;
        		}
        		return null;
        	} finally{
        		password = " ";
        		password = null;
        		System.gc();
        	}
        	//vytvarim aplikaci
        	prgbarFrame.setVytvarimAplikaci();
        	MainFrame hlavniOkno = new MainFrame(conn, userName, prgbarFrame.getApProgresBar());
        	prgbarFrame.setHotovo();
        	return hlavniOkno;
        }
       

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
        	prgbarFrame.setVisible(false);
        	prgbarFrame.dispose();
        	
        	MainFrame okno = null;
        	try {
    			okno = t.get();
    		} catch (Exception e) {			
    			if (cancelZprava.startsWith(e.toString())) {
    				JOptionPane.showMessageDialog(loginWindow, "Pøipojování pøerušeno");
    			} else {
    				ExceptionWin.showExceptionMessage(e);
    			}
    			
    		}
        	if(okno != null){
        		okno.setVisible(true);
        		loginWindow.setVisible(false);
        		loginWindow.dispose();
        	}
        }
    }
	/**
	 * Obsluha pro {@code CancelButton} v {@link app.ProgresBarFrame}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("Cancel")){
			boolean pom = t.cancel(true);
			System.out.println("preruseno "+pom );
			if(!pom){
				JOptionPane.showMessageDialog(loginWindow, "Aplikace je již vytoøena");
			}
		}
		
	}
}
