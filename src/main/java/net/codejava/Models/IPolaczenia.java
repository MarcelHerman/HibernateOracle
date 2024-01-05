package net.codejava.Models;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import net.codejava.HibernateOracle;

public interface IPolaczenia
{
	public void createDBSession();
	
	public Session getDBSession();
	
	public void closeDBSession();
}