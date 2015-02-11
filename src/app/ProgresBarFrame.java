package app;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

import java.awt.Cursor;
import java.awt.event.ActionListener;

import javax.swing.LayoutStyle.ComponentPlacement;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.SystemColor;

public class ProgresBarFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblNewLabel;
	private JLabel lblNewJgoodiesLabel;
	private JButton btnUriit;
	private JLabel dbHotovo;
	private JLabel apHotovo;
	private JProgressBar apProgresBar;
	private JProgressBar dbProgressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ProgresBarFrame dialog = new ProgresBarFrame();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ProgresBarFrame() {
		setTitle("Pr\u016Fb\u011Bh");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ProgresBarFrame.class.getResource("/app/dbSlevarnaIco.png")));
		setAlwaysOnTop(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setBounds(100, 100, 324, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		lblNewLabel = new JLabel("P\u0159ipojuji se do datab\u00E1ze");
		
		dbProgressBar = new JProgressBar();
		
		btnUriit = new JButton("Zru\u0161it");
		btnUriit.setActionCommand("Cancel");
		btnUriit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("Vytv\u00E1\u0159\u00EDm aplikaci");
		
		apProgresBar = new JProgressBar();
		apProgresBar.setStringPainted(true);
		
		dbHotovo = new JLabel("Hotovo!");
		
		apHotovo = new JLabel("Hotovo!");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(33)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(dbHotovo))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(lblNewJgoodiesLabel)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(apHotovo))
								.addComponent(apProgresBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(dbProgressBar, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(100)
							.addComponent(btnUriit, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(462, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(35)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(dbHotovo))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(dbProgressBar, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewJgoodiesLabel)
						.addComponent(apHotovo))
					.addGap(25)
					.addComponent(apProgresBar, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnUriit)
					.addContainerGap(51, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		setPripojuji();
	}
	
	public void setPripojuji(){
		lblNewLabel.setVisible(true);
		lblNewJgoodiesLabel.setVisible(false);
		dbHotovo.setVisible(false);
		apHotovo.setVisible(false);
	}
	
	public void setVytvarimAplikaci(){
		this.dbProgressBar.setIndeterminate(false);
		dbProgressBar.setValue(100);
		dbProgressBar.setStringPainted(true);
		lblNewLabel.setVisible(true);
		dbHotovo.setVisible(true);
		lblNewJgoodiesLabel.setVisible(true);
		apHotovo.setVisible(false);
	}
	
	public void setHotovo(){
		lblNewLabel.setVisible(true);
		dbHotovo.setVisible(true);
		lblNewJgoodiesLabel.setVisible(true);
		apHotovo.setVisible(true);
	}
	public void addListener(ActionListener al) {
		btnUriit.addActionListener(al);
	}
	
	public JProgressBar getApProgresBar() {
		return apProgresBar;
	}
}
