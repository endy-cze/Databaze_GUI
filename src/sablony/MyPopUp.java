package sablony;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import storage.SkladOdkazu;

public class MyPopUp extends JPopupMenu implements ActionListener  {
	
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
	private Color [] barvy = {
	  	    new Color(63,63,63),       //0 cerna hlavicka
	        new Color(88, 88, 87),     //1 mene cerna (pismo)
	        new Color(227, 227, 226),  //8 seda pozadi aplikace 
	};
	private Font [] fonty;
	private final boolean isSeznamVlastnichMaterialu;
	private JCheckBox [] checkBoxItems;
	
	
	
	/**
	 * 
	 * @param nameItems
	 * @param sidePopupMenuItems soubor itemu do kterych budeme pridavat novy Jpopupmenu
	 * @param radek radek pole sidePopupMenuItems do ktereho prave pridavame Menu Itemy
	 */
	public MyPopUp(String [] nameItems, JMenuItem [][] sidePopupMenuItems, int radek, SkladOdkazu sklad){
		super();
		isSeznamVlastnichMaterialu = false; // kvuli druhemu konstruktoru
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
		item.setForeground(barvy[2]);
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
			item.setForeground(barvy[2]);			
			item.setBorder(null);
			item.setBackground(barvy[1]);
			item.setBorder(new EmptyBorder(5, 0, 5, 0));
			item.setContentAreaFilled(false);
		    item.setOpaque(true);
		    item.setActionCommand(nameItems[i]);
		    this.add(item);
		}
	}
	
	public static MyPopUp createPopupResultSetVlastniMaterialy(ResultSet rs) throws SQLException{
		int size = 0;
		try {
		    rs.last();
		    size = rs.getRow();
		    rs.beforeFirst();
		}
		catch(Exception ex) {
			size = 0;
		}
		if(rs.getMetaData().getColumnCount() != 1){
			throw new SQLException("Spatny pocet sloupcu - MyPopUp kosntruktor");
		}
		String [] vlMaterialy = new String [size];
		int i = 0;
		while(rs.next()){
			vlMaterialy[i] = rs.getString(1); // ma jen jeden sloupec
			i++;
		}
		MyPopUp popupMenu = new MyPopUp("Vlastní materiály", vlMaterialy);
		return popupMenu;
	}
	
	/**
	 * Z databaze ziskám seznam všech materiálù a vytovøím z toho JPopupMenu s checkboxama
	 * @param rs
	 * @throws SQLException 
	 */
	private MyPopUp(String title, String [] listOfVlMaterialy) throws SQLException{
		super(title);
		isSeznamVlastnichMaterialu = true; // kvuli druhemu konstruktoru
		checkBoxItems = new JCheckBox [listOfVlMaterialy.length];
		for(int i = 0; i < listOfVlMaterialy.length; i++){
			checkBoxItems[i] = new JCheckBox(listOfVlMaterialy[i]);
			this.add(checkBoxItems[i]);
			checkBoxItems[i].setSelected(true);
			checkBoxItems[i].addActionListener(this);
		}
	}
	
	/**
	 * Vrati null pokud byl použit špatny konstruktor
	 * @return
	 */
	public JCheckBox [] getCheckBoxItems(){
		if(isSeznamVlastnichMaterialu){
			return this.checkBoxItems;
		}else {
			return null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JCheckBox c = (JCheckBox) e.getSource();
		
		if(checkBoxItems.length <= 2){
			if(checkBoxItems.length == 1)checkBoxItems[0].setSelected(true);
			else {
				checkBoxItems[0].setSelected(true);
				checkBoxItems[1].setSelected(true);
			}
		} else {
			if(c.getText().equals("Vše")){
				if(c.isSelected()){
					for(int i = 0; i < checkBoxItems.length; i++){
						checkBoxItems[i].setSelected(true);
					}
				} else{
					for(int i = 0; i < checkBoxItems.length; i++){
						checkBoxItems[i].setSelected(false);
					}
					checkBoxItems[1].setSelected(true);
				}
			} else {
				boolean isAllSelected = true;
				for(int i = 1; i < checkBoxItems.length; i++){
					if(!checkBoxItems[i].isSelected()){
						isAllSelected = false;
						break;
					}
				}
				if(isAllSelected){
					checkBoxItems[0].setSelected(true);
				} else {
					boolean isAllDeselected = true;
					for(int i = 1; i < checkBoxItems.length; i++){
						if(checkBoxItems[i].isSelected()){
							isAllDeselected = false;
							break;
						}
					}
					if(isAllDeselected){
						checkBoxItems[0].setSelected(false);
						checkBoxItems[1].setSelected(true);
					} else {
						checkBoxItems[0].setSelected(false);
					}
				}
			}
		}
		
		
		
		
		
	}
}
