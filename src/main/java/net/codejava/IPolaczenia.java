package net.codejava;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public interface IPolaczenia
{
	public void createDBSession();
	
	public Session getDBSession();
	
	public void closeDBSession();
}

class PolaczenieProxy implements IPolaczenia
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


class PolaczenieOracle implements IPolaczenia{
	private static PolaczenieOracle instance = null;
	private static Configuration config;
	private static SessionFactory sessionFactory;
	private static Session session;
	private static Transaction transaction;
	
	private PolaczenieOracle() {}
	
	public static PolaczenieOracle getInstance() 
	{
		if(instance==null)
		{
			synchronized(PolaczenieOracle.class)
			{
				if(instance==null) instance = new PolaczenieOracle();
			}		
		}			
		return instance;
	}
	
	public void createDBSession()
	{
		config = new Configuration();
		
		config.setProperty("hibernate.connection.driver_class","oracle.jdbc.OracleDriver");
		config.setProperty("hibernate.connection.url","jdbc:oracle:thin:@localhost:1521:xe");
		config.setProperty("hibernate.connection.username","SYSTEM");
		config.setProperty("hibernate.connection.password","123");
		config.setProperty("hibernate.dialect","org.hibernate.dialect.Oracle8iDialect");
		config.setProperty("hibernate.show_sql","true");
		
		config.addAnnotatedClass(Kategorie.class);
		config.addAnnotatedClass(Stany_Zamowienia.class);
		config.addAnnotatedClass(Magazyny.class);
		config.addAnnotatedClass(Producenci.class);
		config.addAnnotatedClass(Typy_uzytkownika.class);
		config.addAnnotatedClass(Uzytkownicy.class);
		config.addAnnotatedClass(Zamowienia.class);
		config.addAnnotatedClass(Produkt_Zamowienia.class);
		config.addAnnotatedClass(Produkty.class);
		config.addAnnotatedClass(Produkt_Magazyn.class);
		config.addAnnotatedClass(Faktury.class);
		
		sessionFactory = config.buildSessionFactory();
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}
	
	public Session getDBSession()
	{
		return session;
	}
	
	public void closeDBSession()
	{
		transaction.commit();
		session.close();
		sessionFactory.close();
	}
}
