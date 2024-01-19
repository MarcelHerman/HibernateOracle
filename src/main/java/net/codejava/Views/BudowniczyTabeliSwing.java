package net.codejava.Views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import org.hibernate.query.Query;

import net.codejava.HibernateOracle;
import net.codejava.Models.Obiekt_Do_Polecen;
import net.codejava.Models.PolaczenieOracle;
import net.codejava.Models.Produkt_Koszyk;
import net.codejava.Models.Produkt_Magazyn;
import net.codejava.Models.Produkt_Zamowienia;
import net.codejava.Models.Produkty;
import net.codejava.Models.Stany_Zamowienia;
import net.codejava.Models.Typy_uzytkownika;
import net.codejava.Models.Zamowienia;

public class BudowniczyTabeliSwing implements BudowniczyTabeli
{	
	private LinkedList<String> naglowek;
	private LinkedList<LinkedList<Object>> dane = new LinkedList<LinkedList<Object>>();
	private LinkedList<Object> wiersz;
	
	public void dodajKolumnePrzycisku() {
			this.dodajKolumne("");
	}

	public void zresetuj() {
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
		if(HibernateOracle.obiekt instanceof Produkty && !(HibernateOracle.nazwaTypu.equals("null"))){
			TableColumn buttonColumn = jt.getColumnModel().getColumn(naglowek.size() - 1);
        	buttonColumn.setCellRenderer(new ButtonRenderer());
        	buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
        	if (HibernateOracle.nazwaTypu != null && HibernateOracle.nazwaTypu.equals("Pracownik")) {
    			if(!(HibernateOracle.obiekt instanceof Typy_uzytkownika) && !(HibernateOracle.obiekt instanceof Stany_Zamowienia))
                {              	
                	TableColumn buttonColumn3 = jt.getColumnModel().getColumn(naglowek.size() - 2);
                	buttonColumn3.setCellRenderer(new ButtonRenderer());
                	buttonColumn3.setCellEditor(new ButtonEditor(new JCheckBox()));
                }
    			
            }else if(HibernateOracle.nazwaTypu != null && HibernateOracle.nazwaTypu.equals("Administrator"))
            {
            	if(!(HibernateOracle.obiekt instanceof Typy_uzytkownika) && !(HibernateOracle.obiekt instanceof Stany_Zamowienia))
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
				if(!(HibernateOracle.obiekt instanceof Typy_uzytkownika) && !(HibernateOracle.obiekt instanceof Stany_Zamowienia))
	            {
					if(!(HibernateOracle.obiekt instanceof Produkt_Zamowienia) && !(HibernateOracle.obiekt instanceof Zamowienia))
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
				if(HibernateOracle.obiekt instanceof Zamowienia) {
	            	TableColumn buttonColumn2 = jt.getColumnModel().getColumn(naglowek.size() - 1);
	            	buttonColumn2.setCellRenderer(new ButtonRenderer());
	            	buttonColumn2.setCellEditor(new ButtonEditor(new JCheckBox()));
				}
				if(HibernateOracle.obiekt instanceof Produkt_Magazyn) {
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
			if(!(HibernateOracle.obiekt instanceof Typy_uzytkownika) && !(HibernateOracle.obiekt instanceof Stany_Zamowienia))
			{
				this.naglowek.addLast(" ");
				if(!(HibernateOracle.obiekt instanceof Zamowienia) &&  !(HibernateOracle.obiekt instanceof Produkt_Zamowienia))this.naglowek.addLast(" ");				
			}
		}
		
		if(HibernateOracle.obiekt instanceof Produkty && !(HibernateOracle.nazwaTypu.equals("null")))
		{
			this.naglowek.addLast(" ");
			if(HibernateOracle.nazwaTypu.equals("Pracownik"))this.naglowek.addLast(" ");
		}
			
		if(!(HibernateOracle.nazwaTypu.equals("null")))
		{
			if(HibernateOracle.nazwaTypu.equals("Pracownik"))
			{
				if(HibernateOracle.obiekt instanceof Zamowienia)this.naglowek.addLast(" ");
				else if(HibernateOracle.obiekt instanceof Produkt_Magazyn) {
					this.naglowek.addLast(" ");
				}
			}
		}		
		
		Object[] naglowek = this.naglowek.toArray();
		Object[][] dane = new Object[this.dane.size()][];
		int i = 0;
		for(LinkedList<Object> w:this.dane) dane[i++]=w.toArray();
		
		DefaultTableModel model = new DefaultTableModel(dane,naglowek);
		
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
            if(HibernateOracle.obiekt instanceof Produkty)
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
            else if(HibernateOracle.obiekt instanceof Produkt_Magazyn) {
            	
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
            else if(HibernateOracle.obiekt instanceof Zamowienia)
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

		public ButtonEditor(JCheckBox poleWyboru) {
            super(poleWyboru);
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
            if(HibernateOracle.obiekt instanceof Produkty)
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
            	else if(HibernateOracle.obiekt instanceof Produkt_Magazyn) {
            	
            	if (column == (naglowek.size() - 1)) {
            		if(HibernateOracle.nazwaTypu.equals("Pracownik"))label = "Edytuj";
            		if(HibernateOracle.nazwaTypu.equals("Administrator"))label = "Usuń";
            	}
            	if(column == (naglowek.size() -2)) {
            		if(HibernateOracle.nazwaTypu.equals("Administrator"))label = "Edytuj";
            	}
            }
            else if(HibernateOracle.obiekt instanceof Zamowienia)
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
            if(HibernateOracle.obiekt instanceof Produkt_Magazyn || HibernateOracle.obiekt instanceof Produkt_Zamowienia) this.id2=Integer.parseInt((String)table.getValueAt(row, 1));
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
                	  JPanel myPanel = new JPanel();
                      myPanel.add(new JLabel("Czy na pewno chcesz usunąć dany rekord?"));

                     int result = JOptionPane.showConfirmDialog(null, myPanel, 
                              "Usuwanie", JOptionPane.OK_CANCEL_OPTION);
                     if (result == JOptionPane.OK_OPTION)HibernateOracle.wzorzec.dodajLogikeUsuwania(this);
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
	                	 JLabel labelek = new JLabel("Podaj ilość");
	                	 JTextField pierwszyField = new JTextField(7);
	                	 
	                	 myPanel.add(labelek);
	                	 myPanel.add(pierwszyField);
	                	
	                	int result = JOptionPane.showConfirmDialog(null, myPanel, 
	   	                         "Dodawanie do koszyka", JOptionPane.OK_CANCEL_OPTION);
	                		 try {
	     	                	if (result == JOptionPane.OK_OPTION) {
	     	                		
	     		                	if(!pierwszyField.getText().isEmpty()) {
	     		                		
	     		                	if(Integer.parseInt(pierwszyField.getText())<=0)
				 	                		throw(new Exception("Ilość nie może być ujemna lub równa zeru."));
	   	     	                  	 PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
		     	                	 bd.stworzSesjeBD();
		     	                	 Session sesja = bd.pobierzSesjeBD();
		     	                	 
		     		                	Produkty rekord = (Produkty)sesja.createQuery("select u from Produkty u where u.id_produktu = :id")
		     		 	                			.setParameter("id", this.id)
		     		 	                			.uniqueResult();
		     		                			
		     		                	Query<Integer> zapytanie = sesja.createQuery("select pd.stan_magazynowy from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP", Integer.class)
	     		 	                			.setParameter("idP", this.id);
		     		                	List<Integer> stanMagazynow = zapytanie.getResultList();
		     		                	
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
		     		                	
		     		                	Produkt_Koszyk pk = new Produkt_Koszyk(rekord,Integer.parseInt(pierwszyField.getText()));
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
