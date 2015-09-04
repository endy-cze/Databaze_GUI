package sablony.tabulka;

import iListeners.MyTableListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Calendar;

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
	private static final int plusRowHeight = 7;
	
	private static final int indexColumnRokPlanovani = 12;
	private static final int indexColumnCisloTydne = 1;

	

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
	
	/**
	 * Vytvoøí booleanoské pole, které pošle do MyTableListener, ktery taky vytvoøí a taky to pole pošle do MyRenderer,
	 * který podle tohoto boolean pole vykresluje bunky tabulky.
	 * Pøi zmìne TableModelu musíš zmìnit tedy oboje, jak MyRender tak MyTableListener
	 * @param tm
	 * @param pane
	 * @param isPlanovani
	 * @param sklad
	 */
	public ColorCellTable(QueryTableModel tm, JScrollPane pane, boolean isPlanovani, SkladOdkazu sklad){
		super(tm);
		this.sklad = sklad;
		//this.barvy = sklad.getBarvy();
		
		
		this.scrollPane = pane;
		// nastaveni booleanovského pole pro render
		boolean [][] zmeneno = new boolean [tm.getRowCount()][tm.getColumnCount()];
		// nastaveni Listeneru
		listener = new MyTableListener(zmeneno,tm);
		tm.addTableModelListener(listener);
		
		// nastaveni Renderu - cell rendere
		this.cellRenderer = new MyRenderer(tm, zmeneno, isPlanovani);
		this.setDefaultRenderer(Object.class, cellRenderer);
		this.setDefaultRenderer(Double.class, cellRenderer);
		// nastaveni Vzhledu hlavicky
		this.getTableHeader().setDefaultRenderer(new DefaultHeaderRenderer());
		
		this.setShowHorizontalLines(false);
		this.setFont(sklad.getFonty()[4]); // velikost písma
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setRowHeight(this.rowHeight + plusRowHeight);
				
		// column adjuster
		columAdjuster = new TableColumnAdjuster(this);
		columAdjuster.adjustColumns();
		this.setModel(tm);
		this.setDefaultEditor(String.class, null); 
	}

	
	
	/**
	 * 
	 * @param predRadek øadek pøed, který se bude pøidávat
	 * @param data
	 */
	public void addRow(int predRadek, String [] data, boolean [][] zmeneno){
		((QueryTableModel)this.getModel()).addRow(predRadek, data);
		if(scrollPane != null){
			Dimension dim = scrollPane.getPreferredSize();
			dim.height = this.getRowHeight()*this.getModel().getRowCount() + vyskaHeaderu;
			scrollPane.setPreferredSize(dim);
		}	
		//this.setModel(getModel());
		
		this.listener.setZmeneno(zmeneno, this.dataModel);
		this.cellRenderer.zmeneno = zmeneno;
		this.getColumAdjuster().adjustColumns();
	}

	/**
	 * Nastaví nový model pro tabulku a zároveò aktualizuje booleanovské pole,
	 * které používá renderer pro vykreslování zmìnìných dat.
	 */
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
	 * Metoda ktera nastavuje hodnoty pro tabulku. radek se ale identifikuje podle roku a èísla tydne (neco jako y = [rok,è.tydne]; x = den * 2 - 1)
	 * @param rok
	 * @param tyden
	 * @param den 2-5 pondeli,utery ... patek
	 * @param value
	 * @throws IndexOutOfBoundsException pokud radek podle roku a tydne nenajde, nebo den nenalezi cislum <2-5>
	 */
	public void addValueGenericTableAtYearWeek(int rok, int tyden, int den, int value) throws IndexOutOfBoundsException{
		int pomRok, pomTyden;
		for(int i = 0; i < this.getRowCount(); i++){
			pomTyden = Integer.parseInt((String) this.getValueAt(i, indexColumnCisloTydne)); // cislo tydne
			pomRok = Integer.parseInt((String) this.getValueAt(i, indexColumnRokPlanovani));  // rok
			if(pomTyden == tyden && pomRok == rok){
				String oldStringValue = (String) this.getValueAt(i, 2 * den - 1);
				int oldValue;
				if(oldStringValue != null){
					if(oldStringValue.equalsIgnoreCase("")){
						oldValue = 0;												
					} else {
					oldValue = Integer.parseInt(oldStringValue);			
					} 
				} else {oldValue = 0;}
				
				value = value + oldValue;
				if(value < 0){throw new IndexOutOfBoundsException("Hodnota je mensi nez nula. Nemozne");}
				else if(value == 0){this.setValueAt(null, i, 2 * den - 1);} 
				else{this.setValueAt(Integer.toString(value), i, 2 * den - 1);}
				
				break;
			}
		}
	}
	
	@Override
    public boolean isCellEditable(int row, int column) {
       //all cells false
       return false;
    }
	
	/**
	 * Metoda ktera vraci zarovnavac tabulky. jen zavola adjust columns.
	 * @return TableColumnAdjuster
	 */
	public TableColumnAdjuster getColumAdjuster() {
		return columAdjuster;
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

			if (tm != null) { // zvyrazneni zmenenych hodnot azurovou barvou
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
}
