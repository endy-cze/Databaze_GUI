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

import sablony.MyJButton;
import storage.SkladOdkazu;

public class MyJButonnListener implements MouseListener, FocusListener {
	private SkladOdkazu sklad;
	private MyJButton pombut;
	
	private Color [] barvy;
	private Font [] fonty;
	
	public MyJButonnListener(SkladOdkazu sklad){
		this.sklad = sklad;
		barvy = sklad.getBarvy();
		fonty = sklad.getFonty();
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		setPressedButton();
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		setReleasedButton();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		pombut = (MyJButton) arg0.getComponent();
		setPressedButton();

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		pombut = (MyJButton) arg0.getComponent();
		setReleasedButton();

	}
	
	public void setPressedButton(){
		pombut.setBorder(null);
		pombut.setForeground(barvy[4]);
		pombut.setContentAreaFilled(false);
		pombut.setOpaque(true);
		pombut.setBackground(barvy[0]);	
	}
	
	public void setReleasedButton(){
		pombut.setForeground(barvy[1]);
		pombut.setBackground(barvy[16]);
		pombut.setContentAreaFilled(false);
		pombut.setOpaque(true);
		pombut.setBorder(new MatteBorder(1, 1, 1, 1, barvy[6]));
		//pombut.setBorder(new EtchedBorder(EtchedBorder.LOWERED, barvy[6], null));
		pombut.setContentAreaFilled(false);
		
	}
	
	

}
