package net.codejava.Controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.hibernate.Session;
import org.hibernate.query.Query;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliDruk;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaStany_Zamowienia implements IStrategia{

	@Override
	public void dodajLogikeEdytowania(ButtonEditor be) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dodajLogikeUsuwania(ButtonEditor be) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dodajLogikeDodawania(JPanel kontener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] pobierzModel(JPanel kontener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void odswiezModel(JPanel kontener, Object[] obiekty) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dodajLogikeDruku(DyrektorTabel dyrektor) {
    	BudowniczyTabeliDruk budDruk = new BudowniczyTabeliDruk();
    	
    	PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();

		List<Obiekt_Do_Polecen> obiekty = null;
		bd.stworzSesjeBD();

		try (Session sesja2 = bd.pobierzSesjeBD()) {

			Query<Obiekt_Do_Polecen> zapytanie = null;
			zapytanie = sesja2.createQuery("FROM Stany_Zamowienia order by id_stanu_zamowienia",
					Obiekt_Do_Polecen.class);
			obiekty = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
		}

		dyrektor.tworzTabeleStany_Zamowienia(obiekty, budDruk);
		String tabela = (String) dyrektor.pobierzTabele();

		String sciezka = "wykaz_stany_zamowienia.txt";
		File plik = new File(sciezka);

		try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
			pisarz.write(tabela);
			JOptionPane.showMessageDialog(null, "Powstał plik: " + sciezka);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		}
	}

}
