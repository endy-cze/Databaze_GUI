package sablony;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import storage.SkladOdkazu;

public class MyPopUp extends JPopupMenu {
	
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
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
	  	    new Color(243, 247, 249),  //18 pozadí tabulky radku (modrejsi)
	  	    new Color(155,214,246)	   //19 pozadi tabulky pri zmene Azurova
	};
	private Font [] fonty;
	
	
	
	/**
	 * 
	 * @param nameItems
	 * @param sidePopupMenuItems
	 * @param radek radek pole sidePopupMenuItems do ktereho prave pridavame Menu Itemy
	 */
	public MyPopUp(String [] nameItems, JMenuItem [][] sidePopupMenuItems, int radek, SkladOdkazu sklad){
		super();
		this.fonty = sklad.getFonty();
		sidePopupMenuItems[radek] = new JMenuItem [nameItems.length];
		/*this.setBorderPainted(false);
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setBackground(barvy[1]);*/
		
		this.setBorderPainted(true);
		this.setBorder(new LineBorder(barvy[1], 10));
		this.setBackground(barvy[1]);	
	    this.setOpaque(true);
		
		JMenuItem item = new JMenuItem("1. "+nameItems[0]);
		item.setFont(fonty[3]);
		sidePopupMenuItems[radek][0] = item;
		item.setForeground(barvy[8]);
		item.setBorder(null);
		item.setBackground(barvy[1]);
		item.setBorder(new EmptyBorder(5, 0, 5, 0));
		item.setContentAreaFilled(false);
	    item.setOpaque(true);
	    item.setActionCommand(nameItems[0]);
		this.add(item);
		
		JSeparator separator;
		
		for(int i = 1; i < nameItems.length; i++){
			separator = new JSeparator();
			separator.setForeground(null);
			separator.setBackground(barvy[0]);
			separator.setPreferredSize(new Dimension(0,1));
			this.add(separator);
			
			item = new JMenuItem((i+1)+". "+nameItems[i]);
			item.setFont(fonty[3]);
			sidePopupMenuItems[radek][i] = item;
			item.setForeground(barvy[8]);			
			item.setBorder(null);
			item.setBackground(barvy[1]);
			item.setBorder(new EmptyBorder(5, 0, 5, 0));
			item.setContentAreaFilled(false);
		    item.setOpaque(true);
		    item.setActionCommand(nameItems[i]);
		    this.add(item);
		}
	}
}
