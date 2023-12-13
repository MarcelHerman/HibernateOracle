package net.codejava;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.hibernate.Session;

public interface BudowniczyTabeli {
	
	public void dodajNaglowek();
	
	public void dodajKolumne(Object wartosc);
	
	public void dodajWiersz();
}


class BudowniczyTabeliSwing implements BudowniczyTabeli
{
	private LinkedList<String> naglowek;
	private LinkedList<LinkedList<Object>> dane = new LinkedList<LinkedList<Object>>();
	private LinkedList<Object> wiersz;
	private Obiekt_Do_Polecen obj = null;

	public void dodajNaglowek() {
		this.naglowek = new LinkedList<String>();
	}

	public void dodajKolumne(Object wartosc) {
		if(this.wiersz==null)this.naglowek.addLast(wartosc.toString());
		else this.wiersz.addLast(wartosc);
	}

	public void dodajWiersz() {
		if(this.wiersz!=null)this.dane.addLast(wiersz);
		this.wiersz = new LinkedList<Object>();
	}
	
	public void dodajPrzycisk(BudowniczyTabeli budowniczy)
	{
		/*
		this.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton clickmeButton = new JButton("Click Me");
		buttonPanel.add(clickmeButton);
		this.add(buttonPanel,BorderLayout.SOUTH);
		*/
		
		//https://stackoverflow.com/questions/11165807/put-jbutton-in-bottom-right
	}
	
	
	public JTable pobierzTabeleSwing()
	{
		if(this.wiersz!=null) this.dane.addLast(wiersz);
		if(HibernateOracle.nazwaTypu!=null && HibernateOracle.nazwaTypu.equals("Administrator"))
		{
			this.naglowek.addLast(" ");
			this.naglowek.addLast(" ");
		}
		Object[] nagl = this.naglowek.toArray();
		Object[][] dan = new Object[this.dane.size()][];
		int i = 0;
		for(LinkedList<Object> w:this.dane) dan[i++]=w.toArray();
		JTable jt = new JTable(dan, nagl);
			
		if (HibernateOracle.nazwaTypu != null && HibernateOracle.nazwaTypu.equals("Administrator")) {
            if(!(obj instanceof Produkt_Zamowienia))
            {
            	TableColumn buttonColumn = jt.getColumnModel().getColumn(naglowek.size() - 2);
            	buttonColumn.setCellRenderer(new ButtonRenderer());
            	buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
            }
			
            TableColumn buttonColumn2 = jt.getColumnModel().getColumn(naglowek.size() - 1);
            buttonColumn2.setCellRenderer(new ButtonRenderer());
            buttonColumn2.setCellEditor(new ButtonEditor(new JCheckBox()));
        }
		
		jt.setPreferredScrollableViewportSize(new Dimension(1000, 400));
		return jt;
	}
	
	private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            if (column == (naglowek.size() - 1)) {
                setText("Usuń");
            } else if(column == (naglowek.size() - 2)) {
            	setText("Edytuj");
            }      
            else{
                setText((value == null) ? "" : value.toString());
            }
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;
        
        private int id;
        
