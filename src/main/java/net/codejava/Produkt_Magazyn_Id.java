package net.codejava;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Produkt_Magazyn_Id implements Serializable {

	@Column(name = "Magazyny_id_magazynu")
	private int magazyny_id_magazynu;
	
	@Column(name = "Produkty_id_produktu")
	private int produkty_id_produktu;

	public Produkt_Magazyn_Id(int magazyny_id_zamowienia, int produkty_id_produktu) {
		this.magazyny_id_magazynu = magazyny_id_zamowienia;
		this.produkty_id_produktu = produkty_id_produktu;
	}
	
	public Produkt_Magazyn_Id(){}
	
	public int getProductId()
	{
		return produkty_id_produktu;
	}
	
	public void setProductId(int id)
	{
		this.produkty_id_produktu=id;
	}
	
	public int getMagazyntId()
	{
		return magazyny_id_magazynu;
	}
	
	public void setMagazynId(int id)
	{
		this.magazyny_id_magazynu = id;
	}
	
}
