package net.codejava.Models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Produkt_Zamowienia")
public class Produkt_Zamowienia implements java.io.Serializable, Obiekt_Do_Polecen {

    @EmbeddedId
    private Produkt_Zamowienia_Id produkt_zamowienia_id;

    private int ilosc;

    public Produkt_Zamowienia(Produkt_Zamowienia_Id produkt_zamowienia_id, int ilosc) {
        this.produkt_zamowienia_id = produkt_zamowienia_id;
        this.ilosc = ilosc;
    }
    
    public Produkt_Zamowienia(){}
    
    public int getZamowienieId()
    {
    	return produkt_zamowienia_id.getZamowienieId();
    }
    
    public int getProduktId()
    {
    	return produkt_zamowienia_id.getProduktId();
    }

    public int getIlosc() {
        return ilosc;
    }

    public Produkt_Zamowienia_Id getProdukt_zamowienia_id() {
		return produkt_zamowienia_id;
	}

	public void setProdukt_zamowienia_id(Produkt_Zamowienia_Id produkt_zamowienia_id) {
		this.produkt_zamowienia_id = produkt_zamowienia_id;
	}

	public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }


}