        private int id2;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            if (column == (naglowek.size() - 1)) {
            	label = "Usuń";
            } else if(column == (naglowek.size() - 2)) {
            	label = "Edytuj";            		
            }      
            else{
            	label = (value == null) ? "" : value.toString();
            }   
            this.id = Integer.parseInt((String)table.getValueAt(row, 0));
            if(obj instanceof Produkt_Magazyn) this.id2=Integer.parseInt((String)table.getValueAt(row, 1));
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
            	 if (this.label.equals("Edytuj")) { //normalna składnia
            		// Kod dla przycisku "Edytuj"
                 	JTextField pierwszyField = new JTextField(7);
	                JTextField drugiField = new JTextField(7);
	                JTextField trzeciField = new JTextField(7);
	                JTextField czwartyField = new JTextField(7);
	                JTextField piatyField = new JTextField(7);
            		 
 	                JPanel myPanel = new JPanel();
	                
	                if(obj instanceof Faktury)
	                	{	                	
		                	myPanel.add(new JLabel("NIP: "));
	                		myPanel.add(pierwszyField);             		
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj fakturę", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	 	 	                	
	     	 	                	Faktury user = (Faktury)session.createQuery("select u from Faktury u where u.id_faktury like "+Integer.toString(this.id))
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setNIP(pierwszyField.getText());	     	 	              	
	     	                		session.update(user);
	     	                			  	 	                	
	     	                		oc.closeDBSession();
	     	                	}
                		 }
                		 catch(Exception e) {
                			 e.printStackTrace();
                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować faktury. Błąd: " + e.getMessage());
                		 }	 	                	
	                	}
	                	
	                	else if(obj instanceof Kategorie)
	                	{
	                		myPanel.add(new JLabel("Nazwa kategorii: "));
	                		myPanel.add(pierwszyField);
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj kategorie", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	                		Kategorie kat = new Kategorie(pierwszyField.getText());
	     	                		kat.setId_Kategorii(this.id);
	     	                		
	     	                		if(!pierwszyField.getText().isEmpty())
	     	                			session.update(kat);
	     	                		
	     	                		oc.closeDBSession();
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować kategorii. Błąd: " + e.getMessage());
	                		 }	                		
	                	}
	                	
	                	else if(obj instanceof Uzytkownicy)
	 	                {
	                		myPanel.add(new JLabel("Nazwa użytkownika: "));
	                		myPanel.add(pierwszyField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Login: "));
	                		myPanel.add(drugiField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Hasło: "));
	                		myPanel.add(trzeciField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("E-mail: "));
	                		myPanel.add(czwartyField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Id typu użytkownika: "));
	                		myPanel.add(piatyField);
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj użytkownika", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	Uzytkownicy user = (Uzytkownicy)session.createQuery("select u from Uzytkownicy u where u.id_uzytkownika like "+Integer.toString(this.id))
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setNazwa_uzytkownika(pierwszyField.getText());
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		user.setLogin(drugiField.getText());
	     	 	                	if(!trzeciField.getText().isEmpty())
	     	 	                		user.setHaslo(trzeciField.getText());
	     	 	                	if(!czwartyField.getText().isEmpty())
	     	 	                		user.setE_mail(czwartyField.getText());
	     	 	                	if(!piatyField.getText().isEmpty())
	     	 	                		user.setId_typu_uzytkownika(Integer.parseInt(piatyField.getText()));
	     	                		session.update(user);
	     	                		
	     	                		oc.closeDBSession();
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
	                		 }
	 	                } 	 
	                	else if(obj instanceof Produkty)
	 	                {
	                		myPanel.add(new JLabel("Nazwa produktu: "));
	                		myPanel.add(pierwszyField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Cena: "));
	                		myPanel.add(drugiField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Opis: "));
	                		myPanel.add(trzeciField);

	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj produkt", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	Produkty user = (Produkty)session.createQuery("select u from Produkty u where u.id_produktu like "+Integer.toString(this.id))
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setNazwa(pierwszyField.getText());
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		user.setCena(Double.parseDouble(drugiField.getText()));
	     	 	                	if(!trzeciField.getText().isEmpty())
	     	 	                		user.setOpis(trzeciField.getText());
	     	                		session.update(user);
	     	                		
	     	                		oc.closeDBSession();
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować produktu. Błąd: " + e.getMessage());
	                		 }
	 	                } 	 
	                	else if(obj instanceof Zamowienia)
	 	                {
	                		myPanel.add(new JLabel("Miasto wysyłki"));
	                		myPanel.add(pierwszyField);	    
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Ulica wysyłki: "));
	                		myPanel.add(drugiField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Stan zamówienia: "));
	                		myPanel.add(trzeciField);

	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj zamówienie", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	Zamowienia user = (Zamowienia)session.createQuery("select u from Zamowienia u where u.id_zamowienia like "+Integer.toString(this.id))
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setAdres_wysylki_miasto(pierwszyField.getText());
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		user.setAdres_wysylki_ulica(drugiField.getText());
	     	 	                	if(!trzeciField.getText().isEmpty())
	     	 	                		user.setId_stanu_zamowienia(Integer.parseInt(trzeciField.getText()));
	     	 	                	
	     	                		session.update(user);
	     	                		
	     	                		oc.closeDBSession();
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować zamówienia. Błąd: " + e.getMessage());
	                		 }
	 	                } 	 
	                	else if(obj instanceof Magazyny)
	 	                {
	                		myPanel.add(new JLabel("Miasto: "));
	                		myPanel.add(pierwszyField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Ulica: "));
	                		myPanel.add(drugiField);
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj magazyn", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	Magazyny user = (Magazyny)session.createQuery("select u from Magazyny u where u.id_magazynu like "+Integer.toString(this.id))
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setMiasto(pierwszyField.getText());
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		user.setUlica(drugiField.getText());
	     	 	                	
	     	                		session.update(user);
	     	                		
	     	                		oc.closeDBSession();
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować magazynu. Błąd: " + e.getMessage());
	                		 }
	 	                } 
	                	else if(obj instanceof Producenci)
	 	                {
	                		myPanel.add(new JLabel("Nazwa producenta: "));
	                		myPanel.add(pierwszyField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Kontakt: "));
	                		myPanel.add(drugiField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Miasto: "));
	                		myPanel.add(trzeciField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Ulica: "));
	                		myPanel.add(czwartyField);
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj producenta", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	Producenci user = (Producenci)session.createQuery("select u from Producenci u where u.id_producenta like "+Integer.toString(this.id))
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setNazwa(pierwszyField.getText());
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		user.setKontakt(drugiField.getText());
	     	 	                	if(!trzeciField.getText().isEmpty())
	     	 	                		user.setMiasto(trzeciField.getText());
	     	 	                	if(!czwartyField.getText().isEmpty())
	     	 	                		user.setUlica(czwartyField.getText());
	     	                		session.update(user);
	     	                		
	     	                		oc.closeDBSession();
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować producenta. Błąd: " + e.getMessage());
	                		 }
	 	                } 
	                	else if(obj instanceof Produkt_Magazyn)
	 	                {
	                		myPanel.add(new JLabel("Stan faktyczny: "));
	                		myPanel.add(pierwszyField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Stan magazynowy: "));
	                		myPanel.add(drugiField);	                		
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj produkt w magazynie", JOptionPane.OK_CANCEL_OPTION); //??
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	 	                	
	     	 	                	try {	     	
	     	 	                		session.doWork(connection -> {
	     	 	                		    // Tutaj możesz bezpośrednio operować na obiekcie java.sql.Connection
	     	 	                		    Connection connectionxd = connection.unwrap(Connection.class);
	     	 	                		    // ...
	     	 	                		    // Wykonaj operacje na jdbcConnection
	     	 	                		    
	     	 	                		  DatabaseMetaData metaData = connectionxd.getMetaData();
	     	 	                		System.out.println(metaData);

		     	 	                	    // Podaj nazwę tabeli, dla której chcesz uzyskać metadane
		     	 	                	    String tableName = "PRODUKT_M%";

		     	 	                	    // Uzyskaj informacje o kolumnach dla danej tabeli
		     	 	                	    ResultSet resultSet = metaData.getColumns(null, null, tableName, null);
		     	 	                	  System.out.println(resultSet);
		     	 	                	  
		     	 	                	    // Przejdź przez wyniki i wydrukuj nazwy kolumn
		     	 	                	    while (resultSet.next()) {
		     	 	                	        String columnName = resultSet.getString("COLUMN_NAME");
		     	 	                	        System.out.println("Nazwa kolumny: " + columnName);
		     	 	                	        // Możesz zebrać te nazwy do listy lub innej struktury danych, aby je wykorzystać później
		     	 	                	    }
	     	 	                		});	     	 	                		     	 	             
	     	 	                	} catch (Exception e) {
	     	 	                		e.printStackTrace();
	   	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować produktu w magazynie. Błąd: " + e.getMessage()); // u.produkt_magazyn_id like "+Integer.toString(this.id) + " and u.produkty_Id_Produktu like " + Integer.toString(this.id2)
	     	 	                	}
	     	                		
	     	 	                	Produkt_Magazyn_Id pr = new Produkt_Magazyn_Id(this.id, this.id2);	 	                	
	     	 	                	Produkt_Magazyn user = (Produkt_Magazyn)session.createQuery("select u from Produkt_Magazyn u where u.produkt_magazyn_id = :pr")
	     	 	                			.setParameter("pr", pr)
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setStan_faktyczny(Integer.parseInt(pierwszyField.getText()));
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		user.setStan_magazynowy(Integer.parseInt(drugiField.getText()));
	     	 	                	
	     	                		session.update(user);
	     	                		
	     	 	                	
	     	 	                	
	     	 	                	System.out.println(user.getProdukt_magazyn_id() + " " + user.getStan_faktyczny() +  " " + user.getStan_magazynowy());
	     	 	                	
	     	                		oc.closeDBSession();
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
	                		 }
	 	                }
	                	else if(obj instanceof Stany_Zamowienia)
	 	                {
	                		myPanel.add(new JLabel("Nazwa: "));
	                		myPanel.add(pierwszyField);             		
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj stan zamówienia", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();  	 	                 
	     	 	                	    	 	                	
	     	 	                	Stany_Zamowienia user = (Stany_Zamowienia)session.createQuery("select u from Stany_Zamowienia u where u.id_Stanu_Zamowienia like " + Integer.toString(this.id))
	     	 	                			.uniqueResult();	     	 	                	
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setNazwa(pierwszyField.getText());	     	 	              	
	     	                		session.update(user);
	     	                		
	     	 	                	
	     	                		oc.closeDBSession();
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować stanu zamówienia. Błąd: " + e.getMessage());
	                		 }
	 	                }
	                
	                	else if(obj instanceof Typy_uzytkownika)
	 	                {
	                		myPanel.add(new JLabel("Nazwa: "));
	                		myPanel.add(pierwszyField);             		
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj typ użytkownika", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	 	 	                	
	     	 	                	Typy_uzytkownika user = (Typy_uzytkownika)session.createQuery("select u from Typy_uzytkownika u where u.id_typu_uzytkownika like "+Integer.toString(this.id))
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setNazwa(pierwszyField.getText());	     	 	              	
	     	                		session.update(user);
	     	                		
	     	 	                	
	     	                		oc.closeDBSession();
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować typu użytkownika. Błąd: " + e.getMessage());
	                		 }
	 	                }
	                	                             
                     //JOptionPane.showMessageDialog(button, "Kliknięto przycisk Edytuj");
                     
                 } else if ("Usuń".equals(this.label)) { //składnia Yody
                     // Kod dla przycisku "Usuń"
                	                       	 
                	JPanel myPanel = new JPanel();
 	                myPanel.add(new JLabel("Czy na pewno chcesz usunąć dany rekord?"));
 	                
 	               int result = JOptionPane.showConfirmDialog(null, myPanel, 
	                         "Usuwanie", JOptionPane.OK_CANCEL_OPTION);
                			
 	              try {
 	            	 if (result == JOptionPane.OK_OPTION) {
 	            		OracleConnection oc =  OracleConnection.getInstance();
 	                	oc.createDBSession();	                			
 	                	Session session = oc.getDBSession();
 	            		
 	                	if(obj instanceof Faktury)
 	                	{
 	                		Faktury pr = new Faktury();
 	 	                	pr.setId_faktury(this.id);
 	 	                	session.delete(pr);
 	                	}
 	                	
 	                	else if(obj instanceof Kategorie)
 	                	{
 	                		Kategorie pr = new Kategorie();
 	 	                	pr.setId_Kategorii(this.id);
 	 	                	session.delete(pr);
 	                	}
 	                	
 	                	else if(obj instanceof Uzytkownicy)
 	 	                {
 	 	                	Uzytkownicy pr = new Uzytkownicy();
 	 	 	                pr.setId_uzytkownika(this.id);
 	 	 	                session.delete(pr);
 	 	                } 	 
 	                	else if(obj instanceof Produkty)
 	 	                {
 	 	                	Produkty pr = new Produkty();
 	 	 	                pr.setId_produktu(this.id);
 	 	 	                session.delete(pr);
 	 	                } 	 
 	                	else if(obj instanceof Zamowienia)
 	 	                {
 	 	                	Zamowienia pr = new Zamowienia();
 	 	 	                pr.setId_zamowienia(this.id);
 	 	 	                session.delete(pr);
 	 	                } 	 
 	                	else if(obj instanceof Magazyny)
 	 	                {
 	 	                	Magazyny pr = new Magazyny();
 	 	 	                pr.setId_magazynu(this.id);
 	 	 	                session.delete(pr);
 	 	                } 
 	                	else if(obj instanceof Producenci)
 	 	                {
 	                		Producenci pr = new Producenci();
 	 	 	                pr.setId_producenta(this.id);
 	 	 	                session.delete(pr);
 	 	                } 
 	            		 
 	            		 oc.closeDBSession();
 	            	 } 	            	  
 	              }catch(Exception e) {	            	  
 	              }           			                	
                 }
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
	

	void tworzTabeleMagazyny(List<Obiekt_Do_Polecen> entities)
	{
		this.obj = new Magazyny();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Miasto");
		this.dodajKolumne("Ulica");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Magazyny) entry).getId_magazynu()));
			this.dodajKolumne(((Magazyny) entry).getMiasto().toString());
			this.dodajKolumne(((Magazyny) entry).getUlica().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			}
		}
	}
	
	void tworzTabeleProdukty(List<Obiekt_Do_Polecen> entities)
	{
		this.obj = new Produkty();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Nazwa");
		this.dodajKolumne("Cena");
		this.dodajKolumne("Opis");		
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Produkty) entry).getId_produktu()));
			this.dodajKolumne(((Produkty) entry).getNazwa().toString());
			this.dodajKolumne(Double.toString(((Produkty) entry).getCena()));
			this.dodajKolumne(((Produkty) entry).getOpis().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			}
		}
	}
	
	void tworzTabeleKategorie(List<Obiekt_Do_Polecen> entities)
	{
		this.obj = new Kategorie();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Nazwa");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Kategorie) entry).getId_Kategorii()));
			this.dodajKolumne(((Kategorie) entry).getNazwa().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			default:
				break;
			}
		}
	}
	
	void tworzTabeleFaktury(List<Obiekt_Do_Polecen> entities)
	{
		this.obj = new Faktury();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Data wystawienia");
		this.dodajKolumne("NIP");
		this.dodajKolumne("Id zamówienia");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Faktury) entry).getId_faktury()));
			this.dodajKolumne(((Faktury) entry).getData_wystawienia().toString());
			this.dodajKolumne(((Faktury) entry).getNIP().toString());
			this.dodajKolumne(Integer.toString(((Faktury) entry).getZamowienia_id_zamowienia()));
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			}
		}
	}
	
	void tworzTabeleProdukt_Magazyn(List<Obiekt_Do_Polecen> entities)
	{
		this.obj = new Produkt_Magazyn();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Id Magazynu");
		this.dodajKolumne("Id Produktu");
		this.dodajKolumne("Stan faktyczny");
		this.dodajKolumne("Stan magazynowy");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Produkt_Magazyn) entry).getMagazyn_id()));
			this.dodajKolumne(Integer.toString(((Produkt_Magazyn) entry).getProdukt_id()));			
			this.dodajKolumne(Double.toString(((Produkt_Magazyn) entry).getStan_faktyczny()));
			this.dodajKolumne(Double.toString(((Produkt_Magazyn) entry).getStan_magazynowy()));
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			}
		}
	}
	
	void tworzTabeleProdukt_Zamowienia(List<Obiekt_Do_Polecen> entities)
	{
		this.obj = new Produkt_Zamowienia();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Id Zamowienia");
		this.dodajKolumne("Id Produktu");
		this.dodajKolumne("Ilosc");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Produkt_Zamowienia) entry).getZamowienieId()));
			this.dodajKolumne(Integer.toString(((Produkt_Zamowienia) entry).getProduktId()));
			this.dodajKolumne(Double.toString(((Produkt_Zamowienia) entry).getIlosc()));
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			}
		}
	}
	
	void tworzTabeleStany_Zamowienia(List<Obiekt_Do_Polecen> entities)
	{
		this.obj = new Stany_Zamowienia();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Id Stanu Zamówienia");
		this.dodajKolumne("Nazwa");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Stany_Zamowienia) entry).getId_Stanu_Zamowienia()));
			this.dodajKolumne(((Stany_Zamowienia) entry).getNazwa().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			}
		}
	}
	
	void tworzTabeleProducenci(List<Obiekt_Do_Polecen> entities)
	{
		this.obj = new Producenci();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Nazwa");
		this.dodajKolumne("Kontakt");
		this.dodajKolumne("Miasto");
		this.dodajKolumne("Ulica");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Producenci) entry).getId_producenta()));
			this.dodajKolumne(((Producenci) entry).getNazwa().toString());
			this.dodajKolumne(((Producenci) entry).getKontakt().toString());
			this.dodajKolumne(((Producenci) entry).getMiasto().toString());
			this.dodajKolumne(((Producenci) entry).getUlica().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			}
		}
	}
	
	void tworzTabeleUzytkownicy(List<Obiekt_Do_Polecen> entities)
	{	
		this.obj = new Uzytkownicy();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Nazwa użytkownika");
		this.dodajKolumne("Login");
		this.dodajKolumne("Hasło");
		this.dodajKolumne("E-mail");
		this.dodajKolumne("Typ konta");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Uzytkownicy) entry).getId_uzytkownika()));
			this.dodajKolumne(((Uzytkownicy) entry).getNazwa_uzytkownika().toString());
			this.dodajKolumne(((Uzytkownicy) entry).getLogin().toString());
			this.dodajKolumne(((Uzytkownicy) entry).getHaslo().toString());
			this.dodajKolumne(((Uzytkownicy) entry).getE_mail().toString());
			this.dodajKolumne(((Uzytkownicy) entry).getId_typu_uzytkownika());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			}
		}
	}

	void tworzTabeleZamowienia(List<Obiekt_Do_Polecen> entities)
	{
		this.obj = new Zamowienia();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Miasto wysyłki");
		this.dodajKolumne("Ulica wysyłki");
		this.dodajKolumne("Koszt");
		this.dodajKolumne("Id stanu zamowienia");
		this.dodajKolumne("Id Użytkownika");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Zamowienia) entry).getId_zamowienia()));
			this.dodajKolumne(((Zamowienia) entry).getAdres_wysylki_miasto().toString());
			this.dodajKolumne(((Zamowienia) entry).getAdres_wysylki_ulica().toString());
			this.dodajKolumne(Double.toString(((Zamowienia) entry).getKoszt()));
			this.dodajKolumne(Integer.toString(((Zamowienia) entry).getId_stanu_zamowienia()));
			this.dodajKolumne(Integer.toString(((Zamowienia) entry).getUzytkownicy_id_uzytkownika()));
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			}
		}
	}
	
	void tworzTabeleTypy_uzytkownika(List<Obiekt_Do_Polecen> entities)
    {
		this.obj = new Typy_uzytkownika();
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
        this.dodajNaglowek();

        this.dodajKolumne("Lp.");
        this.dodajKolumne("Nazwa");

        for(Obiekt_Do_Polecen entry: entities)
        {
            this.dodajWiersz();
            this.dodajKolumne(Integer.toString(((Typy_uzytkownika) entry).getId_typu_uzytkownika()));
            this.dodajKolumne(((Typy_uzytkownika) entry).getNazwa().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				this.dodajKolumne("");
				this.dodajKolumne("");
				break;
			}
        }
    }
	
}
