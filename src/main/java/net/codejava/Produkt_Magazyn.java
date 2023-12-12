package net.codejava;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Produkt_Magazyn")
public class Produkt_Magazyn implements java.io.Serializable, Obiekt_Do_Polecen{
	
	@EmbeddedId
	private Produkt_Magazyn_Id produkt_magazyn_id;
	
	private int stan_faktyczny;
	private int stan_magazynowy;
	
	public Produkt_Magazyn(Produkt_Magazyn_Id produkt_magazyn_id, int stan_faktyczny, int stan_magazynowy) {
		this.produkt_magazyn_id = produkt_magazyn_id;
		this.stan_faktyczny = stan_faktyczny;
		this.stan_magazynowy = stan_magazynowy;
	}
	
	public Produkt_Magazyn() {}

	public Produkt_Magazyn_Id getProdukt_magazyn_id() {
		return produkt_magazyn_id;
	}
	
	public int getProdukt_id()
	{
		return produkt_magazyn_id.getProductId();
	}

	public int getMagazyn_id()
	{
		return produkt_magazyn_id.getMagazyntId();
	}
	
	public void setProdukt_magazyn_id(Produkt_Magazyn_Id produkt_magazyn_id) {
		this.produkt_magazyn_id = produkt_magazyn_id;
	}

	public int getStan_faktyczny() {
		return stan_faktyczny;
	}

	public void setStan_faktyczny(int stan_faktyczny) {
		this.stan_faktyczny = stan_faktyczny;
	}

	public int getStan_magazynowy() {
		return stan_magazynowy;
	}

	public void setStan_magazynowy(int stan_magazynowy) {
		this.stan_magazynowy = stan_magazynowy;
	}
	
	
	

}
