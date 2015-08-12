package sablony;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import storage.SkladOdkazu;

public class MyJButton extends JButton {
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
	private Color [] barvy;
	private Font [] fonty;
	
	public MyJButton(String nazev, int pozadi, int popredi, SkladOdkazu sklad){
		super(nazev);
		this.barvy = sklad.getBarvy();
		this.fonty = sklad.getFonty();
		this.setFont(fonty[3]);
		this.setBackground(barvy[pozadi]);
		this.setForeground(barvy[popredi]);
		this.setContentAreaFilled(false);
		this.setPreferredSize(new Dimension(170, 30));
		if(pozadi == 10 && popredi == 1){
			this.setPreferredSize(new Dimension(190, 30));
		}
		this.setOpaque(true);
		this.setBorder(new MatteBorder(1, 1, 1, 1, barvy[6]));
		//this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, barvy[6], null));
		if(pozadi != 10 || popredi != 1) {
			//this.addFocusListener(sklad.getMyJButonnListener());
			this.addMouseListener(sklad.getMyJButonnListener());
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}
	
	public boolean isFocusPainted(){
		return false;
	}
}
