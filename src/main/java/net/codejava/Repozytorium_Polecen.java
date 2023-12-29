package net.codejava;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Repozytorium_Polecen {
	private ArrayList<Polecenie> polecenia = new ArrayList<Polecenie>();
	
	public void wykonajPolecenie(Polecenie polecenie) {
		polecenie.Wykonaj();
		polecenia.add(polecenie);
	}
	
	public ArrayList<Polecenie> getListaPolecen(){
		return polecenia;
	}

	public void setPolecenia(ArrayList<Polecenie> polecenia) {
		this.polecenia = polecenia;
	}
	
	public void saveToFile() {
		String path = "historia_polecen.txt";
        File file = new File(path);

        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Utworzono nowy plik: " + path);
            } else {
                System.out.println("Plik już istnieje: " + path);
            }

            // Używamy konstruktora FileWriter z trybem append (dopisywania)
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                // Kod zapisu do pliku
                for (Polecenie polecenie : polecenia) {
                    writer.write(polecenie.toString());
                    writer.newLine();
                }
                System.out.println("Zapisano polecenia do pliku: " + path);
            } catch (IOException e) {
                System.err.println("Błąd podczas zapisu do pliku: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas tworzenia pliku: " + e.getMessage());
            e.printStackTrace();
        }
    
	}
	
}
