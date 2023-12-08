package net.codejava;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Stany_Zamowienia {
    private int id_stanu_zamowienia;
    private String nazwa;
    public Stany_Zamowienia() {
    }
    public Stany_Zamowienia(String nazwa) {
        this.nazwa = nazwa;
    }

    @Id
	@GeneratedValue(generator = "incrementor")
	@GenericGenerator(name = "incrementor", strategy = "increment")
    public int getId_Stanu_Zamowienia() {
        return id_stanu_zamowienia;
    }
    public void setId_Stanu_Zamowienia(int id_stanu_zamowienia) {
        this.id_stanu_zamowienia = id_stanu_zamowienia;
    }
    public String getNazwa() {
        return nazwa;
    }
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }
}
