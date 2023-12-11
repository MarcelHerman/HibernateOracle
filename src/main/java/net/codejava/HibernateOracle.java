package net.codejava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateOracle {

	public static void main(String[] args) {
		
		OracleConnection oc =  OracleConnection.getInstance();
		
		oc.createDBSession();
		
		Session session = oc.getDBSession();
		
		//session.save(new Kategorie("laptopy"));
		//session.save(new Zamowienia(15.25,"Wiej","Hallera",1,3));
		//session.save(new Producenci("Biedronka","238989","Bialystok","Pogodna"));
		//session.save(new Produkty("wiadro 2000",52.32,"jeszcze fajniejsze",1,1));
		//session.save(new Produkt_Zamowienia(new Produkt_Zamowienia_Id(2,3),3));
		//session.save(new Magazyny("Skieblewo","Piotrkowa"));
		//session.save(new Produkt_Magazyn(new Produkt_Magazyn_Id(1,3),2,2));
		//session.save(new Faktury("05-10-23","232312",3));
		
        oc.closeDBSession();

		try
		{
			session = oc.getDBSession();
		}catch(Exception e)
		{
            System.out.println("Blad dodania tablicy");
		}
		
		String placeholderLogin = "login";
		String placeholderPassword = "1234";
		
		final JFrame frame = new JFrame("Elektryka Prad Nie Tyka");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        JMenuBar bar = new JMenuBar();
		JButton zaloguj = new JButton("Zaloguj sie");
		JButton dodajProduktPrzycisk = new JButton("Produkty");
		
		bar.add(dodajProduktPrzycisk);
		bar.add(Box.createHorizontalGlue());
		bar.add(zaloguj);
		
		 zaloguj.addActionListener(new ActionListener() {

	            public void actionPerformed(ActionEvent e) {
	            	JTextField loginField = new JTextField(5);
	                JTextField hasloField = new JTextField(5);

	                JPanel myPanel = new JPanel();
	                myPanel.add(new JLabel("Login:"));
	                myPanel.add(loginField);
	                myPanel.add(Box.createHorizontalStrut(15));
	                myPanel.add(new JLabel("Haslo:"));
	                myPanel.add(hasloField);

	                int result = JOptionPane.showConfirmDialog(null, myPanel, 
	                         "Podaj login i haslo", JOptionPane.OK_CANCEL_OPTION);

	                
	                try {
	                	if (result == JOptionPane.OK_OPTION) {
		                	if(loginField.getText().equals(placeholderLogin ) && hasloField.getText().equals(placeholderPassword)) {
		                		System.out.println("zalogowano");
		                		zaloguj.setVisible(false);
		                		bar.remove(zaloguj);
		                		
		                		bar.add(new JLabel(placeholderLogin));
		                	}
		                	else
		                	{
		                		System.out.println("nie zalogowano");
		                	}
		                }
	                    
	                } catch (Exception ex) {
	                    JOptionPane.showMessageDialog(null, "Jakis Blad.");
	                }
	                ;
	            }
	        });
		
		frame.setJMenuBar(bar);
		
		frame.setSize(600, 450);
        frame.setVisible(true);
		
	}

}
