package net.codejava;

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

    private float ilosc;

    public Produkt_Zamowienia(Produkt_Zamowienia_Id produkt_zamowienia_id, float ilosc) {
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

    public float getIlosc() {
        return ilosc;
    }

    public void setIlosc(float ilosc) {
        this.ilosc = ilosc;
    }


}