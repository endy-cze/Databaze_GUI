package sablony;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIDefaults;


import sablony.storage.DateStor;

public class MyCellrendererCheckBox implements ListCellRenderer<DateStor> {
	private Color c;
	private Color back;
	
	public MyCellrendererCheckBox(){
		UIDefaults defaults = javax.swing.UIManager.getDefaults();
		back = defaults.getColor("List.selectionBackground");
		c = defaults.getColor("List.selectionForeground");
	}
	@Override
    public Component getListCellRendererComponent(JList<? extends DateStor> list, DateStor value, int index,
			boolean isSelected, boolean cellHasFocus){
		DateStor checkbox = value;
		if(cellHasFocus){
			checkbox.setBackground(back);
			checkbox.setForeground(c);
		} else {
			checkbox.setBackground(Color.WHITE);
			checkbox.setForeground(Color.BLACK);
		} 
      return checkbox;
    }
  }
