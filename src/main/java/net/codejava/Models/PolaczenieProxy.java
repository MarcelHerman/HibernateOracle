package net.codejava.Models;

import javax.swing.JOptionPane;

import org.hibernate.Session;

import net.codejava.HibernateOracle;

public class PolaczenieProxy implements IPolaczenia
{
	PolaczenieOracle bd;
	
	public PolaczenieProxy()
	{
		this.stworzSesjeBD();
	}
	
	public void stworzSesjeBD()
	{
		if(bd == null)
			bd = PolaczenieOracle.pobierzInstancje();
	}
	
	public Session pobierzSesjeBD()
	{
		return bd.pobierzSesjeBD();
	}
	
	public void zamknijSesjeBD()
	{
		try
		{
			HibernateOracle.repoPolecen.wykonajPolecenia();
			HibernateOracle.repoPolecen.zapiszDoPliku();
			bd = null;
		}catch(Exception e)
		{
    		JOptionPane.showMessageDialog(null, "Nie udalo polaczyc sie z baza danych. Spróbuj później. Błąd: " + e);
		}	
	}
}