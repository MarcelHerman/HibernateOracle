package net.codejava;

import javax.swing.JPanel;
import javax.swing.JTable;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public interface IStrategia {
	public void dodajLogikeEdytowania(ButtonEditor bt);
	public void dodajLogikeUsuwania(ButtonEditor bt);
	public void dodajLogikeDodawania(JPanel kontener);
}
