package interfaces;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class MyTableListener implements TableModelListener {
	/*
	 * Lal
	 */
	private boolean [][] zmeneno;
	private String[][] puvodniHodnoty;
	//private Object[][] puvodniHodnoty;
	private TableModel tm;
	
	public MyTableListener(boolean [][] zmeneno, TableModel tm){
		this.zmeneno = zmeneno;
		this.tm = tm;
		String p;
		puvodniHodnoty = new String [tm.getRowCount()][tm.getColumnCount()];
		for(int i = 0; i < puvodniHodnoty.length; i++){
			for(int j = 0; j < puvodniHodnoty[i].length; j++){
				p = (String) tm.getValueAt(i, j);
				puvodniHodnoty[i][j] = p;
			}
		}
	}
	
	public void resetPuvHodnoty(){
		for(int i = 0; i < puvodniHodnoty.length; i++){
			for(int j = 0; j < puvodniHodnoty[i].length; j++){
				tm.setValueAt(puvodniHodnoty[i][j], i, j);
			}
		}
	}
	
	public void setZmeneno(boolean [][] zmeneno, TableModel tm){
		this.zmeneno = zmeneno;
		this.tm = tm;
		String p;
		//Object p;
		puvodniHodnoty = new String [tm.getRowCount()][tm.getColumnCount()];
		//puvodniHodnoty = new Object [tm.getRowCount()][tm.getColumnCount()];
		for(int i = 0; i < puvodniHodnoty.length; i++){
			for(int j = 0; j < puvodniHodnoty[i].length; j++){
				p = (String) tm.getValueAt(i, j);
				puvodniHodnoty[i][j] = p;
			}
		}
	}

	/**
	 * Null a prázdný øetezec "" jsou stejné
	 */
	@Override
	public void tableChanged(TableModelEvent arg0) {
		int i = arg0.getFirstRow(), j = arg0.getColumn();
		String value = (String) tm.getValueAt(i, j);
		if(value == null){
			if(puvodniHodnoty[i][j] == null)zmeneno[i][j] = false;
			else zmeneno[i][j] = true;
			return;
		}
		if(puvodniHodnoty[i][j] == null){
			if(value.equalsIgnoreCase(""))zmeneno[i][j] = false;
		}
		if(value.equalsIgnoreCase(puvodniHodnoty[i][j]))
			zmeneno[i][j] = false;
		else zmeneno[i][j] = true;
		System.out.println("table changed");
	}

}
