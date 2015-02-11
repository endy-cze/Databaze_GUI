package sablony.tabulka;

import interfaces.MyTableListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import storage.SkladOdkazu;


public class ColorCellTable extends JTable{
	
	/**
	 * Verze 2
	 */
	private static final long serialVersionUID = 1L;
	private SkladOdkazu sklad;
	private TableColumnAdjuster columAdjuster;
	private MyRenderer cellRenderer;

	private EmptyBorder ohraniceniBunky = new EmptyBorder(0, 4, 0, 0);
	private JScrollPane scrollPane;
	private MyTableListener listener;
	private static final int vyskaHeaderu = 80;
	

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
	  	    new Color(230, 247, 249),  //18 pozadí tabulky radku (modrejsi)
	  	    new Color(155,214,246)	   //19 pozadi tabulky pri zmene Azurova
	};
	
	public ColorCellTable(SkladOdkazu sklad){
		super();
		this.sklad = sklad;
		//this.barvy = sklad.getBarvy();
		
		
		//this.setShowGrid(false);
		this.setShowHorizontalLines(false);
		this.cellRenderer = new MyRenderer();
		this.setDefaultRenderer(Object.class, cellRenderer);
		this.getTableHeader().setDefaultRenderer(new DefaultHeaderRenderer());
		this.getTableHeader().setBackground(Color.black);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setRowHeight(this.rowHeight+5);
		
		//zkouska
		columAdjuster = new TableColumnAdjuster(this);
		columAdjuster.adjustColumns();
	}
	
	/**
	 * 
	 * @param tm
	 * @param pane
	 * @param isPlanovani
	 * @param sklad
	 */
	public ColorCellTable(TableModel tm, JScrollPane pane, boolean isPlanovani, SkladOdkazu sklad){
		super(tm);
		this.sklad = sklad;
		//this.barvy = sklad.getBarvy();
		
		
		this.scrollPane = pane;
		boolean [][] zmeneno = new boolean [tm.getRowCount()][tm.getColumnCount()];
		listener = new MyTableListener(zmeneno,tm);
		tm.addTableModelListener(listener);
		
		//this.setShowGrid(true);
		this.setShowHorizontalLines(false);
		this.cellRenderer = new MyRenderer(tm, zmeneno, isPlanovani);
		this.setDefaultRenderer(Object.class, cellRenderer);
		this.getTableHeader().setDefaultRenderer(new DefaultHeaderRenderer());
		this.setFont(sklad.getFonty()[4]); // velikost písma
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setRowHeight(this.rowHeight+7);
				
		//zkouska
		columAdjuster = new TableColumnAdjuster(this);
		columAdjuster.adjustColumns();
		this.setModel(tm);
		this.setDefaultEditor(String.class, null);
	}

	public TableColumnAdjuster getColumAdjuster() {
		return columAdjuster;
	}
	
	
	
	/**
	 * Pro prazdny model
	 * @param predRadek øadek pøed, který se bude pøidávat
	 * @param data
	 */
	public void addRow(int predRadek, Object [] data, boolean [][] zmeneno){
		((QueryTableModel)this.getModel()).addRow(predRadek, data);
		if(scrollPane != null){
			Dimension dim = scrollPane.getPreferredSize();
			dim.height = this.getRowHeight()*this.getModel().getRowCount() + vyskaHeaderu;
			scrollPane.setPreferredSize(dim);
		}	
		//this.setModel(getModel());
		
		this.listener.setZmeneno(zmeneno, this.dataModel);
		this.getColumAdjuster().adjustColumns();
	}
	
	/*
	 * Ještì se musí implementovat
	 * @param predRadek
	 * @param rady
	 *
	public void addRows(int predRadek, Object [][] rady){
		
	}
	*/
	
	@Override
    public boolean isCellEditable(int row, int column) {
       //all cells false
       return false;
    }
	
	public void setModel(TableModel tm){
		super.setModel(tm);
		if(scrollPane != null){			
			scrollPane.setPreferredSize(null);

			Dimension dim = scrollPane.getPreferredSize();
			dim.height = this.getRowHeight()*tm.getRowCount() + vyskaHeaderu;
			dim.width = this.getPreferredSize().width;
			scrollPane.setPreferredSize(dim);
			boolean [][]zmeneno = new boolean [tm.getRowCount()][tm.getColumnCount()]; 
			for(int i = 0; i < zmeneno.length; i++){
				for(int j = 0; j < zmeneno[i].length; j++)
					zmeneno[i][j] = false;
			}
			listener.setZmeneno(zmeneno, tm);
			cellRenderer.setMyModel(tm, zmeneno);
			tm.addTableModelListener(listener);
		}	
	}
	
	/**
	 * Praticke využití teto metody je pouze u TabulkyFyzKusy, když ukonèujeme plánování
	 * @return
	 */
	public boolean [][]getZmeneno(){
		return this.cellRenderer.zmeneno;
	}
	
	/**
	 * tato se metoda se pozije pouze v ColorCell table pøi addRow.
	 * @param zmen
	 */
	public void setZmeneno(boolean [][] zmen){
		this.cellRenderer.zmeneno = zmen;
	}
	
	public void setIsPlanovani(boolean isPlan){
		this.cellRenderer.isRozvrhPlanovani = isPlan;
	}
	

	private class MyRenderer extends DefaultTableCellRenderer{
		
		private TableModel tm;
		private boolean [][] zmeneno;
		public boolean isRozvrhPlanovani;
		
		public MyRenderer(TableModel tm, boolean [][] zmeneno, boolean isRozvrhPlanovani) {
			super();
			this.tm = tm;
			this.zmeneno = zmeneno;
			this.isRozvrhPlanovani = isRozvrhPlanovani;
			for(int i = 0; i < zmeneno.length; i++){
				for(int j = 0; j < zmeneno[i].length; j++)
					zmeneno[i][j] = false;
			}
		}
		
		
		public MyRenderer() {
			super();
		}
		
		public void setMyModel(TableModel tm, boolean [][] zmeneno){
			this.tm = tm;
			this.zmeneno = zmeneno;
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		   Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		   ((JLabel) renderer).setOpaque(true);
		   
		   if(isRozvrhPlanovani && ( column == 3 ||column == 5 ||column == 7 ||column == 9)){
			   ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
		   }
		   
		   ((JLabel) renderer).setBorder(ohraniceniBunky);
		 
		   Color foreground, background;
		   if (isSelected) {
		     foreground = Color.black;
		     background = barvy[16];
		   } else {
		     if (row % 2 == 0) {
		       foreground = Color.black;
		       background = Color.white;
		     } else {
		       foreground = Color.black;
		       background = barvy[18];
		     }
		   }

			if (tm != null) {
				if (zmeneno[row][column]) {
					foreground = Color.BLACK;
					background = barvy[19];
				}

			}
		   
			if (isRozvrhPlanovani && (column == 2 || column == 4 || column == 6 || column == 8 || column == 10)) {
				foreground = Color.WHITE;
				background = barvy[2];
			}
		   
		   renderer.setForeground(foreground);
		   renderer.setBackground(background);
		   return renderer;
		}
	}
	
	
	
	public MyTableListener getListener() {
		return listener;
	}
	
	public JScrollPane getScrollPane(){
		return this.scrollPane;
	}
	
}
