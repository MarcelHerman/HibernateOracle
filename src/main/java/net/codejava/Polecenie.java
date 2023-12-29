package net.codejava;

import java.time.LocalDate;

public abstract class Polecenie {
	protected Obiekt_Do_Polecen obiekt;
	private LocalDate data_wykonania;
	private int id_wykonawcy;
	
	abstract void Wykonaj();
	
	public Polecenie(Obiekt_Do_Polecen obiekt, int id_wykonawcy) {
		this.obiekt = obiekt;
		this.data_wykonania = LocalDate.now();
		this.id_wykonawcy = id_wykonawcy;
	}

	public Obiekt_Do_Polecen getObiekt() {
		return obiekt;
	}

	public void setObiekt(Obiekt_Do_Polecen obiekt) {
		this.obiekt = obiekt;
	}

	public LocalDate getData() {
		return data_wykonania;
	}

	public void setData(LocalDate data) {
		this.data_wykonania = data;
	}

	public int getId_wykonawcy() {
		return id_wykonawcy;
	}

	public void setId_wykonawcy(int id_wykonawcy) {
		this.id_wykonawcy = id_wykonawcy;
	}
	
	@Override
    public String toString() {
        return "Data wykonania: " + data_wykonania + ";\tID wykonawcy: " + id_wykonawcy + ";\tObiekt: " + obiekt.getClass().getSimpleName();
    }
}
