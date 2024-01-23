package net.codejava;

import net.codejava.Controllers.IStrategia;
import net.codejava.Models.*;
import net.codejava.Views.widokAplikacji;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import java.util.Map;
 
public class HibernateOracle extends JFrame {
	
	public static String nazwaTypu = "null";
	public static Obiekt_Do_Polecen obiekt = null;
	public static List<Obiekt_Do_Polecen> koszyk = new ArrayList<Obiekt_Do_Polecen>();
	public static Map<String, List<Obiekt_Do_Polecen>> cache;
	
	public static int idUzytkownika;
	
	public static Repozytorium_Polecen repoPolecen = new Repozytorium_Polecen();
	
	public static IStrategia wzorzec;

	public static void main(String[] args) {
		widokAplikacji aplikacja = new widokAplikacji();
		aplikacja.Inicjalizuj();
	}
}
