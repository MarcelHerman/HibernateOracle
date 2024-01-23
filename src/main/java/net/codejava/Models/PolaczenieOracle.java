package net.codejava.Models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class PolaczenieOracle implements IPolaczenia{
	private static PolaczenieOracle instancja = null;
	private static Configuration konfiguracja;
	private static SessionFactory fabrykaSesji;
	private static Session sesja;
	private static Transaction transakcja;
	
	private PolaczenieOracle() {}
	
	public static PolaczenieOracle pobierzInstancje() 
	{
		if(instancja==null)
		{
			synchronized(PolaczenieOracle.class)
			{
				if(instancja==null) instancja = new PolaczenieOracle();
			}		
		}			
		return instancja;
	}
	
	public void stworzSesjeBD()
	{
		konfiguracja = new Configuration();
		
		konfiguracja.setProperty("hibernate.connection.driver_class","oracle.jdbc.OracleDriver");
		konfiguracja.setProperty("hibernate.connection.url","jdbc:oracle:thin:@212.33.90.213:1521:xe");
		konfiguracja.setProperty("hibernate.connection.username","SBD_ST_PS10_1");
		konfiguracja.setProperty("hibernate.connection.password","kotkiwpiwnicy");
		konfiguracja.setProperty("hibernate.dialect","org.hibernate.dialect.Oracle8iDialect");
		konfiguracja.setProperty("hibernate.show_sql","true");
		
		konfiguracja.addAnnotatedClass(Kategorie.class);
		konfiguracja.addAnnotatedClass(Stany_Zamowienia.class);
		konfiguracja.addAnnotatedClass(Magazyny.class);
		konfiguracja.addAnnotatedClass(Producenci.class);
		konfiguracja.addAnnotatedClass(Typy_uzytkownika.class);
		konfiguracja.addAnnotatedClass(Uzytkownicy.class);
		konfiguracja.addAnnotatedClass(Zamowienia.class);
		konfiguracja.addAnnotatedClass(Produkt_Zamowienia.class);
		konfiguracja.addAnnotatedClass(Produkty.class);
		konfiguracja.addAnnotatedClass(Produkt_Magazyn.class);
		konfiguracja.addAnnotatedClass(Faktury.class);
		
		fabrykaSesji = konfiguracja.buildSessionFactory();
		sesja = fabrykaSesji.openSession();
		transakcja = sesja.beginTransaction();
	}
	
	public Session pobierzSesjeBD()
	{
		return sesja;
	}
	
	public void zamknijSesjeBD()
	{
		transakcja.commit();
		sesja.close();
		fabrykaSesji.close();
	}
}
