package net.codejava.Controllers;

import javax.swing.JPanel;
import javax.swing.JTable;

import net.codejava.Views.BudowniczyTabeliSwing;
import net.codejava.Views.BudowniczyTabeliSwing.EdytorPrzycisku;

public interface IStrategia {
	public static final DyrektorOkienek dyrektorOkienek = new DyrektorOkienek();
	public void dodajLogikeEdytowania(EdytorPrzycisku edytorPrzycisku);
	public void dodajLogikeUsuwania(EdytorPrzycisku edytorPrzycisku);
	public void dodajLogikeDodawania(JPanel kontener);
	public Object[] pobierzModel(JPanel kontener);
	public void odswiezModel(JPanel kontener, Object[] obiekty);
}
