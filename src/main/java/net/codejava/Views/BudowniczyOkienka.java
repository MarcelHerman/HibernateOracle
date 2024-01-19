package net.codejava.Views;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BudowniczyOkienka {

	private JPanel panel = new JPanel();
	
	public JPanel zwrocOkno()
	{		
		return panel;
	}
	
	public void dodajJTextField(String tekst)
	{
		JTextField poleTekstowe = new JTextField(7);
		panel.add(new JLabel(tekst));
		panel.add(poleTekstowe);	
		panel.add(Box.createHorizontalStrut(5));
	}
	
	public void dodajDuzyJTextField(String tekst)
	{
		JTextField poleTekstowe = new JTextField(20);
		panel.add(new JLabel(tekst));
		panel.add(poleTekstowe);	
		panel.add(Box.createHorizontalStrut(5));
	}
	
	public void dodajCheckBox(String tekst)
	{
		JCheckBox poleWyboru = new JCheckBox(tekst);
		panel.add(poleWyboru);
		panel.add(Box.createHorizontalStrut(5));
	}
	
	public void dodajJComboBox(String[] nazwy)
	{
		JComboBox listaRozwijana = new JComboBox(nazwy);
		panel.add(listaRozwijana);
		panel.add(Box.createHorizontalStrut(5));
	}
	
	public ArrayList<JTextField> zwrocPolaTekstowe()
	{
		ArrayList<JTextField> lista = new ArrayList<JTextField>();
		
		Component[] komponenty = panel.getComponents();	

		for (Component komponent : komponenty) {
			if (komponent instanceof JTextField) {
				lista.add((JTextField) komponent);
			}
		}
		
		return lista;
	}
	
	public void zresetuj()
	{
		panel = new JPanel();
	}
}
