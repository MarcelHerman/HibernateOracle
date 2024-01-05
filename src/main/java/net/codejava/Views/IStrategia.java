package net.codejava.Views;

import javax.swing.JPanel;

import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

public interface IStrategia {
	public void dodajLogikeEdytowania(ButtonEditor bt);
	public void dodajLogikeUsuwania(ButtonEditor bt);
	public void dodajLogikeDodawania(JPanel kontener);
}
