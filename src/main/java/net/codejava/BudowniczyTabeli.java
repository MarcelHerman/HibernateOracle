package net.codejava;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

public interface BudowniczyTabeli {
	
	public void dodajNaglowek();
	
	public void dodajKolumne(Object wartosc);
	
	public void dodajWiersz();
	
	public Object pobierzTabele(Object ob);
	
	public void refresh();
	
	public void dodajKolumnePrzycisku();
}

class BudowniczyTabeliDruk implements BudowniczyTabeli
{
	private LinkedList<String> naglowek;
	private LinkedList<LinkedList<Object>> dane = new LinkedList<LinkedList<Object>>();
	private LinkedList<Object> wiersz;

	public void refresh() {
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
	}
	
	public void dodajKolumnePrzycisku() {}
	
	@Override
	public void dodajNaglowek() {
		this.naglowek = new LinkedList<String>();	
	}

	@Override
	public void dodajKolumne(Object wartosc) {
		if(this.wiersz==null)this.naglowek.addLast(wartosc.toString());
		else this.wiersz.addLast(wartosc);		
	}

	@Override
	public void dodajWiersz() {
		if(this.wiersz!=null)this.dane.addLast(wiersz);
		this.wiersz = new LinkedList<Object>();
	} 

	@Override
	public Object pobierzTabele(Object ob) {
		if (this.wiersz != null) {
	        this.dane.addLast(wiersz);
	    }

	    Object[] nagl = this.naglowek.toArray();
	    Object[][] dan = new Object[this.dane.size()][];
	    int i = 0;
	    for (LinkedList<Object> w : this.dane) {
	        dan[i++] = w.toArray();
	    }

	    // Tworzenie łańcucha znaków dla nagłówków
	    String naglowki = String.join(", ", (Iterable<? extends CharSequence>) Arrays.asList(nagl)) + "\n";

	    // Tworzenie łańcucha znaków dla danych
	    String daneString = "Wykaz " +HibernateOracle.obj.getClass().getSimpleName() + " z dnia " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +"\n"+ naglowki;
	    for (Object[] row : dan) {
	        String rowString = String.join(", ", (Iterable<? extends CharSequence>) Arrays.asList(row)) + "\n";
	        daneString += rowString;
	    }

	    return daneString;
	}
}


class BudowniczyTabeliSwing implements BudowniczyTabeli
{
	private LinkedList<String> naglowek;
	private LinkedList<LinkedList<Object>> dane = new LinkedList<LinkedList<Object>>();
	private LinkedList<Object> wiersz;
	
	public void dodajKolumnePrzycisku() {
			this.dodajKolumne("");
	}

	public void refresh() {
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
	}
	
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
	
