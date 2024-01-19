package net.codejava.Controllers;

import javax.swing.JPanel;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

public interface IStrategia {
	public static final DyrektorOkienek dyrektorOkienek = new DyrektorOkienek();
	public void dodajLogikeEdytowania(ButtonEditor be);
	public void dodajLogikeUsuwania(ButtonEditor be);
	public void dodajLogikeDodawania(JPanel kontener);
	public Object[] pobierzModel(JPanel kontener);
	public void odswiezModel(JPanel kontener, Object[] obiekty);
	public void dodajLogikeDruku(DyrektorTabel dyrektor);
}
