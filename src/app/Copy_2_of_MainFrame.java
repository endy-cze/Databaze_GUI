package app;

import interfaces.MainSideJButtonListener;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Rectangle;

import javax.swing.BoxLayout;

import java.awt.Dimension;

import javax.swing.JButton;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;

import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.UIManager;
import javax.swing.JScrollPane;

import sablony.MyJButton;
import sablony.MyPopUp;
import sablony.errorwin.ExceptionWin;

import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;

import java.awt.ComponentOrientation;
import java.awt.Insets;
import java.sql.Connection;

import javax.swing.DebugGraphics;


public class Copy_2_of_MainFrame extends JFrame {	
	private JPopupMenu popupMenu;
	private JPopupMenu popupMenu_1;
	private JPopupMenu popupMenu_2;
	
	private JLabel [] hlavickaLabels;
	private MyJButton [] sideListButton;
	private JPopupMenu [] sidePopupMenulist;
	private Connection conn;
	
	private MainSideJButtonListener listener;
		
	private Color [] barvy = {
	  	    new Color(63,63,63),       //0 cerna hlavicka
	        new Color(88, 88, 87),     //1 mene cerna (pismo)
	        new Color(98, 98, 98),     //2 mene cerna (pismo)
	        new Color(106, 200, 235),  //3 azurova (odlhasit)
	        new Color(112, 216, 255),  //4 azurova svetlejsi (button)
	        new Color(187, 187, 187),  //5 seda (sipky u navigatoru)
	        new Color(196, 196, 196),  //6 seda okraje oken
	        new Color(197, 197, 197),  //7 seda barva (nadpis header, a prihlas, uziv)
	        new Color(227, 227, 226),  //8 seda pozadi aplikace 
			new Color(232, 232, 232),  //9 ohraniceni tabulky
	        new Color(240, 240, 240),  //10 pozadi tlaèítek
	        new Color(246, 246, 246),  //11 bile pismo v tabulce - novyzakaznik
			new Color(249, 249, 249),  //12 Bíle pozadí vedlejsi okna
	  	    new Color(59,59,59), 	   //13 cerna barva ve tlaèitku vyhledavat u tabulky
	  	    new Color(72,72,72), 	   //14 cerna barva, hlavicka tabulky
		    new Color(111,111,111),	   //15 ohranièeni tlaèitka
		    new Color(220,220,220),    //16 selected row color
		    new Color(232, 232, 232),  //17 pozadí tlaèítka Pøidat 
	  	    new Color(243, 247, 249)}; //18 pozadí tabulky radku (modrejsi)

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
			    { 
					//UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

			    } 
			    catch(Exception e){
			    	ExceptionWin win = new ExceptionWin(e);
			    }
				
				/*UIManager.put("MenuBar.background", Color.RED);
                UIManager.put("Menu.background", Color.GREEN);
                UIManager.put("MenuItem.background", Color.MAGENTA);*/
                
