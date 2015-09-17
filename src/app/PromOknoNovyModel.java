package app;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import sablony.MyJButton;
import storage.SkladOdkazu;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.border.LineBorder;

import java.awt.Color;


public class PromOknoNovyModel extends JPanel{
	/**
	 * Verze
	 */
	private static final long serialVersionUID = 1L;
	private SkladOdkazu sklad;
	private MainFrame hlavniOkno;
	
	private JLabel textLabelIdModelu;
	private JCheckBox isOdhatHmotnost;
	private JComboBox<Character> textFormovna;
	private Character [] formovnaZkratky = {'T','S','M'}; 
	private JTextField textJmenoModelu;
	private JTextField textCisloModelu;
	private JTextField textHmotnost;
	private JTextField textMaterial;
	private JTextField textMaterialVlastni;
	private JTextField textNorma;
	
	private Color [] barvy;
	private Component [] listTextComponent;
	private JLabel popisIdModelu;
	private MyJButton pridatModel;
	private MyJButton vyhledatPodobneModely;
	private JLabel nadpisLabelModel;
	private JLabel lblNewLabel;
	private JLabel lblNormaSe;
	private JLabel lblPoznmka;
	private JTextField textPoznamkaModel;
	private JLabel lblkonstruknZmenyApod;
	
	public void setUpravModel(String [] parametryModelu){
		popisIdModelu.setVisible(true);
		textLabelIdModelu.setVisible(true);
		pridatModel.setText("Uprav model");
		nadpisLabelModel.setText("Upravit model");
		pridatModel.setActionCommand("UpravModelAction");
		
		
		for(int i = 0; i < listTextComponent.length; i++){
			if(listTextComponent[i] instanceof JTextField){
				((JTextField) listTextComponent[i]).setText(parametryModelu[i]);
			} else if(listTextComponent[i] instanceof JCheckBox){
				boolean pom = false;
				if(parametryModelu[i].equalsIgnoreCase("Ano"))pom = true;
				else if(parametryModelu[i].equalsIgnoreCase("Ne")) pom = false;
				else JOptionPane.showMessageDialog(hlavniOkno, "Spatne zapsany v databazi jestli je odhadova hmot");
				((JCheckBox) listTextComponent[i]).setSelected(pom);
			}
			else if(listTextComponent[i] instanceof JComboBox){
				int index = 0;
				char pomocne = parametryModelu[i].charAt(0);
				if(pomocne == 'M')index = 2;
				else if(pomocne == 'S')index = 1;
				else if(pomocne == 'T')index = 0;
				
				((JComboBox) listTextComponent[i]).setSelectedIndex(index);
			} else if(listTextComponent[i] instanceof JLabel) {
				((JLabel)listTextComponent[i]).setText(parametryModelu[i]);
			}
		}
		
		
	}
	public void setNovyModel(){
		popisIdModelu.setVisible(false);
		textLabelIdModelu.setVisible(false);
		pridatModel.setText("P\u0159idat model");
		nadpisLabelModel.setText("P\u0159idat nov\u00FD model");
		pridatModel.setActionCommand("PridejNovyModelAction");
		
		for(int i = 0; i < listTextComponent.length; i++){
			if(listTextComponent[i] instanceof JTextField){
				((JTextField) listTextComponent[i]).setText("");
			} else if(listTextComponent[i] instanceof JCheckBox){
				((JCheckBox) listTextComponent[i]).setSelected(false);
			} else if(listTextComponent[i] instanceof JComboBox){				
				((JComboBox) listTextComponent[i]).setSelectedIndex(0);
			} else if(listTextComponent[i] instanceof JLabel) {
				((JLabel)listTextComponent[i]).setText("");
			}
		}
		
	}
	
	public void setNovyModelDleStareho(String [] parametryModelu){
		this.setUpravModel(parametryModelu);
		popisIdModelu.setVisible(false);
		textLabelIdModelu.setVisible(false);
		pridatModel.setText("P\u0159idat model");
		nadpisLabelModel.setText("P\u0159idat nov\u00FD model");
		pridatModel.setActionCommand("PridejNovyModelAction");
	}
	
