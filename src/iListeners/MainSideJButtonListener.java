package iListeners;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.border.MatteBorder;

import storage.SkladOdkazu;

public class MainSideJButtonListener implements MouseListener, FocusListener {
	private SkladOdkazu sklad;
	
	private JButton [] sideListButton;	
	private JPopupMenu [] popupMenu;
	
	private int lastPressed = -1;
	private int pressed = -1;
	
	private JButton pombut;
	
	private Color [] barvy;
	//private Font [] fonty;
	
	public MainSideJButtonListener(JButton [] sideListButton, JPopupMenu [] popupMenu, SkladOdkazu sklad){
		this.sklad = sklad;
		barvy = sklad.getBarvy();
		this.sideListButton = sideListButton;
		this.popupMenu = popupMenu;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		pombut = (JButton) arg0.getComponent();
		String pom;
		int i = 0;
		for(i = 0; i < this.sideListButton.length; i++){
			pom = pombut.getText();
			if(pom.equalsIgnoreCase(this.sideListButton[i].getText())){
				break;
			}
		}
		popupMenu[i].show(sideListButton[i], sideListButton[i].getWidth()+4, 0);
		this.pressed = i;
		pombut.requestFocus();
		String s = null;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void focusGained(FocusEvent arg0) {
		setPressedButton(pressed);
		this.lastPressed = pressed;
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		setReleasedButton(this.lastPressed);		
	}
	
	public void setPressedButton(int i){
		sideListButton[i].setBorder(null);
		//sideListButton[i].setPreferredSize(new Dimension(160, 27));
		sideListButton[i].setForeground(barvy[4]);
		sideListButton[i].setContentAreaFilled(false);
		sideListButton[i].setOpaque(true);
		sideListButton[i].setBackground(barvy[0]);	
	}
	
	public void setReleasedButton(int i){
		sideListButton[i].setForeground(barvy[1]);
		sideListButton[i].setBackground(barvy[10]);
		sideListButton[i].setContentAreaFilled(false);
		sideListButton[i].setOpaque(true);
		sideListButton[i].setBorder(new MatteBorder(1, 1, 1, 1, barvy[6]));
		//sideListButton[i].setBorder(new EtchedBorder(EtchedBorder.LOWERED, barvy[6], null));
		sideListButton[i].setContentAreaFilled(false);
		//sideListButton[i].setPreferredSize(new Dimension(160, 27));
	}

}
