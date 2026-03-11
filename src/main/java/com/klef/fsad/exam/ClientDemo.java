package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Date;

public class ClientDemo {

    public static void main(String[] args) {
        // Build SessionFactory
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        // I. Insert a new record into the database.
        insertRecord(sessionFactory);

        // II. Update fields such as Name or Status based on the ID.
        // Assuming ID 1 for demonstration; in a real app, this should be an existing ID.
        updateRecord(sessionFactory, 1L, "Updated Customer Name", "Active");

        sessionFactory.close();
    }

    private static void insertRecord(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            CustomerAccount account = new CustomerAccount();
            account.setName("John Doe");
            account.setDescription("Premium Savings Account");
            account.setDate(new Date());
            account.setStatus("Pending");

            session.save(account);

            transaction.commit();
            System.out.println("Record inserted successfully with ID: " + account.getId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void updateRecord(SessionFactory sessionFactory, Long id, String newName, String newStatus) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Retrieve the record
            CustomerAccount account = session.get(CustomerAccount.class, id);

            if (account != null) {
                // Update fields
                System.out.println("Found Record: " + account.getName() + " - " + account.getStatus());
                account.setName(newName);
                account.setStatus(newStatus);
                
                session.update(account);
                
                transaction.commit();
                System.out.println("Record with ID " + id + " updated successfully. New Name: " + newName + ", New Status: " + newStatus);
            } else {
                System.out.println("Record with ID " + id + " not found.");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