	private void initListComponent(){
		listTextComponent[0] = textLabelIdModelu;
		listTextComponent[1] = textJmenoModelu;
		listTextComponent[2] = textCisloModelu;
		listTextComponent[3] = textMaterial;
		listTextComponent[4] = textMaterialVlastni;
		listTextComponent[5] = textHmotnost;
		listTextComponent[6] = isOdhatHmotnost;
		listTextComponent[7] = textFormovna;
		listTextComponent[8] = textNorma;
		listTextComponent[9] = textPoznamkaModel;
	}
	public void zvetsiPismo(){
		Font f = new Font("Tahoma", Font.PLAIN, 12);
		for(int i = 0; i < listTextComponent.length; i++){
			if(listTextComponent[i] instanceof JTextField)listTextComponent[i].setFont(f);
		}
	}
	/**
	 * 
	 * @param hlavniOkno
	 */
	public PromOknoNovyModel(MainFrame hlavniOkno) {
		this.hlavniOkno = hlavniOkno;
		this.sklad = hlavniOkno.getSklad();
		this.barvy = sklad.getBarvy();
		listTextComponent = new Component [10];
		sklad.setNovyModelListTextComponent(listTextComponent);
		setBackground(barvy[12]);
		setBorder(new LineBorder(barvy[6]));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {20, 0, 20, 20, 35, 37, 72, 0, 30, 30, 30, 30, 100, 68, 0, 20, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 15, 0, 0, 19, 0, 19, 0, 35, 24, 35, 0, 0, 20, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		nadpisLabelModel = new JLabel("P\u0159idat nov\u00FD model");
		nadpisLabelModel.setFont(new Font("Tahoma", Font.BOLD, 12));
		//lblPidatNovModel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_nadpisLabelModel = new GridBagConstraints();
		gbc_nadpisLabelModel.anchor = GridBagConstraints.WEST;
		gbc_nadpisLabelModel.gridwidth = 3;
		gbc_nadpisLabelModel.insets = new Insets(20, 0, 7, 5);
		gbc_nadpisLabelModel.gridx = 1;
		gbc_nadpisLabelModel.gridy = 0;
		add(nadpisLabelModel, gbc_nadpisLabelModel);
		
		JLabel popisJmenoModelu = new JLabel("Jm\u00E9no modelu:");
		GridBagConstraints gbc_popisJmenoModelu = new GridBagConstraints();
		gbc_popisJmenoModelu.anchor = GridBagConstraints.WEST;
		gbc_popisJmenoModelu.gridwidth = 2;
		gbc_popisJmenoModelu.insets = new Insets(0, 0, 7, 5);
		gbc_popisJmenoModelu.gridx = 1;
		gbc_popisJmenoModelu.gridy = 1;
		add(popisJmenoModelu, gbc_popisJmenoModelu);
		
		textJmenoModelu = new JTextField();
		GridBagConstraints gbc_textJmenoModelu = new GridBagConstraints();
		gbc_textJmenoModelu.gridwidth = 4;
		gbc_textJmenoModelu.insets = new Insets(0, 0, 7, 5);
		gbc_textJmenoModelu.fill = GridBagConstraints.HORIZONTAL;
		gbc_textJmenoModelu.gridx = 3;
		gbc_textJmenoModelu.gridy = 1;
		add(textJmenoModelu, gbc_textJmenoModelu);
		textJmenoModelu.setColumns(10);
		
		JLabel popisMaterial = new JLabel("Materi\u00E1l:");
		GridBagConstraints gbc_popisMaterial = new GridBagConstraints();
		gbc_popisMaterial.gridwidth = 2;
		gbc_popisMaterial.anchor = GridBagConstraints.WEST;
		gbc_popisMaterial.insets = new Insets(0, 0, 7, 5);
		gbc_popisMaterial.gridx = 9;
		gbc_popisMaterial.gridy = 1;
		add(popisMaterial, gbc_popisMaterial);
		
		textMaterial = new JTextField();
		GridBagConstraints gbc_textMaterial = new GridBagConstraints();
		gbc_textMaterial.gridwidth = 2;
		gbc_textMaterial.insets = new Insets(0, 0, 7, 5);
		gbc_textMaterial.fill = GridBagConstraints.HORIZONTAL;
		gbc_textMaterial.gridx = 11;
		gbc_textMaterial.gridy = 1;
		add(textMaterial, gbc_textMaterial);
		textMaterial.setColumns(10);
		
		JLabel popisCisloModelu = new JLabel("\u010C\u00EDslo modelu:");
		GridBagConstraints gbc_popisCisloModelu = new GridBagConstraints();
		gbc_popisCisloModelu.anchor = GridBagConstraints.WEST;
		gbc_popisCisloModelu.gridwidth = 2;
		gbc_popisCisloModelu.insets = new Insets(0, 0, 7, 5);
		gbc_popisCisloModelu.gridx = 1;
		gbc_popisCisloModelu.gridy = 2;
		add(popisCisloModelu, gbc_popisCisloModelu);
		
		textCisloModelu = new JTextField();
		GridBagConstraints gbc_textCisloModelu = new GridBagConstraints();
		gbc_textCisloModelu.gridwidth = 4;
		gbc_textCisloModelu.insets = new Insets(0, 0, 7, 5);
		gbc_textCisloModelu.fill = GridBagConstraints.HORIZONTAL;
		gbc_textCisloModelu.gridx = 3;
		gbc_textCisloModelu.gridy = 2;
		add(textCisloModelu, gbc_textCisloModelu);
		textCisloModelu.setColumns(10);
		
		JLabel popisMaterialVlastni = new JLabel("Vlastn\u00ED materi\u00E1l:");
		GridBagConstraints gbc_popisMaterialVlastni = new GridBagConstraints();
		gbc_popisMaterialVlastni.gridwidth = 2;
		gbc_popisMaterialVlastni.anchor = GridBagConstraints.WEST;
		gbc_popisMaterialVlastni.insets = new Insets(0, 0, 7, 5);
		gbc_popisMaterialVlastni.gridx = 9;
		gbc_popisMaterialVlastni.gridy = 2;
		add(popisMaterialVlastni, gbc_popisMaterialVlastni);
		
		textMaterialVlastni = new JTextField();
		textMaterialVlastni.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_textMaterialVlastni = new GridBagConstraints();
		gbc_textMaterialVlastni.gridwidth = 2;
		gbc_textMaterialVlastni.insets = new Insets(0, 0, 7, 5);
		gbc_textMaterialVlastni.fill = GridBagConstraints.HORIZONTAL;
		gbc_textMaterialVlastni.gridx = 11;
		gbc_textMaterialVlastni.gridy = 2;
		add(textMaterialVlastni, gbc_textMaterialVlastni);
		textMaterialVlastni.setColumns(10);
		
		popisIdModelu = new JLabel("Id modelu:");
		GridBagConstraints gbc_popisIdModelu = new GridBagConstraints();
		gbc_popisIdModelu.anchor = GridBagConstraints.WEST;
		gbc_popisIdModelu.gridwidth = 2;
		gbc_popisIdModelu.insets = new Insets(0, 0, 7, 5);
		gbc_popisIdModelu.gridx = 1;
		gbc_popisIdModelu.gridy = 3;
		add(popisIdModelu, gbc_popisIdModelu);
		
		textLabelIdModelu = new JLabel("5555");
		GridBagConstraints gbc_textLabelIdModelu = new GridBagConstraints();
		gbc_textLabelIdModelu.anchor = GridBagConstraints.WEST;
		gbc_textLabelIdModelu.gridwidth = 4;
		gbc_textLabelIdModelu.insets = new Insets(0, 0, 7, 5);
		gbc_textLabelIdModelu.gridx = 3;
		gbc_textLabelIdModelu.gridy = 3;
		add(textLabelIdModelu, gbc_textLabelIdModelu);
		
		JLabel popisHmotnost = new JLabel("* Hmotnost:");
		GridBagConstraints gbc_popisHmotnost = new GridBagConstraints();
		gbc_popisHmotnost.anchor = GridBagConstraints.WEST;
		gbc_popisHmotnost.insets = new Insets(0, 0, 7, 5);
		gbc_popisHmotnost.gridx = 1;
		gbc_popisHmotnost.gridy = 5;
		add(popisHmotnost, gbc_popisHmotnost);
		
		textHmotnost = new JTextField();
		GridBagConstraints gbc_textHmotnost = new GridBagConstraints();
		gbc_textHmotnost.gridwidth = 3;
		gbc_textHmotnost.insets = new Insets(0, 0, 7, 5);
		gbc_textHmotnost.fill = GridBagConstraints.HORIZONTAL;
		gbc_textHmotnost.gridx = 2;
		gbc_textHmotnost.gridy = 5;
		add(textHmotnost, gbc_textHmotnost);
		textHmotnost.setColumns(10);
		
		isOdhatHmotnost = new JCheckBox("Odhadovan\u00E1 hmotnost");
		isOdhatHmotnost.setBackground(barvy[12]);
		GridBagConstraints gbc_isOdhatHmotnost = new GridBagConstraints();
		gbc_isOdhatHmotnost.gridwidth = 2;
		gbc_isOdhatHmotnost.insets = new Insets(0, 0, 7, 5);
		gbc_isOdhatHmotnost.gridx = 6;
		gbc_isOdhatHmotnost.gridy = 5;
		add(isOdhatHmotnost, gbc_isOdhatHmotnost);
		
		lblNewLabel = new JLabel("* Hmotnost se zaokrouhluje na 2 des. m\u00EDsta");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridwidth = 5;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 9;
		gbc_lblNewLabel.gridy = 5;
		add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel popisFormovna = new JLabel("Formovna:");
		GridBagConstraints gbc_popisFormovna = new GridBagConstraints();
		gbc_popisFormovna.anchor = GridBagConstraints.WEST;
		gbc_popisFormovna.insets = new Insets(0, 0, 7, 5);
		gbc_popisFormovna.gridx = 1;
		gbc_popisFormovna.gridy = 6;
		add(popisFormovna, gbc_popisFormovna);
		
		textFormovna = new JComboBox<Character>();
		textFormovna.setModel(new DefaultComboBoxModel<Character>(formovnaZkratky));
		textFormovna.setSelectedIndex(0);
		//((JLabel)textFormovna.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		GridBagConstraints gbc_textFormovna = new GridBagConstraints();
		gbc_textFormovna.gridwidth = 2;
		gbc_textFormovna.insets = new Insets(0, 0, 7, 5);
		gbc_textFormovna.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFormovna.gridx = 2;
		gbc_textFormovna.gridy = 6;
		add(textFormovna, gbc_textFormovna);
		
		pridatModel = new MyJButton("P\u0159idat model", 16, 1, sklad);
		pridatModel.setActionCommand("PridejNovyModelAction");
		pridatModel.addActionListener(sklad.getNovyZakModelZakazka());
		
		JLabel popisNorma = new JLabel("** Norma:");
		GridBagConstraints gbc_popisNorma = new GridBagConstraints();
		gbc_popisNorma.anchor = GridBagConstraints.WEST;
		gbc_popisNorma.insets = new Insets(0, 0, 7, 5);
		gbc_popisNorma.gridx = 1;
		gbc_popisNorma.gridy = 8;
		add(popisNorma, gbc_popisNorma);
		
		textNorma = new JTextField();
		GridBagConstraints gbc_textNorma = new GridBagConstraints();
		gbc_textNorma.gridwidth = 3;
		gbc_textNorma.insets = new Insets(0, 0, 7, 5);
		gbc_textNorma.fill = GridBagConstraints.HORIZONTAL;
		gbc_textNorma.gridx = 2;
		gbc_textNorma.gridy = 8;
		add(textNorma, gbc_textNorma);
		textNorma.setColumns(10);
		
		lblNormaSe = new JLabel("** Norma se zaokrouhluje na 1 desetinn\u00E9 m\u00EDsto");
		GridBagConstraints gbc_lblNormaSe = new GridBagConstraints();
		gbc_lblNormaSe.anchor = GridBagConstraints.WEST;
		gbc_lblNormaSe.gridwidth = 5;
		gbc_lblNormaSe.insets = new Insets(0, 0, 5, 5);
		gbc_lblNormaSe.gridx = 9;
		gbc_lblNormaSe.gridy = 8;
		add(lblNormaSe, gbc_lblNormaSe);
		
		lblPoznmka = new JLabel("Pozn\u00E1mka:");
		GridBagConstraints gbc_lblPoznmka = new GridBagConstraints();
		gbc_lblPoznmka.anchor = GridBagConstraints.WEST;
		gbc_lblPoznmka.insets = new Insets(0, 0, 5, 5);
		gbc_lblPoznmka.gridx = 1;
		gbc_lblPoznmka.gridy = 9;
		add(lblPoznmka, gbc_lblPoznmka);
		
		textPoznamkaModel = new JTextField();
		GridBagConstraints gbc_textPoznamkaModel = new GridBagConstraints();
		gbc_textPoznamkaModel.gridwidth = 6;
		gbc_textPoznamkaModel.insets = new Insets(0, 0, 5, 5);
		gbc_textPoznamkaModel.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPoznamkaModel.gridx = 2;
		gbc_textPoznamkaModel.gridy = 9;
		add(textPoznamkaModel, gbc_textPoznamkaModel);
		textPoznamkaModel.setColumns(10);
		
		lblkonstruknZmenyApod = new JLabel("(konstruk\u010Dn\u00ED zm\u011Bny apod.)");
		GridBagConstraints gbc_lblkonstruknZmenyApod = new GridBagConstraints();
		gbc_lblkonstruknZmenyApod.anchor = GridBagConstraints.WEST;
		gbc_lblkonstruknZmenyApod.gridwidth = 5;
		gbc_lblkonstruknZmenyApod.insets = new Insets(0, 0, 5, 5);
		gbc_lblkonstruknZmenyApod.gridx = 9;
		gbc_lblkonstruknZmenyApod.gridy = 9;
		add(lblkonstruknZmenyApod, gbc_lblkonstruknZmenyApod);
		GridBagConstraints gbc_pridatModel = new GridBagConstraints();
		gbc_pridatModel.gridwidth = 4;
		gbc_pridatModel.fill = GridBagConstraints.BOTH;
		gbc_pridatModel.insets = new Insets(0, 0, 7, 5);
		gbc_pridatModel.gridx = 1;
		gbc_pridatModel.gridy = 11;
		add(pridatModel, gbc_pridatModel);
		
		/*
		vyhledatPodobneModely = new MyJButton("Vyhledat podobn\u00E9 modely", 16, 1, sklad);
		GridBagConstraints gbc_vyhledatPodobneModely = new GridBagConstraints();
		gbc_vyhledatPodobneModely.gridwidth = 5;
		gbc_vyhledatPodobneModely.fill = GridBagConstraints.BOTH;
		gbc_vyhledatPodobneModely.insets = new Insets(0, 0, 7, 5);
		gbc_vyhledatPodobneModely.gridx = 1;
		gbc_vyhledatPodobneModely.gridy = 12;
		add(vyhledatPodobneModely, gbc_vyhledatPodobneModely);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(200,200));
		scrollPane.setWheelScrollingEnabled(false); 
		scrollPane.addMouseWheelListener(new MouseWheelListener() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
		    	scrollPane.getParent().dispatchEvent(e);
		    }
		});
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridwidth = 14;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 13;
		add(scrollPane, gbc_scrollPane);
		
		

		
		
		
		
		table = new ColorCellTable(sklad.getPrazdneTabulky()[1], scrollPane, false, sklad);
		sklad.setTableNovyModel(table);
		scrollPane.setViewportView(table);
		*/
		
		initListComponent();
		zvetsiPismo();
		setNovyModel();
	}

	public JLabel getPopisIdModelu() {
		return popisIdModelu;
	}
	public JLabel getTextLabelIdModelu() {
		return textLabelIdModelu;
	}
	public MyJButton getVyhledatPodobneModely() {
		return vyhledatPodobneModely;
	}
	public MyJButton getPridatModel() {
		return pridatModel;
	}
}
