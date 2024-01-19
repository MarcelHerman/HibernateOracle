package net.codejava.Models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class PolaczenieOracle implements IPolaczenia{
	private static PolaczenieOracle instance = null;
	private static Configuration config;
	private static SessionFactory sesjaFactory;
	private static Session sesja;
	private static Transaction transaction;
	
	private PolaczenieOracle() {}
	
	public static PolaczenieOracle pobierzInstancje() 
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
	
	public void stworzSesjeBD()
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
		
		sesjaFactory = config.buildSessionFactory();
		sesja = sesjaFactory.openSession();
		transaction = sesja.beginTransaction();
	}
	
	public Session pobierzSesjeBD()
	{
		return sesja;
	}
	
	public void zamknijSesjeBD()
	{
		transaction.commit();
		sesja.close();
		sesjaFactory.close();
	}
}
