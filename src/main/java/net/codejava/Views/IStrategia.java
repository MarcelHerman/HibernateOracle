package net.codejava.Views;

import javax.swing.JPanel;

import net.codejava.Controllers.DyrektorOkienek;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

public interface IStrategia {
	public static final DyrektorOkienek dyrektorOkienek = new DyrektorOkienek();
	public void dodajLogikeEdytowania(ButtonEditor bt);
	public void dodajLogikeUsuwania(ButtonEditor bt);
	public void dodajLogikeDodawania(JPanel kontener);
}
