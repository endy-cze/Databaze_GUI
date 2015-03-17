package iListeners;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import storage.SkladOdkazu;

public class PanelFiltrListenerTextField implements DocumentListener {
	private Component [] pole; 
	private Component [] vypisy; 
	private SkladOdkazu sklad;
	private Color [] barvy;
	private final LineBorder changedBorder;
	private final LineBorder defaultBorder;
	
	public PanelFiltrListenerTextField(SkladOdkazu sklad, Component [] pole, Component [] vypisy){
		this.pole = pole;
		this.vypisy = vypisy;
		this.sklad = sklad;
		this.barvy = sklad.getBarvy();
		changedBorder = new LineBorder(barvy[5], 2);
		defaultBorder = new LineBorder(barvy[2], 2);
	}
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		for(int i = 0; i < pole.length; i++){
			if(!pole[i].hasFocus())continue;
			if(pole[i] instanceof JTextField){
				if(((JTextField)pole[i]).getText().equalsIgnoreCase("")){
					((JTextField)pole[i]).setBackground(barvy[2]);
					((JTextField)pole[i]).setForeground(Color.WHITE);
					((JTextField)pole[i]).setBorder(defaultBorder);
				} else {
					((JTextField)pole[i]).setBackground(barvy[5]);
					((JTextField)pole[i]).setForeground(Color.BLACK);
					((JTextField)pole[i]).setBorder(changedBorder);
				}
			}
		}
		for(int i = 0; i < vypisy.length; i++){
			if(!vypisy[i].hasFocus())continue;
			if(vypisy[i] instanceof JTextField){
				if(((JTextField)vypisy[i]).getText().equalsIgnoreCase("")){
					((JTextField)vypisy[i]).setBackground(barvy[2]);
					((JTextField)vypisy[i]).setForeground(Color.WHITE);
					((JTextField)vypisy[i]).setBorder(defaultBorder);
				} else {
					((JTextField)vypisy[i]).setBackground(barvy[5]);
					((JTextField)vypisy[i]).setForeground(Color.BLACK);
					((JTextField)vypisy[i]).setBorder(changedBorder);
				}
			}
		}
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		for(int i = 0; i < pole.length; i++){
			if(!pole[i].hasFocus())continue;
			if(pole[i] instanceof JTextField){
				if(((JTextField)pole[i]).getText().equalsIgnoreCase("")){
					((JTextField)pole[i]).setBackground(barvy[2]);
					((JTextField)pole[i]).setForeground(Color.WHITE);
					((JTextField)pole[i]).setBorder(defaultBorder);
				} else {
					((JTextField)pole[i]).setBackground(barvy[5]);
					((JTextField)pole[i]).setForeground(Color.BLACK);
					((JTextField)pole[i]).setBorder(changedBorder);
				}
			}
		}
		for(int i = 0; i < vypisy.length; i++){
			if(!vypisy[i].hasFocus())continue;
			if(vypisy[i] instanceof JTextField){
				if(((JTextField)vypisy[i]).getText().equalsIgnoreCase("")){
					((JTextField)vypisy[i]).setBackground(barvy[2]);
					((JTextField)vypisy[i]).setForeground(Color.WHITE);
					((JTextField)vypisy[i]).setBorder(defaultBorder);
				} else {
					((JTextField)vypisy[i]).setBackground(barvy[5]);
					((JTextField)vypisy[i]).setForeground(Color.BLACK);
					((JTextField)vypisy[i]).setBorder(changedBorder);
				}
			}
		}
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		for(int i = 0; i < pole.length; i++){
			if(!pole[i].hasFocus())continue;
			if(pole[i] instanceof JTextField){
				if(((JTextField)pole[i]).getText().equalsIgnoreCase("")){
					((JTextField)pole[i]).setBackground(barvy[2]);
					((JTextField)pole[i]).setForeground(Color.WHITE);
					((JTextField)pole[i]).setBorder(defaultBorder);
				} else {
					((JTextField)pole[i]).setBackground(barvy[5]);
					((JTextField)pole[i]).setForeground(Color.BLACK);
					((JTextField)pole[i]).setBorder(changedBorder);
				}
			}
		}
		for(int i = 0; i < vypisy.length; i++){
			if(!vypisy[i].hasFocus())continue;
			if(vypisy[i] instanceof JTextField){
				if(((JTextField)vypisy[i]).getText().equalsIgnoreCase("")){
					((JTextField)vypisy[i]).setBackground(barvy[2]);
					((JTextField)vypisy[i]).setForeground(Color.WHITE);
					((JTextField)vypisy[i]).setBorder(defaultBorder);
				} else {
					((JTextField)vypisy[i]).setBackground(barvy[5]);
					((JTextField)vypisy[i]).setForeground(Color.BLACK);
					((JTextField)vypisy[i]).setBorder(changedBorder);
				}
			}
		}
		
	}

}
