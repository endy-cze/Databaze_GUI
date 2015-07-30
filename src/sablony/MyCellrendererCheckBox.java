package sablony;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIDefaults;


import sablony.storage.DateStor;

public class MyCellrendererCheckBox implements ListCellRenderer<DateStor> {
	Color c;
	public MyCellrendererCheckBox(){
		UIDefaults defaults = javax.swing.UIManager.getDefaults();
		c = defaults.getColor("List.selectionBackground");
	}
	@Override
    public Component getListCellRendererComponent(JList<? extends DateStor> list, DateStor value, int index,
			boolean isSelected, boolean cellHasFocus){
		DateStor checkbox = value;
		if(cellHasFocus){
			checkbox.setBackground(c);
		} else {
			checkbox.setBackground(Color.WHITE);
		} 
      return checkbox;
    }
  }
