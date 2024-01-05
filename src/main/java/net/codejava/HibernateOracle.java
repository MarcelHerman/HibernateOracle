package net.codejava;

import net.codejava.Controllers.DyrektorTabel;
import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliDruk;
import net.codejava.Views.BudowniczyTabeliSwing;
import net.codejava.Views.IStrategia;
import net.codejava.Views.widokAplikacji;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.service.spi.ServiceException;

import java.util.Map;
import java.util.HashMap;

public class HibernateOracle extends JFrame {
	
	public static String nazwaTypu = "null";
	public static Obiekt_Do_Polecen obj = null;
	public static List<Obiekt_Do_Polecen> koszyk = new ArrayList<Obiekt_Do_Polecen>();
	public static Map<String, List<Obiekt_Do_Polecen>> cache;
	
	public static int idUzytkownika;
	
	public static Repozytorium_Polecen repoPolecen = new Repozytorium_Polecen();
	
	public static IStrategia wzorzec;

	public static void main(String[] args) {
		widokAplikacji aplikacja = new widokAplikacji();
		aplikacja.Inicjalizuj();
	}
}
