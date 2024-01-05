package net.codejava.Models;

import org.hibernate.Session;

public interface IPolaczenia
{
	public void createDBSession();
	
	public Session getDBSession();
	
	public void closeDBSession();
}