package net.codejava.Models;

import org.hibernate.Session;

public interface IPolaczenia
{
	public void stworzSesjeBD();
	
	public Session pobierzSesjeBD();
	
	public void zamknijSesjeBD();
}