package net.codejava.Models;

import javax.swing.JOptionPane;

import org.hibernate.Session;

import net.codejava.HibernateOracle;

public class PolaczenieProxy implements IPolaczenia
{
	PolaczenieOracle oc;
	public void createDBSession()
	{
		if(oc == null)
			oc = PolaczenieOracle.getInstance();
	}
	
	public Session getDBSession()
	{
		return oc.getDBSession();
	}
	
	public void closeDBSession()
	{
		try
		{
			HibernateOracle.repoPolecen.wykonajPolecenia();
			HibernateOracle.repoPolecen.saveToFile();
			oc = null;
		}catch(Exception e)
		{
    		JOptionPane.showMessageDialog(null, "Nie udalo polaczyc sie z baza danych. Spróbuj później");
		}	
	}
}