	public JTable dodajPrzycisk(JTable jt)
	{
		if(HibernateOracle.obj instanceof Produkty && !(HibernateOracle.nazwaTypu.equals("null"))){
			TableColumn buttonColumn = jt.getColumnModel().getColumn(naglowek.size() - 1);
        	buttonColumn.setCellRenderer(new ButtonRenderer());
        	buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
        	if (HibernateOracle.nazwaTypu != null && HibernateOracle.nazwaTypu.equals("Pracownik")) {
    			if(!(HibernateOracle.obj instanceof Typy_uzytkownika) && !(HibernateOracle.obj instanceof Stany_Zamowienia))
                {              	
                	TableColumn buttonColumn3 = jt.getColumnModel().getColumn(naglowek.size() - 2);
                	buttonColumn3.setCellRenderer(new ButtonRenderer());
                	buttonColumn3.setCellEditor(new ButtonEditor(new JCheckBox()));
                }
    			
            }else if(HibernateOracle.nazwaTypu != null && HibernateOracle.nazwaTypu.equals("Administrator"))
            {
            	if(!(HibernateOracle.obj instanceof Typy_uzytkownika) && !(HibernateOracle.obj instanceof Stany_Zamowienia))
                {
                	TableColumn buttonColumn2 = jt.getColumnModel().getColumn(naglowek.size() - 3);
                	buttonColumn2.setCellRenderer(new ButtonRenderer());
                	buttonColumn2.setCellEditor(new ButtonEditor(new JCheckBox()));
                	
                	TableColumn buttonColumn3 = jt.getColumnModel().getColumn(naglowek.size() - 2);
                	buttonColumn3.setCellRenderer(new ButtonRenderer());
                	buttonColumn3.setCellEditor(new ButtonEditor(new JCheckBox()));
                }
            }
		}
		else {
			if (HibernateOracle.nazwaTypu != null && HibernateOracle.nazwaTypu.equals("Administrator")) {
				if(!(HibernateOracle.obj instanceof Typy_uzytkownika) && !(HibernateOracle.obj instanceof Stany_Zamowienia))
	            {
					if(!(HibernateOracle.obj instanceof Produkt_Zamowienia) && !(HibernateOracle.obj instanceof Zamowienia))
					{
						TableColumn buttonColumn = jt.getColumnModel().getColumn(naglowek.size() - 2);
						buttonColumn.setCellRenderer(new ButtonRenderer());
						buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox()));						
					}
	            	
	            	
	            	TableColumn buttonColumn2 = jt.getColumnModel().getColumn(naglowek.size() - 1);
	            	buttonColumn2.setCellRenderer(new ButtonRenderer());
	            	buttonColumn2.setCellEditor(new ButtonEditor(new JCheckBox()));
	            }
				
	        }
			else if(HibernateOracle.nazwaTypu.equals("Pracownik")) {
				if(HibernateOracle.obj instanceof Zamowienia) {
	            	TableColumn buttonColumn2 = jt.getColumnModel().getColumn(naglowek.size() - 1);
	            	buttonColumn2.setCellRenderer(new ButtonRenderer());
	            	buttonColumn2.setCellEditor(new ButtonEditor(new JCheckBox()));
				}
				if(HibernateOracle.obj instanceof Produkt_Magazyn) {
					TableColumn buttonColumn = jt.getColumnModel().getColumn(naglowek.size() - 1);
					buttonColumn.setCellRenderer(new ButtonRenderer());
					buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox()));		
				}
			}						
		}
		return jt;
	}

	
	
	public Object pobierzTabele(Object ob)
	{
		if(this.wiersz!=null) this.dane.addLast(wiersz);
		if(HibernateOracle.nazwaTypu!=null && HibernateOracle.nazwaTypu.equals("Administrator"))
		{
			if(!(HibernateOracle.obj instanceof Typy_uzytkownika) && !(HibernateOracle.obj instanceof Stany_Zamowienia))
			{
				this.naglowek.addLast(" ");
				if(!(HibernateOracle.obj instanceof Zamowienia) &&  !(HibernateOracle.obj instanceof Produkt_Zamowienia))this.naglowek.addLast(" ");				
			}
		}
		
		if(HibernateOracle.obj instanceof Produkty && !(HibernateOracle.nazwaTypu.equals("null")))
		{
			this.naglowek.addLast(" ");
			if(HibernateOracle.nazwaTypu.equals("Pracownik"))this.naglowek.addLast(" ");
		}
			
		if(!(HibernateOracle.nazwaTypu.equals("null")))
		{
			if(HibernateOracle.nazwaTypu.equals("Pracownik"))
			{
				if(HibernateOracle.obj instanceof Zamowienia)this.naglowek.addLast(" ");
				if(HibernateOracle.obj instanceof Produkt_Magazyn) {
					this.naglowek.addLast(" ");
				}
			}
		}

			
		
		Object[] nagl = this.naglowek.toArray();
		Object[][] dan = new Object[this.dane.size()][];
		int i = 0;
		for(LinkedList<Object> w:this.dane) dan[i++]=w.toArray();
		
		DefaultTableModel model = new DefaultTableModel(dan,nagl);
		
		JTable jt = new JTable(model);
			
		jt = dodajPrzycisk(jt);
		
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
            if(HibernateOracle.obj instanceof Produkty)
            {
            	if (column == (naglowek.size() - 1)) {
            		setText("Dodaj do koszyka"); ;
            	} else if(column == (naglowek.size() - 2)) {
            		if(HibernateOracle.nazwaTypu.equals("Pracownik"))setText("Edytuj");
            		else setText("Usuń");           		
	            } else if(column == (naglowek.size()-3)){
	            	setText("Edytuj");
	            }
	            else{
	            	setText((value == null) ? "" : value.toString());
	            } 
            }
            else if(HibernateOracle.obj instanceof Produkt_Magazyn) {
            	
            	if(HibernateOracle.nazwaTypu.equals("Pracownik"))
            		if (column == (naglowek.size() - 1)) 
            			setText("Edytuj");
            		else {}
            	else if(HibernateOracle.nazwaTypu.equals("Administrator")) {
            		if (column == (naglowek.size() - 1)) 
            			setText("Usuń");
            		if (column == (naglowek.size() - 2)) 
            			setText("Edytuj");
            	}
            }
            else if(HibernateOracle.obj instanceof Zamowienia)
            {
            	if (column == (naglowek.size() - 1)) {
            		setText("Edytuj");
            	}    
	            else{
	            	setText((value == null) ? "" : value.toString());
	            } 
            }
            else
            {
            	if (column == (naglowek.size() - 1)) {
            		setText("Usuń");
            	} else if(column == (naglowek.size() - 2)) {
            		setText("Edytuj");           		
	            }      
	            else{
	            	setText((value == null) ? "" : value.toString());
	            } 
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
        
        private JTable tab;
        
        private int row;

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
            if(HibernateOracle.obj instanceof Produkty)
            {
            	if (column == (naglowek.size() - 1)) {
            		label = "Dodaj do koszyka";
            	} else if(column == (naglowek.size() - 2)) {
            		if(HibernateOracle.nazwaTypu.equals("Pracownik"))label = "Edytuj";
            		else label = "Usuń";         		
	            } else if(column == (naglowek.size()-3)){
	            	label = "Edytuj";
	            }
	            else{
	            	label = (value == null) ? "" : value.toString();
	            } 
            }
            	else if(HibernateOracle.obj instanceof Produkt_Magazyn) {
            	
            	if (column == (naglowek.size() - 1)) {
            		if(HibernateOracle.nazwaTypu.equals("Pracownik"))label = "Edytuj";
            		if(HibernateOracle.nazwaTypu.equals("Administrator"))label = "Usuń";
            	}
            	if(column == (naglowek.size() -2)) {
            		if(HibernateOracle.nazwaTypu.equals("Administrator"))label = "Edytuj";
            	}
            }
            else if(HibernateOracle.obj instanceof Zamowienia)
            {
            	if (column == (naglowek.size() - 1)) {
            		label = "Edytuj";
            	}    
	            else{
	            	label = (value == null) ? "" : value.toString();
	            } 
            }
            else
            {
            	if (column == (naglowek.size() - 1)) {
            		label = "Usuń";
            	} else if(column == (naglowek.size() - 2) ) {
	            	label = "Edytuj";            		
	            }      
	            else{
	            	label = (value == null) ? "" : value.toString();
	            } 
            }
              
            this.id = Integer.parseInt((String)table.getValueAt(row, 0));
            if(HibernateOracle.obj instanceof Produkt_Magazyn || HibernateOracle.obj instanceof Produkt_Zamowienia) this.id2=Integer.parseInt((String)table.getValueAt(row, 1));
            button.setText(label);
            isPushed = true;
            tab = table;
            this.row = row;
            
            
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
	                
	                if(HibernateOracle.obj instanceof Faktury)
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
	     	                		
	     	 	                	 	 	                	
	     	 	                	Faktury user = (Faktury)session.createQuery("select u from Faktury u where u.id_faktury = :id")
	     	 	                			.setParameter("id", this.id)
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setNIP(pierwszyField.getText());	     	 	              	
	     	                		session.update(user);
	     	                			  	 	                	
	     	                		oc.closeDBSession();
	     	                		
	     	                		tab.setValueAt(user.getNIP(),row,2);
	     	                	}
                		 }
                		 catch(Exception e) {
                			 e.printStackTrace();
                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować faktury. Błąd: " + e.getMessage());
                		 }	 	                	
	                	}
	                	
	                	else if(HibernateOracle.obj instanceof Kategorie)
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
	     	                		oc.closeDBSession();
	     	                		if(!pierwszyField.getText().isEmpty())
	     	                		{
	     	                			//session.update(kat);
	     	                			HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(kat, HibernateOracle.idUzytkownika));
	     	                		}

	     	                		
	     	                		//oc.closeDBSession();
	     	                		tab.setValueAt(kat.getNazwa(), row, 1);
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować kategorii. Błąd: " + e.getMessage());
	                		 }	                		
	                	}
	                	
	                	else if(HibernateOracle.obj instanceof Uzytkownicy)
	 	                {
	                		OracleConnection oc =  OracleConnection.getInstance();
			                oc.createDBSession();

			                List<Obiekt_Do_Polecen> fData = null;

			                try (Session session = oc.getDBSession()) {
			                    Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Typy_uzytkownika", Obiekt_Do_Polecen.class);
			                    fData = query.getResultList();
			                    oc.closeDBSession();
			                } catch (Exception e) {
			                    e.printStackTrace();
			                    System.out.println(e);
			                }
			                
			                String nazwy[] = new String[fData.size()]; 
			                
			                int i=0;
			                for(Obiekt_Do_Polecen stan: fData) {
			                	//nazwy[((Stany_Zamowienia)stan).getId_Stanu_Zamowienia()-1] = ((Stany_Zamowienia)stan).getNazwa();
			                	nazwy[i] = ((Typy_uzytkownika)stan).getNazwa();
			                	i++;
			                }
			                
			                JComboBox jombo = new JComboBox(nazwy);
			                JCheckBox czyUsunietyCheck = new JCheckBox("Czy usunięty: ");
			                
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
	                		myPanel.add(new JLabel("Typ użytkownika: "));
	                		myPanel.add(jombo);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(czyUsunietyCheck);
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj użytkownika", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	Uzytkownicy user = (Uzytkownicy)session.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
	     	 	                			.setParameter("id", this.id)
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	user.setCzy_usunieto(czyUsunietyCheck.isSelected()?1:0);
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setNazwa_uzytkownika(pierwszyField.getText());
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		user.setLogin(drugiField.getText());
	     	 	                	if(!trzeciField.getText().isEmpty())
	     	 	                		user.setHaslo(trzeciField.getText());
	     	 	                	if(!czwartyField.getText().isEmpty())
	     	 	                		user.setE_mail(czwartyField.getText());
	     	 	                	user.setId_typu_uzytkownika(((Typy_uzytkownika)fData.get(jombo.getSelectedIndex())).getId_typu_uzytkownika());
	     	 	                	
	     	                		session.update(user);
	     	                		
	     	                		oc.closeDBSession();
	     	                		
	     	                		tab.setValueAt(user.getNazwa_uzytkownika(), row, 1);
	     	                		tab.setValueAt(user.getLogin(), row, 2);
	     	                		tab.setValueAt(user.getHaslo(), row, 3);
	     	                		tab.setValueAt(user.getE_mail(), row, 4);
	     	                		tab.setValueAt(((Typy_uzytkownika)fData.get(jombo.getSelectedIndex())).getNazwa(), row, 5);
	     	                		if(user.getCzy_usunieto() == 1)
	     	                			tab.setValueAt("TAK", row, 6);
	     	                		else   	                		
	     	                			tab.setValueAt("NIE", row, 6);
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
	                		 }
	 	                } 	 
	                	else if(HibernateOracle.obj instanceof Produkty)
	 	                {
	                		OracleConnection oc =  OracleConnection.getInstance();
			                oc.createDBSession();

			                List<Obiekt_Do_Polecen> fData = null;

			                try (Session session = oc.getDBSession()) {
			                    Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Kategorie", Obiekt_Do_Polecen.class);
			                    fData = query.getResultList();
			                    oc.closeDBSession();
			                } catch (Exception e) {
			                    e.printStackTrace();
			                    System.out.println(e);
			                }
			                
			                String nazwy[] = new String[fData.size()]; 
			                
			                int i=0;
			                for(Obiekt_Do_Polecen stan: fData) {
			                	//nazwy[((Stany_Zamowienia)stan).getId_Stanu_Zamowienia()-1] = ((Stany_Zamowienia)stan).getNazwa();
			                	nazwy[i] = ((Kategorie)stan).getNazwa();
			                	i++;
			                }
			                
			                JComboBox jombo = new JComboBox(nazwy);
			                JCheckBox czyUsunietyCheck = new JCheckBox("Czy usunięty: ");
			                
	                		myPanel.add(new JLabel("Nazwa produktu: "));
	                		myPanel.add(pierwszyField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Cena: "));
	                		myPanel.add(drugiField);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(new JLabel("Opis: "));
	                		myPanel.add(trzeciField);
	                		myPanel.add(new JLabel("Kategoria: "));
	                		myPanel.add(jombo);
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(czyUsunietyCheck);

	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj produkt", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	Produkty user = (Produkty)session.createQuery("select u from Produkty u where u.id_produktu = :id")
	     	 	                			.setParameter("id", this.id)
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	user.setCzy_usunieto(czyUsunietyCheck.isSelected()?1:0);
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setNazwa(pierwszyField.getText());
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		if(Double.parseDouble(drugiField.getText())<=0)
	     	 	                			throw(new Exception("Nie można dodać ujemnej ceny, ani ceny równej 0"));
	     	 	                		else
	     	 	                			user.setCena(Double.parseDouble(drugiField.getText()));
	     	 	                	if(!trzeciField.getText().isEmpty())
	     	 	                		user.setOpis(trzeciField.getText());
	     	 	                	if(!czwartyField.getText().isEmpty())
	     	 	                		user.setKategorie_id_kategorii(Integer.parseInt(czwartyField.getText()));
	     	 	                	
	     	 	                	user.setKategorie_id_kategorii(((Kategorie)fData.get(jombo.getSelectedIndex())).getId_Kategorii());
	     	                		session.update(user);
	     	                		
	     	                		oc.closeDBSession();
	     	                		
	     	                		tab.setValueAt(user.getNazwa(), row, 1);
	     	                		tab.setValueAt(user.getCena(), row, 2);
	     	                		tab.setValueAt(user.getOpis(), row, 3);
	     	                		tab.setValueAt(((Kategorie)fData.get(jombo.getSelectedIndex())).getNazwa() ,row, 5);
	     	                		if(user.getCzy_usunieto() == 1)
	     	                			tab.setValueAt("TAK", row, 6);
	     	                		else   	                		
	     	                			tab.setValueAt("NIE", row, 6);
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować produktu. Błąd: " + e.getMessage());
	                		 }
	 	                } 	 
	                	else if(HibernateOracle.obj instanceof Zamowienia)
	 	                {
	                		
	                		OracleConnection oc =  OracleConnection.getInstance();
			                oc.createDBSession();

			                List<Obiekt_Do_Polecen> fData = null;

			                try (Session session = oc.getDBSession()) {
			                    Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Stany_Zamowienia", Obiekt_Do_Polecen.class);
			                    fData = query.getResultList();
			                    oc.closeDBSession();
			                } catch (Exception e) {
			                    e.printStackTrace();
			                    System.out.println(e);
			                }
			                
			                String nazwy[] = new String[fData.size()]; 
			                
			                int i=0;
			                for(Obiekt_Do_Polecen stan: fData) {
			                	//nazwy[((Stany_Zamowienia)stan).getId_Stanu_Zamowienia()-1] = ((Stany_Zamowienia)stan).getNazwa();
			                	nazwy[i] = ((Stany_Zamowienia)stan).getNazwa();
			                	i++;
			                }
			                
			                JComboBox jombo = new JComboBox(nazwy);
	                		
			                if(HibernateOracle.nazwaTypu.equals("Pracownik"))
			                {
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel("Stan zamówienia: "));
		                		myPanel.add(jombo);
			                }
			                
			                else
			                {
		                		myPanel.add(new JLabel("Miasto wysyłki"));
		                		myPanel.add(pierwszyField);	    
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel("Ulica wysyłki: "));
		                		myPanel.add(drugiField);
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel("Stan zamówienia: "));
		                		myPanel.add(jombo);
			                }


	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj zamówienie", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	Zamowienia user = (Zamowienia)session.createQuery("select u from Zamowienia u where u.id_zamowienia = :id")
	     	 	                			.setParameter("id", this.id)
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setAdres_wysylki_miasto(pierwszyField.getText());
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		user.setAdres_wysylki_ulica(drugiField.getText());
	     	 	                	
	     	 	                	user.setId_stanu_zamowienia(((Stany_Zamowienia)fData.get(jombo.getSelectedIndex())).getId_Stanu_Zamowienia());
	     	 	                	
	     	                		session.update(user);
	     	                		
	     	                		oc.closeDBSession();
	     	                		
	     	                			tab.setValueAt(user.getAdres_wysylki_miasto(), row, 1); 	                		
	     	                			tab.setValueAt(user.getAdres_wysylki_ulica(), row, 2);
	     	                			tab.setValueAt(((Stany_Zamowienia)fData.get(jombo.getSelectedIndex())).getNazwa(), row, 4);
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować zamówienia. Błąd: " + e.getMessage());
	                		 }
	 	                } 	 
	                	else if(HibernateOracle.obj instanceof Magazyny)
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
	     	                		
	     	 	                	Magazyny user = (Magazyny)session.createQuery("select u from Magazyny u where u.id_magazynu = :id")
	     	 	                			.setParameter("id", this.id)
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		user.setMiasto(pierwszyField.getText());
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		user.setUlica(drugiField.getText());
	     	 	                	
	     	                		session.update(user);
	     	                		
	     	                		oc.closeDBSession();
	     	                		tab.setValueAt(user.getMiasto(), row, 1); 	                		
     	                			tab.setValueAt(user.getUlica(), row, 2);
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować magazynu. Błąd: " + e.getMessage());
	                		 }
	 	                } 
	                	else if(HibernateOracle.obj instanceof Producenci)
	 	                {
	                		
	                		JCheckBox czyUsunietyCheck = new JCheckBox("Czy usunięty: ");
	                		
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
	                		myPanel.add(Box.createHorizontalStrut(5));
	                		myPanel.add(czyUsunietyCheck);
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj producenta", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     	                		OracleConnection oc =  OracleConnection.getInstance();
	     	 	                	oc.createDBSession();
	     	 	                	Session session = oc.getDBSession();
	     	                		
	     	 	                	Producenci user = (Producenci)session.createQuery("select u from Producenci u where u.id_producenta = :id")
	     	 	                			.setParameter("id", this.id)
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	user.setCzy_usunieto(czyUsunietyCheck.isSelected()?1:0);
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
	     	                		
	     	                		tab.setValueAt(user.getNazwa(), row, 1); 	                		
     	                			tab.setValueAt(user.getKontakt(), row, 2);
     	                			tab.setValueAt(user.getMiasto(), row, 3); 	                		
     	                			tab.setValueAt(user.getUlica(), row, 4);
     	                			if(user.getCzy_usunieto() == 1)
	     	                			tab.setValueAt("TAK", row, 5);
	     	                		else   	                		
	     	                			tab.setValueAt("NIE", row, 5);
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować producenta. Błąd: " + e.getMessage());
	                		 }
	 	                } 
	                	else if(HibernateOracle.obj instanceof Produkt_Magazyn)
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
									 	     	                		
	     	 	                	Produkt_Magazyn_Id pr = new Produkt_Magazyn_Id(this.id, this.id2);	 	                	
	     	 	                	Produkt_Magazyn user = (Produkt_Magazyn)session.createQuery("select u from Produkt_Magazyn u where u.produkt_magazyn_id = :pr")
	     	 	                			.setParameter("pr", pr)
	     	 	                			.uniqueResult();
	     	 	                	//System.out.println(user.getId_uzytkownika());
	     	 	                	
	     	 	                	
	     	 	                	if(!pierwszyField.getText().isEmpty())
	     	 	                		if(Integer.parseInt(pierwszyField.getText())<0)
	     	 	                			throw(new Exception("Nie można dodać ujemnego stanu faktycznego."));
	     	 	                		else
	     	 	                			user.setStan_faktyczny(Integer.parseInt(pierwszyField.getText()));
	     	 	                		
	     	 	                	if(!drugiField.getText().isEmpty())
	     	 	                		if(Integer.parseInt(drugiField.getText())<0)
	     	 	                			throw(new Exception("Nie można dodać ujemnego stanu magazynowego."));
	     	 	                		else
	     	 	                			user.setStan_magazynowy(Integer.parseInt(drugiField.getText()));
	     	 	                	
	     	                		session.update(user);
	     	                		
	     	 	                	
	     	 	                	
	     	 	                	System.out.println(user.getProdukt_magazyn_id() + " " + user.getStan_faktyczny() +  " " + user.getStan_magazynowy());
	     	 	                	
	     	                		oc.closeDBSession();
	     	                		
	     	                		tab.setValueAt(user.getStan_faktyczny(), row, 2); 	                		
     	                			tab.setValueAt(user.getStan_magazynowy(), row, 3);
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
	                		 }
	 	                }
	                	else if(HibernateOracle.obj instanceof Stany_Zamowienia)
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
	     	 	                	    	 	                	
	     	 	                	Stany_Zamowienia user = (Stany_Zamowienia)session.createQuery("select u from Stany_Zamowienia u where u.id_Stanu_Zamowienia = :id")
	     	 	                			.setParameter("id", this.id)
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
	                
	                	else if(HibernateOracle.obj instanceof Typy_uzytkownika)
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
	     	                		
	     	 	                	 	 	                	
	     	 	                	Typy_uzytkownika user = (Typy_uzytkownika)session.createQuery("select u from Typy_uzytkownika u where u.id_typu_uzytkownika = :id")
	     	 	                			.setParameter("id", this.id)
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
	 	                }else if(HibernateOracle.obj instanceof Produkt_Koszyk)
	 	                {
	                		myPanel.add(new JLabel("Ilosc: "));
	                		myPanel.add(pierwszyField);             		
	                		
	                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj typ użytkownika", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	 	                	if(!pierwszyField.getText().isEmpty() && Integer.parseInt(pierwszyField.getText()) > 0)
	     	 	                	{
	     	 	                		for(Obiekt_Do_Polecen pk: HibernateOracle.koszyk) {
	     	 	                			if(((Produkt_Koszyk)pk).getPr().getId_produktu() == id) {
	     	 	                				((Produkt_Koszyk)pk).setIlosc(Integer.parseInt(pierwszyField.getText()));
	     	 	                				tab.setValueAt(Integer.parseInt(pierwszyField.getText()), row, 3); 	  
	     	 	                				tab.setValueAt(Integer.parseInt(pierwszyField.getText()) *((Produkt_Koszyk)pk).getPr().getCena() , row, 4);
	     	 	                				break;
	     	 	                			}
	     	 	                		}
	     	 	                	}
	     	 	                	else
	     	 	                		throw new Exception("Nie podano wartosci lub podano niedopuszczalna liczbe");
	     	 	                		 	 	              	
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować wartosci. Błąd: " + e.getMessage());
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
 	            		
 	                	if(HibernateOracle.obj instanceof Faktury)
 	                	{
 	                		Faktury pr = new Faktury();
 	 	                	pr.setId_faktury(this.id);
 	 	                	session.delete(pr);
 	 	                	oc.closeDBSession();
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	                	}
 	                	
 	                	else if(HibernateOracle.obj instanceof Kategorie)
 	                	{
 	                		Kategorie pr = new Kategorie();
 	 	                	pr.setId_Kategorii(this.id);
 	 	                	//session.delete(pr);
 	 	                	oc.closeDBSession();
 	 	                	HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	                	}
 	                	
 	                	else if(HibernateOracle.obj instanceof Uzytkownicy)
 	 	                {
 	                		Uzytkownicy pr = (Uzytkownicy)session.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
 	 	                			.setParameter("id", this.id)
 	 	                			.uniqueResult();
 	                		
 	 	 	             pr.setCzy_usunieto(1);
	 	 	                session.update(pr);
	 	 	              tab.setValueAt("TAK", row, 6);
	 	 	            oc.closeDBSession();
 	 	 	                
 	 	                } 	 
 	                	else if(HibernateOracle.obj instanceof Produkty)
 	 	                {	
 	 	                	Produkty pr = (Produkty)session.createQuery("select u from Produkty u where u.id_produktu = :id")
 	 	                			.setParameter("id", this.id)
 	 	                			.uniqueResult();
 	 	 	                pr.setCzy_usunieto(1);
 	 	 	                
 	 	 	                session.update(pr);
 	 	 	                tab.setValueAt("TAK", row, 6);
 	 	 	             oc.closeDBSession();
 	 	                } 	 
 	                	else if(HibernateOracle.obj instanceof Zamowienia)
 	 	                {
 	 	                	Zamowienia pr = new Zamowienia();
 	 	 	                pr.setId_zamowienia(this.id);
 	 	 	                session.delete(pr);
 	 	 	             oc.closeDBSession();
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	 	                } 	 
 	                	else if(HibernateOracle.obj instanceof Magazyny)
 	 	                {
 	 	                	Magazyny pr = new Magazyny();
 	 	 	                pr.setId_magazynu(this.id);
 	 	 	                session.delete(pr);
 	 	 	             oc.closeDBSession();
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	 	                } 
 	                	else if(HibernateOracle.obj instanceof Producenci)
 	 	                {
 	                		Producenci pr = (Producenci)session.createQuery("select u from Producenci u where u.id_producenta = :id")
 	 	                			.setParameter("id", this.id)
 	 	                			.uniqueResult();
 	 	 	             pr.setCzy_usunieto(1);
	 	 	                session.update(pr);
	 	 	              tab.setValueAt("TAK", row, 5);
	 	 	            oc.closeDBSession();
 	 	                } 
 	                	else if(HibernateOracle.obj instanceof Produkt_Koszyk)
 	 	                {
 	                		for(Obiekt_Do_Polecen pk: HibernateOracle.koszyk) {
	 	                			if(((Produkt_Koszyk)pk).getPr().getId_produktu() == id) {
	 	                				HibernateOracle.koszyk.remove(pk);
	 	                				break;
	 	                			}
	 	                		}
 	                		oc.closeDBSession();
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	 	                }
 	                	else if(HibernateOracle.obj instanceof Produkt_Magazyn)
 	 	                {
 	                		Produkt_Magazyn pr = new Produkt_Magazyn();
 	 	 	                pr.setProdukt_magazyn_id(new Produkt_Magazyn_Id(this.id, this.id2));
 	 	 	                session.delete(pr);
 	 	 	             oc.closeDBSession();
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	 	                } 
 	                	else if(HibernateOracle.obj instanceof Produkt_Zamowienia)
 	 	                {
 	                		Produkt_Zamowienia pr = new Produkt_Zamowienia();
 	 	 	                pr.setProdukt_zamowienia_id(new Produkt_Zamowienia_Id(this.id, this.id2));
 	 	 	                session.delete(pr);
 	 	 	             oc.closeDBSession();
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	 	                } 	            		 	            			                	
 	            	 } 	            	  
 	              }catch(Exception e) {	  
 	            	 JOptionPane.showMessageDialog(null, "Nie udało się usunąć elementu. Błąd: " + e.getMessage());
 	              }           			                	
                 }
                 else if(this.label.equals("Dodaj do koszyka")) {
                	 //Przycisk Dodaj do koszyka
                	 	
                	 try {
                    	 for(Obiekt_Do_Polecen pk: HibernateOracle.koszyk) {
                    			if(((Produkt_Koszyk)pk).getPr().getId_produktu() == id) {
                    				throw new Exception("Podany produkt juz zostal dodany."
                    						+ " Jesli chcesz dodac wieksza ilosc tego produkut zmodyfikuj jego wartosc w zamowieniu");
                    			}
                    		}
                	 

	                	 JPanel myPanel = new JPanel();
	                	 JLabel labelek = new JLabel("Podaj ilosc");
	                	 JTextField pierwszyField = new JTextField(7);
	                	 
	                	 myPanel.add(labelek);
	                	 myPanel.add(pierwszyField);
	                	
	                	int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Edytuj typ użytkownika", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     		                	if(!pierwszyField.getText().isEmpty()) {
	     		                		
	     		                	if(Integer.parseInt(pierwszyField.getText())<=0)
				 	                		throw(new Exception("Ilość nie może być ujemna lub równa zeru."));
	   	     	                  	 OracleConnection oc = OracleConnection.getInstance();
		     	                	 oc.createDBSession();
		     	                	 Session session = oc.getDBSession();
		     	                	 
		     		                	Produkty user = (Produkty)session.createQuery("select u from Produkty u where u.id_produktu = :id")
		     		 	                			.setParameter("id", this.id)
		     		 	                			.uniqueResult();
		     		                			
		     		                	Query<Integer> query = session.createQuery("select pd.stan_magazynowy from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP", Integer.class)
	     		 	                			.setParameter("idP", this.id);
		     		                	List<Integer> stanMagazynow = query.getResultList();
		     		                	
		     		                	int stanMag = 0;
		     		                	
		     		                	for(Integer stan: stanMagazynow) {
		     		                		if(stanMag + stan > (Integer.parseInt(pierwszyField.getText())))
		     		                		{
		     		                			stanMag = (Integer.parseInt(pierwszyField.getText()));
		     		                		}else
		     		                		{
		     		                			stanMag += stan;
		     		                		}
		     		                	}
		     		                	
		     		                	if((Integer.parseInt(pierwszyField.getText())) > stanMag)
		     		                		throw new Exception("Niestety nie posiadamy takiej ilości produktu w magazynie."
		     		                				+ "Obecny stan: " +Integer.toString(stanMag));
		     		                	
		     		                	Produkt_Koszyk pk = new Produkt_Koszyk(user,Integer.parseInt(pierwszyField.getText()));
		     		                	HibernateOracle.koszyk.add(pk);
	     		                	}
	     	                	}
	                		 }
	                		 catch(Exception e) {
	                			 e.printStackTrace();
	                			 JOptionPane.showMessageDialog(null, "Nie udało się dodac produktu do zamowienia. Błąd: " + e.getMessage());
	                		 }
                	 }catch(Exception e){
                		 JOptionPane.showMessageDialog(null, "Nie udało się dodac produktu do zamowienia. Błąd: " + e.getMessage());
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
}
