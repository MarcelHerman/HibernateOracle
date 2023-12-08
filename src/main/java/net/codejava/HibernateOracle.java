package net.codejava;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateOracle {

	public static void main(String[] args) {
		Configuration config = new Configuration();
		
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
		
		
		SessionFactory sessionFactory = config.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		//session.save(new Kategorie("Komputery"));
		//session.save(new Zamowienia(15.25,"Wiej","Hallera",1,3));
		//session.save(new Producenci("Biedronka","238989","Bialystok","Pogodna"));
		//session.save(new Produkty("wiadro 2000",52.32,"jeszcze fajniejsze",1,1));
		//session.save(new Produkt_Zamowienia(new Produkt_Zamowienia_Id(2,3),3));
		//session.save(new Magazyny("Skieblewo","Piotrkowa"));
		//session.save(new Produkt_Magazyn(new Produkt_Magazyn_Id(1,3),2,2));
		//session.save(new Faktury("05-10-23","232312",3));
		
		transaction.commit();
		session.close();
		sessionFactory.close();
		
	}

}
