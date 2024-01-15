package net.codejava.Views;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BudowniczyOkienka {

	JPanel panel;
	
	public JPanel zwrocOkno()
	{
		return panel;
	}
	
	public void dodajLabel(String tekst)
	{
		JTextField poleTekstowe = new JTextField(7);
		panel.add(new JLabel(tekst));
		panel.add(poleTekstowe);	
		panel.add(Box.createHorizontalStrut(5));
	}
	
	public void dodajCheckBox(String tekst)
	{
		JCheckBox checkBox = new JCheckBox(tekst);
		panel.add(checkBox);
		panel.add(Box.createHorizontalStrut(5));
	}
	
	public void dodajJComboBox(String[] nazwy)
	{
		JComboBox comboBox = new JComboBox(nazwy);
		panel.add(comboBox);
		panel.add(Box.createHorizontalStrut(5));
	}
}
