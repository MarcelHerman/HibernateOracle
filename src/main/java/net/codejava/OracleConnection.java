package net.codejava;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class OracleConnection {
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
		config.setProperty("hibernate.connection.url","jdbc:oracle:thin:@212.33.90.213:1521:xe");
		config.setProperty("hibernate.connection.username","SBD_ST_PS10_1");
		config.setProperty("hibernate.connection.password","kotkiwpiwnicy");
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
