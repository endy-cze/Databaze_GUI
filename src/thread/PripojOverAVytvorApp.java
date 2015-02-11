package thread;

import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import app.MainFrame;
import app.ProgresBarFrame;
import sablony.ProgressBarDemo;
import sablony.errorwin.ExceptionWin;
import security2.OverHeslo;

/**
 * Objekt pro ukládání Connection a CallableStatements pro komunikaci s databází.
 * @author Havlicek
 *
 */
public class PripojOverAVytvorApp extends SwingWorker<MainFrame, Void> {
        /*
         * Main task. Executed in background thread.
         */
		private char [] heslo; 
		private String jmeno;
		private ProgresBarFrame mujBar;
		private JFrame okno;
		private boolean uspesnePrihlaseno = false;
		private final String url;
		
		public PripojOverAVytvorApp(String jmeno, char [] heslo, JFrame okno, ProgresBarFrame bar, String url){
			this.url = url;
			this.heslo = heslo;
			this.jmeno = jmeno;
			this.mujBar = bar;
			this.okno = okno;
		}
		
        @Override
        public MainFrame doInBackground() {
			OverHeslo over = new OverHeslo(okno, mujBar, url);
			uspesnePrihlaseno = over.over2(jmeno, heslo);
			if(uspesnePrihlaseno){ //prihlaseni
				Arrays.fill(heslo,'0');
				System.gc();
				jmeno = null;
				return over.getHlavniOkno();				
			} else {
				Arrays.fill(heslo,'0');
				jmeno = null;
				System.gc();
				return null;
			}      	
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
        	MainFrame frame = null;
        	Arrays.fill(heslo, '0');
        	heslo = null;
			try {
				frame = this.get();
				if(uspesnePrihlaseno){
					frame.setVisible(true);
					okno.dispose();
				}
			} catch (Exception e) {
				ExceptionWin.showExceptionMessage(e);
			}
        }
        
        public void cancelss(){
        	if(!isDone()){
        		this.cancel(true);
        	}
        }
    }