				try {
					Copy_2_of_MainFrame frame = new Copy_2_of_MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public void createMainListener(JButton [] seznam, JPopupMenu [] popupMenu){
		this.listener = new MainSideJButtonListener(seznam, popupMenu, null);
	}
	
	public void addListeners(){
		for(int i = 0; i < this.sideListButton.length; i++){
			sideListButton[i].addMouseListener(listener);
		}
	}
	
	public void initPopUpMenus(){
		String [] jmena0 = {"Nov\u00FD z\u00E1kazn\u00EDk", "Nov\u00FD model", "Nov\u00E1 zak\u00E1zka"};
		sidePopupMenulist[0] = new MyPopUp(jmena0);
		
		String [] jmena1 = {"\u00DAprava z\u00E1kazn\u00EDka", "\u00DAprava modelu", "\u00DAprava zak\u00E1zky"};
		sidePopupMenulist[1] = new MyPopUp(jmena1);
		
		String [] jmena2 = {"Pl\u00E1nov\u00E1n\u00ED lit\u00ED", "P\u0159idat d\u00EDl\u010D\u00ED term\u00EDny"};
		sidePopupMenulist[2] = new MyPopUp(jmena2);
		
		String [] jmena3 = {"Zadat nov\u00FD odlitek/zmetek", "Zadat vy\u010Dist\u011Bn\u00FD kus"};
		sidePopupMenulist[3] = new MyPopUp(jmena3);
		
		String [] jmena4 = {"Smazat z\u00E1kazn\u00EDka", "Smazat model", "Smazat Zak\u00E1zku"};
		sidePopupMenulist[4] = new MyPopUp(jmena4);
		
		String [] jmena5 = {"Expedice"};
		sidePopupMenulist[5] = new MyPopUp(jmena5);
		
		String [] jmena6 = {
				"V\u00FDpis odlitk\u016F v kg/k\u010D (od-do)", "V\u00FDpis zpo\u017Ed\u011Bn\u00ED",
				"V\u00FDpis dle term\u00EDnu expedice", "V\u00FDpis polo\u017Eek s odhadovanou hmotnosti", 
				"Mzdy sl\u00E9va\u010D\u016F", "Denn\u00ED v\u00FDpis odlit\u00FDch (vyroben\u00FDch) kus\u016F",
				"V\u00FDpis vy\u010Di\u0161t\u011Bn\u00FDch kus\u016F za obdob\u00ED", "Inventura rozpracovan\u00E9 v\u00FDroby",
				"V\u00FDpis odlit\u00FDch kus\u016F od-do", "T\u00FDdenn\u00ED tabulka v\u00FDroby", "V\u00FDpis expedice od-do", 
				"V\u00FDpis skladu ke dne\u0161n\u00EDmu dni"};
		sidePopupMenulist[6] = new MyPopUp(jmena6);
	}
	
	public void initJButtons(){
		sideListButton[0] = new MyJButton("Nov\u00E1 zak\u00E1zka",10,1);
		sideListButton[1] = new MyJButton("Pl\u00E1nov\u00E1n\u00ED",10,1);	
		sideListButton[2] = new MyJButton("\u00DAprava zak\u00E1zky",10,1);
		sideListButton[3] = new MyJButton("Zad\u00E1v\u00E1n\u00ED a \u00FAprava odlitk\u016F", 10, 1);
		sideListButton[4] = new MyJButton("Smazat zak\u00E1zku", 10, 1);
		sideListButton[5] = new MyJButton("Expedice", 10, 1);
		sideListButton[5] = new MyJButton("V\u00FDpisy", 10, 1);
	}
	
	public void initLabels(){
				
		
	}

	/**
	 * Create the frame.
	 */
	public Copy_2_of_MainFrame() {
		hlavickaLabels = new JLabel [4];
		
		setTitle("Datab\u00E1ze Sl\u00E9v\u00E1rna Stra\u0161ice");
		setBounds(new Rectangle(100, 100, 1266, 668));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel jPanelHeader = new Hlavicka();
		jPanelHeader.setBackground(barvy[0]);
		
		contentPane.add(jPanelHeader);
		
		hlavickaLabels[0] = new JLabel("Sl\u00E9v\u00E1rna Stra\u0161ice");
		hlavickaLabels[0].setFont(new Font("Tahoma", Font.PLAIN, 20));
		hlavickaLabels[0].setForeground(barvy[7]);
		
		hlavickaLabels[1] = new JLabel("P\u0159ihl\u00E1\u0161en\u00FD u\u017Eivatel:");
		hlavickaLabels[1].setForeground(barvy[7]);
		
		hlavickaLabels[2] = new JLabel("admin");
		hlavickaLabels[2].setForeground(Color.WHITE);
		
		hlavickaLabels[3] = new JLabel("|");
		hlavickaLabels[3].setForeground(barvy[7]);
		
		JButton btnNewButton = new JButton("odhl\u00E1sit");
		btnNewButton.setBorder(new EmptyBorder(4, 9, 4, 9));
		btnNewButton.setForeground(barvy[3]);
		btnNewButton.setContentAreaFilled(false);
		GroupLayout gl_jPanelHeader = new GroupLayout(jPanelHeader);
		gl_jPanelHeader.setHorizontalGroup(
			gl_jPanelHeader.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jPanelHeader.createSequentialGroup()
					.addGap(23)
					.addComponent(hlavickaLabels[0])
					.addPreferredGap(ComponentPlacement.RELATED, 939, Short.MAX_VALUE)
					.addComponent(hlavickaLabels[1])
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(hlavickaLabels[2])
					.addGap(2)
					.addComponent(hlavickaLabels[3])
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton)
					.addGap(69))
		);
		gl_jPanelHeader.setVerticalGroup(
			gl_jPanelHeader.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_jPanelHeader.createSequentialGroup()
					.addGroup(gl_jPanelHeader.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jPanelHeader.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_jPanelHeader.createParallelGroup(Alignment.BASELINE)
								.addComponent(hlavickaLabels[3])
								.addComponent(hlavickaLabels[2])
								.addComponent(hlavickaLabels[1])
								.addComponent(btnNewButton)))
						.addGroup(gl_jPanelHeader.createSequentialGroup()
							.addGap(15)
							.addComponent(hlavickaLabels[0], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addGap(14))
		);
		jPanelHeader.setLayout(gl_jPanelHeader);
		
