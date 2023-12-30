package net.codejava;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public interface ConnectionInterface
{
	public void createDBSession();
	
	public Session getDBSession();
	
	public void closeDBSession();
}

class ProxyConnection implements ConnectionInterface
{
	OracleConnection oc;
	public void createDBSession()
	{
		if(oc == null)
			oc = OracleConnection.getInstance();
	}
	
	public Session getDBSession()
	{
		return oc.getDBSession();
	}
	
	public void closeDBSession()
	{
		try
		{
			HibernateOracle.repo_pol.wykonajPolecenia();
			HibernateOracle.repo_pol.saveToFile();
			oc = null;
		}catch(Exception e)
		{
    		JOptionPane.showMessageDialog(null, "Nie udalo polaczyc sie z baza danych. Spróbuj później");
		}	
	}
}


class OracleConnection implements ConnectionInterface{
	private static OracleConnection instance = null;
	private static Configuration config;
	private static SessionFactory sessionFactory;
	private static Session session;
	private static Transaction transaction;
	
	private OracleConnection() {}
	
	public static OracleConnection getInstance() 
	{
		if(instance==null)
		{
			synchronized(OracleConnection.class)
			{
				if(instance==null) instance = new OracleConnection();
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
