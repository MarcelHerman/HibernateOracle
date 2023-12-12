package net.codejava;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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
            TableColumn buttonColumn = jt.getColumnModel().getColumn(naglowek.size() - 2);
            buttonColumn.setCellRenderer(new ButtonRenderer());
            buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

            TableColumn buttonColumn2 = jt.getColumnModel().getColumn(naglowek.size() - 1);
            buttonColumn2.setCellRenderer(new ButtonRenderer());
            buttonColumn2.setCellEditor(new ButtonEditor(new JCheckBox()));
        }
		
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

    private static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;

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
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                // Do something when button is pressed
                // You probably want to open a dialog here
                JOptionPane.showMessageDialog(button, label + " Clicked");
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
	

	void tworzTabeleMagazyn(List<Obiekt_Do_Polecen> entities)
	{
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
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Data wystawienia");
		this.dodajKolumne("NIP");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Faktury) entry).getId_faktury()));
			this.dodajKolumne(((Faktury) entry).getData_wystawienia().toString());
			this.dodajKolumne(((Faktury) entry).getNIP().toString());
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
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Nazwa użytkownika");
		this.dodajKolumne("Login");
		this.dodajKolumne("Hasło");
		this.dodajKolumne("E-mail");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Uzytkownicy) entry).getId_uzytkownika()));
			this.dodajKolumne(((Uzytkownicy) entry).getNazwa_uzytkownika().toString());
			this.dodajKolumne(((Uzytkownicy) entry).getLogin().toString());
			this.dodajKolumne(((Uzytkownicy) entry).getHaslo().toString());
			this.dodajKolumne(((Uzytkownicy) entry).getE_mail().toString());
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