		JPanel navigation = new JPanel();
		navigation.setBackground(barvy[8]);
		navigation.setPreferredSize(new Dimension(10, 50));
		navigation.setMinimumSize(new Dimension(10, 50));
		navigation.setMaximumSize(new Dimension(32767, 50));
		contentPane.add(navigation);
		
		
		
		
		JLabel lblNewLabel_4 = new JLabel("Sl\u00E9v\u00E1rna Stra\u0161ice");
		lblNewLabel_4.setForeground(barvy[1]);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		 
		JLabel lblNewLabel_5 = new JLabel(">>>");
		lblNewLabel_5.setForeground(barvy[5]);
		
		JLabel lblNovZakzka = new JLabel("Nov\u00E1 zak\u00E1zka");
		lblNovZakzka.setForeground(barvy[1]);
		lblNovZakzka.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel label = new JLabel(">>>");
		label.setForeground(barvy[5]);
		
		JLabel lblNovModel = new JLabel("Nov\u00FD model");
		lblNovModel.setForeground(barvy[1]);
		lblNovModel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GroupLayout gl_navigation = new GroupLayout(navigation);
		gl_navigation.setHorizontalGroup(
			gl_navigation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_navigation.createSequentialGroup()
					.addGap(27)
					.addComponent(lblNewLabel_4)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_5)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNovZakzka)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNovModel)
					.addContainerGap(1025, Short.MAX_VALUE))
		);
		gl_navigation.setVerticalGroup(
			gl_navigation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_navigation.createSequentialGroup()
					.addGap(25)
					.addGroup(gl_navigation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_4)
						.addComponent(lblNewLabel_5)
						.addComponent(lblNovZakzka, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(label)
						.addComponent(lblNovModel, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(28, Short.MAX_VALUE))
		);
		navigation.setLayout(gl_navigation);
		
		JPanel telo = new JPanel();
		contentPane.add(telo);
		telo.setLayout(new BoxLayout(telo, BoxLayout.X_AXIS));
		
		JPanel sideMenu = new JPanel();
		sideMenu.setBorder(null);
		sideMenu.setBackground(barvy[8]);
		FlowLayout flowLayout = (FlowLayout) sideMenu.getLayout();
		flowLayout.setVgap(20);
		sideMenu.setMinimumSize(new Dimension(210, 10));
		sideMenu.setPreferredSize(new Dimension(210, 10));
		sideMenu.setMaximumSize(new Dimension(210, 32767));
		telo.add(sideMenu);
		
		final JButton nova_zakazka = new MyJButton("Nov\u00E1 zak\u00E1zka",10,1);
		nova_zakazka.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				popupMenu.show(nova_zakazka, nova_zakazka.getWidth(), 0);
			}
		});
		nova_zakazka.setBorder(new EtchedBorder(EtchedBorder.LOWERED, barvy[6], null));
		nova_zakazka.setContentAreaFilled(false);
		nova_zakazka.setPreferredSize(new Dimension(160, 27));
		
		sideMenu.add(nova_zakazka);
		
		String [] jmena = {"koko", "manik", "dezo"};
		popupMenu = new MyPopUp(jmena);
		
		JButton planovani = new JButton("Pl\u00E1nov\u00E1n\u00ED");
		planovani.setPreferredSize(new Dimension(160, 27));
		planovani.setForeground(new Color(88, 88, 87));
		planovani.setFont(new Font("Tahoma", Font.PLAIN, 12));
		planovani.setContentAreaFilled(false);
		planovani.setBorder(new EtchedBorder(EtchedBorder.LOWERED, barvy[6], null));
		planovani.setBackground(barvy[12]);
		planovani.setOpaque(true);
		sideMenu.add(planovani);
		
		JPopupMenu popupMenu_3 = new JPopupMenu();
		popupMenu_1 = new JPopupMenu();
		popupMenu_1.setBorderPainted(false);
		popupMenu_1.setBorder(new EmptyBorder(10, 10, 10, 10));
		popupMenu_1.setBackground(barvy[1]);
		
		final JButton upravaZakazky = new JButton("\u00DAprava zak\u00E1zky");
		upravaZakazky.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				popupMenu_1.show(upravaZakazky, upravaZakazky.getWidth(), 0);
			}
		});
		upravaZakazky.setBorder(null);
		upravaZakazky.setPreferredSize(new Dimension(160, 27));
		upravaZakazky.setForeground(barvy[4]);
		upravaZakazky.setFont(new Font("Dialog", Font.PLAIN, 12));
		upravaZakazky.setContentAreaFilled(false);
		upravaZakazky.setOpaque(true);
		upravaZakazky.setBackground(barvy[0]);
		sideMenu.add(upravaZakazky);
		
		String [] jmena3 = {"Upravit z\u00E1kazn\u00EDka","Upravit zak\u00E1zku","Upravit model"};
		popupMenu_1 = new MyPopUp(jmena3);
		popupMenu_1.setBorderPainted(false);
		popupMenu_1.setBorder(new EmptyBorder(10, 10, 10, 10));
		popupMenu_1.setBackground(barvy[1]);
		
		JButton button = new MyJButton("Zad\u00E1v\u00E1n\u00ED a \u00FAprava odlitk\u016F", 10, 1);
		button.setBorder(new EtchedBorder(EtchedBorder.LOWERED, barvy[6], null));
		button.setPreferredSize(new Dimension(160, 27));
		sideMenu.add(button);
		
		JButton smazatZakazku = new MyJButton("Smazat zak\u00E1zku",10,1);
		smazatZakazku.setBorder(new EtchedBorder(EtchedBorder.LOWERED, barvy[6], null));
		smazatZakazku.setContentAreaFilled(false);
		smazatZakazku.setPreferredSize(new Dimension(160, 27));
		
		sideMenu.add(smazatZakazku);
				
		MyJButton mjbtnExpedice = new MyJButton("ss", 10, 1);
		mjbtnExpedice.setPreferredSize(new Dimension(160, 27));
		mjbtnExpedice.setContentAreaFilled(false);
		mjbtnExpedice.setBorder(new EtchedBorder(EtchedBorder.LOWERED, barvy[6], null));
		sideMenu.add(mjbtnExpedice);
		
		final MyJButton vypisy = new MyJButton("Smazat zak\u00E1zku", 10, 1);
		vypisy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				popupMenu_2.show(vypisy, vypisy.getWidth(), 0);
			}
		});
		vypisy.setText("V\u00FDpisy");
		vypisy.setPreferredSize(new Dimension(160, 27));
		vypisy.setContentAreaFilled(false);
		vypisy.setBorder(new EtchedBorder(EtchedBorder.LOWERED, barvy[6], null));
		sideMenu.add(vypisy);
				
		String [] jmena2 = {"V\u00FDpis odlitk\u016F v kg/k\u010D (od-do)", "V\u00FDpis zpo\u017Ed\u011Bn\u00ED","V\u00FDpis dle term\u00EDnu expedice", "V\u00FDpis polo\u017Eek s odhadovanou hmotnosti", "Mzdy sl\u00E9va\u010D\u016F", "Denn\u00ED v\u00FDpis odlit\u00FDch (vyroben\u00FDch) kus\u016F", "V\u00FDpis vy\u010Di\u0161t\u011Bn\u00FDch kus\u016F za obdob\u00ED", "Inventura rozpracovan\u00E9 v\u00FDroby", "V\u00FDpis odlit\u00FDch kus\u016F od-do", "T\u00FDdenn\u00ED tabulka v\u00FDroby", "V\u00FDpis expedice od-do", "V\u00FDpis skladu ke dne\u0161n\u00EDmu dni"};
		popupMenu_2 = new MyPopUp(jmena2);
		popupMenu_2.setPreferredSize(new Dimension(300, 339));
		
		JPanel obalVedlejsihoOkna = new JPanel();
		obalVedlejsihoOkna.setBorder(new EmptyBorder(20, 0, 20, 20));
		obalVedlejsihoOkna.setBackground(barvy[8]);
		telo.add(obalVedlejsihoOkna);
		obalVedlejsihoOkna.setLayout(new BoxLayout(obalVedlejsihoOkna, BoxLayout.X_AXIS));
		
		PromOknoNovyZakaznikAndSearch vedlejsiOkno = new PromOknoNovyZakaznikAndSearch(null);
		obalVedlejsihoOkna.add(vedlejsiOkno);
		vedlejsiOkno.setLayout(new BoxLayout(vedlejsiOkno, BoxLayout.Y_AXIS));
		
		
	}
}
