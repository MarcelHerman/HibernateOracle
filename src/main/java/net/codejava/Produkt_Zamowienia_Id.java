package net.codejava;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Produkt_Zamowienia_Id implements Serializable {
	
	@Column(name = "id_zamowienia")
	private int id_zamowienia;
	
	@Column(name = "id_produktu")
	private int id_produktu;

	public Produkt_Zamowienia_Id(int zamowienia_id_zamowienia, int produkty_id_produktu) {
		this.id_zamowienia = zamowienia_id_zamowienia;
		this.id_produktu = produkty_id_produktu;
	}
	
	public int getZamowienieId()
	{
		return id_zamowienia;
	}
	
	public int getProduktId()
	{
		return id_produktu;
	}
	
}
