package sablony.errorwin;


import java.awt.EventQueue;





import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

public class ErrorWin extends JFrame {
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
	private JFrame win = this;
	private JPanel contentPane;
	
	public static ErrorWin createErrorWin(String text, boolean isExitProgram){
		ErrorWin win = new ErrorWin(text, isExitProgram);
		return win;
	}

	/**
	 * Create the frame.
	 */
	private ErrorWin(String text, boolean isExitProgram) {
		setAlwaysOnTop(true);
		setTitle("Error");
		setMinimumSize(new Dimension(800, 300));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 639, 393);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 60));
		panel.setMaximumSize(new Dimension(32767, 200));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(panel);
		
		JPanel panel_3 = new JPanel();
		panel_3.setPreferredSize(new Dimension(150, 50));
		panel.add(panel_3);
		
		JLabel lblNewLabel;
		if(isExitProgram){
			lblNewLabel = new JLabel("Ukonèuji program");
		}else {
			lblNewLabel = new JLabel("Vyskytla se chyba");
		}
		
		panel_3.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("OK");
		if(isExitProgram){
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
		}else {
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					win.dispose();
				}
			});
		}
		
		btnNewButton.setPreferredSize(new Dimension(75, 23));
		panel_3.add(btnNewButton);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		
		JTextPane txtpnPis = new JTextPane();
		txtpnPis.setText(text);
		scrollPane.setViewportView(txtpnPis);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(5)
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_2.setLayout(gl_panel_2);
		
		
		
	}
	
}
