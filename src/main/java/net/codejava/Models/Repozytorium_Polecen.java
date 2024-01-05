package net.codejava.Models;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Repozytorium_Polecen {
	private ArrayList<Polecenie> polecenia = new ArrayList<Polecenie>();
	
	public void dodajPolecenie(Polecenie polecenie) {
		this.polecenia.add(polecenie);
	}
	
	public void wykonajPolecenia() {
		for(Polecenie polecenie : polecenia) {
			polecenie.Wykonaj();	
		}
	}
	
	public ArrayList<Polecenie> getListaPolecen(){
		return polecenia;
	}

	public void setPolecenia(ArrayList<Polecenie> polecenia) {
		this.polecenia = polecenia;
	}
	
	public void saveToFile() {
		String sciezka = "historia_polecen.txt";
        File file = new File(sciezka);

        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Utworzono nowy plik: " + sciezka);
            } else {
                System.out.println("Plik już istnieje: " + sciezka);
            }

            // Używamy konstruktora FileWriter z trybem append (dopisywania)
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                // Kod zapisu do pliku
                for (Polecenie polecenie : polecenia) {
                    writer.write(polecenie.toString());
                    writer.newLine();
                }
                System.out.println("Zapisano polecenia do pliku: " + sciezka);
            } catch (IOException e) {
                System.err.println("Błąd podczas zapisu do pliku: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas tworzenia pliku: " + e.getMessage());
            e.printStackTrace();
        }
        this.polecenia = new ArrayList<Polecenie>();
	}
	
}
