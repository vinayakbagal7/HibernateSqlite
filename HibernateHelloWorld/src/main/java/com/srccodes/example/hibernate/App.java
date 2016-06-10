package com.srccodes.example.hibernate;

import java.util.List;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Hello world!
 * 
 */
public class App {
	private static SessionFactory sessionFactory = null;  
	private static ServiceRegistry serviceRegistry = null;  
	  
	private static SessionFactory configureSessionFactory() throws HibernateException {  
	    Configuration configuration = new Configuration();  
	    configuration.configure();  
	    
	    Properties properties = configuration.getProperties();
	    
		serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();          
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);  
	    
	    return sessionFactory;  
	}
	
	public static void main(String[] args) {
		// Configure the session factory
		configureSessionFactory();
		
		Session session = null;
		Transaction tx=null;
		
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			
			// Creating Contact entity that will be save to the sqlite database
			Contact myContact = new Contact(1, "Vinayak", "vinayak@sqs.com");
			Contact yourContact = new Contact(2, "Likhit", "likhit@email.com");
			
			// Saving to the database
			session.update(myContact);
			session.save(yourContact);
			
			// Committing the change in the database.
			session.flush();
			tx.commit();
			
			// Fetching saved data
			List<Contact> contactList = session.createQuery("from Contact").list();
			
			for (Contact contact : contactList) {
				System.out.println("Id: " + contact.getId() + " | Name:"  + contact.getName() + " | Email:" + contact.getEmail());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			
			// Rolling back the changes to make the data consistent in case of any failure 
			// in between multiple database write operations.
			tx.rollback();
		} finally{
			if(session != null) {
				session.close();
			}
		}
	}
}
