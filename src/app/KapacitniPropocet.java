package app;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;

import sablony.errorwin.ExceptionWin;
import sablony.tabulka.ColorCellTable;
import sablony.tabulka.QueryTableModel;
import sqlstorage.SQLStor;
import storage.SkladOdkazu;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.Dimension;

import javax.swing.JProgressBar;

public class KapacitniPropocet extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private ColorCellTable table;
	private SQLStor sql;
	private JScrollPane scrollPane;
	private JPanel panel;
	private JProgressBar progressBar;
	private MainFrame hlavniOkno;
	private static final String acesDenied = "execute command denied to user";

	/**
	 * Create the dialog.
	 */
	public KapacitniPropocet(SkladOdkazu sklad) {
		sql = sklad.getSql();
		hlavniOkno = sklad.getHlavniOkno();
		setTitle("Propo\u010Det");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		{
			scrollPane = new JScrollPane();
			scrollPane.setMinimumSize(new Dimension(400, 220));
			{
				table = new ColorCellTable(sklad.getPrazdneTabulky()[0], scrollPane, false,sklad);
				table.setMinimumSize(new Dimension(400, 858));
				scrollPane.setViewportView(table);
			}
		}
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
					.addGap(0))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			panel = new JPanel();
			getContentPane().add(panel, BorderLayout.NORTH);
			{
				progressBar = new JProgressBar();
				progressBar.setIndeterminate(true);
				panel.add(progressBar);
			}
		}
	}
	
	public void setVitsibleProgressBar(boolean t){
		this.panel.setVisible(t);
	}
	
	/**
	 * 
	 * @param cisloTydne
	 * @param rok
	 * @param f
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void setTable(int cisloTydne, int rok, char f) throws SQLException, ParseException{
		setVitsibleProgressBar(true);
		PropocetTable t = new PropocetTable(cisloTydne,rok,f, this, table);
		t.execute();
	}
	
	private class PropocetTable extends SwingWorker<QueryTableModel, Void> {
        /*
         * Main task. Executed in background thread.
         */
		private int cisloTydne;
		private int rok;
		private char f;
		private KapacitniPropocet okno;
		private ColorCellTable table;
		
		private PropocetTable(int cisloTydne, int rok, char f, KapacitniPropocet okno, ColorCellTable table){
			this.cisloTydne = cisloTydne;
			this.rok = rok;
			this.f = f;
			this.okno = okno;
			this.table = table;
		}
		
        @Override
        public QueryTableModel doInBackground() { 
    		ResultSet rs;
			try {
				rs = sql.kapacitniPropocet(cisloTydne, rok, f);
				QueryTableModel model = new QueryTableModel(rs); // , new String[] {"Datum lití","Propoèet "+jmenoSloupce}
				rs.close();
				return model;
			} catch (SQLException e) {
				if(e.getLocalizedMessage() != null){
					if(e.getLocalizedMessage().startsWith(acesDenied)){
						okno.setVisible(false);
						JOptionPane.showMessageDialog(hlavniOkno, "Na tuto operaci nemáte pravomoce");
					}else {
						ExceptionWin.showExceptionMessage(e);
					}
				} else {
					ExceptionWin.showExceptionMessage(e);
				}
					return null;
			}
        }
       

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
			try {
				okno.setTitle("Propoèet týdne "+cisloTydne+" v roce "+rok+" formovna: "+f);
	        	QueryTableModel model;
				model = this.get();
				if(model == null){
					okno.setVisible(false);
				} else {
					table.setModel(model);
					okno.setVitsibleProgressBar(false);
					okno.pack();
				}
				
			} catch (Exception e) {
				ExceptionWin.showExceptionMessage(e);
			} 
        }
    }

}
