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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

    public class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;
        
        public int id;
        
        public int id2;
        
        public JTable tab;
        
        public int row;

        public JTable getTab() {
			return tab;
		}

		public void setTab(JTable tab) {
			this.tab = tab;
		}

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
              
            this.id = Integer.parseInt((table.getValueAt(row, 0)).toString());
            if(HibernateOracle.obj instanceof Produkt_Magazyn || HibernateOracle.obj instanceof Produkt_Zamowienia) this.id2=Integer.parseInt((String)table.getValueAt(row, 1));
            button.setText(label);
            isPushed = true;
            tab = table;
            this.row = row;
            
            
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
            	 if (this.label.equals("Edytuj"))  HibernateOracle.wzorzec.dodajLogikeEdytowania(this);  // Kod dla przycisku "Edytuj"
                	                      
                  else if ("Usuń".equals(this.label)) { 
                     // Kod dla przycisku "Usuń"
                	                       	 
                	JPanel myPanel = new JPanel();
 	                myPanel.add(new JLabel("Czy na pewno chcesz usunąć dany rekord?"));
 	                
 	               int result = JOptionPane.showConfirmDialog(null, myPanel, 
	                         "Usuwanie", JOptionPane.OK_CANCEL_OPTION);
                			
 	              try {
 	            	 if (result == JOptionPane.OK_OPTION) {
 	            		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
 	                	//oc.createDBSession();	                			
 	                	//Session session = oc.getDBSession();
 	            		
 	                	if(HibernateOracle.obj instanceof Faktury)
 	                	{
 	                		Faktury pr = new Faktury();
 	 	                	pr.setId_faktury(this.id);
 	 	                	//session.delete(pr);
 	 	                	HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
 	 	                	//oc.closeDBSession();
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	                	}
 	                	
 	                	else if(HibernateOracle.obj instanceof Kategorie)
 	                	{
 	                		Kategorie pr = new Kategorie();
 	 	                	pr.setId_Kategorii(this.id);
 	 	                	//session.delete(pr);
 	 	                	//oc.closeDBSession();
 	 	                	List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Kategorie");
 	 	                	lista.remove(this.row);
 	 	                	HibernateOracle.cache.put("Kategorie", lista);
 	 	                	HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	                	}
 	                	
 	                	else if(HibernateOracle.obj instanceof Uzytkownicy)
 	 	                {
 	 	                	
	 	 	            //oc.closeDBSession();
 	 	 	                
 	 	                } 	 
 	                	else if(HibernateOracle.obj instanceof Produkty)
 	 	                {	
 	                		
 	 	 	             //oc.closeDBSession();
 	 	                } 	 
 	                	else if(HibernateOracle.obj instanceof Zamowienia)
 	 	                {
 	 	                	
 	 	                } 	 
 	                	else if(HibernateOracle.obj instanceof Magazyny)
 	 	                {
 	 	                	Magazyny pr = new Magazyny();
 	 	 	                pr.setId_magazynu(this.id);
 	 	 	             List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Magazyny");
	 	                	lista.remove(this.row);
	 	                	HibernateOracle.cache.put("Magazyny", lista);
 	 	 	                //session.delete(pr);
 	 	                	HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));

 	 	 	             //oc.closeDBSession();
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	 	                } 
 	                	else if(HibernateOracle.obj instanceof Producenci)
 	 	                {
 	                		oc.createDBSession();	                			
 	 	                	Session session = oc.getDBSession();
 	                		Producenci pr = (Producenci)session.createQuery("select u from Producenci u where u.id_producenta = :id")
 	 	                			.setParameter("id", this.id)
 	 	                			.uniqueResult();
 	                		oc.closeDBSession();
 	                		
 	                		List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Producenci");

 	                		for (int i = 0; i < lista.size(); i++) {
 	                		    Obiekt_Do_Polecen element = lista.get(i);
 	                		    Producenci pom = (Producenci) element;
 	                		    //System.out.println("przed modyfikacją:" + pom.getCzy_usunieto() + "\n");

 	                		    if (pom.getId_producenta() == pr.getId_producenta()) {
 	                		        pom.setCzy_usunieto(1);
 	                		        //System.out.println("po modyfikacji:" + pom.getCzy_usunieto() + "\n");
 	                		        break;
 	                		    }
 	                		}

 	                		/*for (Obiekt_Do_Polecen element : lista) {
 	                		    Producenci pom = (Producenci) element;
 	                		    System.out.println("po wyjściu z pętli:" + pom.getCzy_usunieto() + "\n");
 	                		}*/

 	                		HibernateOracle.cache.put("Producenci", lista);
 	 	 	             pr.setCzy_usunieto(1);
	 	 	                //session.update(pr);
		 	                HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(pr, HibernateOracle.idUzytkownika));

	 	 	              tab.setValueAt("TAK", row, 5);
	 	 	            //oc.closeDBSession();
 	 	                } 
 	                	else if(HibernateOracle.obj instanceof Produkt_Koszyk)
 	 	                {
 	                		for(Obiekt_Do_Polecen pk: HibernateOracle.koszyk) {
	 	                			if(((Produkt_Koszyk)pk).getPr().getId_produktu() == id) {
	 	                				HibernateOracle.koszyk.remove(pk);
	 	                				break;
	 	                			}
	 	                		}
 	                		//oc.closeDBSession();
 	 	                	((DefaultTableModel)tab.getModel()).removeRow(row);
 	 	                }
 	                	else if(HibernateOracle.obj instanceof Produkt_Magazyn)
 	 	                {
 	                		
 	 	                } 
 	                	else if(HibernateOracle.obj instanceof Produkt_Zamowienia)
 	 	                {
 	                		Produkt_Zamowienia pr = new Produkt_Zamowienia();
 	 	 	                pr.setProdukt_zamowienia_id(new Produkt_Zamowienia_Id(this.id, this.id2));
 	 	 	                //session.delete(pr);
 	 	                	HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
 	 	 	            // oc.closeDBSession();
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
	   	     	                  	 PolaczenieOracle oc = PolaczenieOracle.getInstance();
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
