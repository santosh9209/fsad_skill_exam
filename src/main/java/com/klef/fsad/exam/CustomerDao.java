package com.klef.fsad.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class CustomerDao {
    private SessionFactory sessionFactory;

    public CustomerDao() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
    }

    public List<CustomerAccount> getAllCustomers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from CustomerAccount", CustomerAccount.class).list();
        }
    }

    public CustomerAccount getCustomerById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(CustomerAccount.class, id);
        }
    }

    public CustomerAccount addCustomer(CustomerAccount customer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(customer);
            transaction.commit();
            return customer;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public CustomerAccount updateCustomer(Long id, CustomerAccount updatedCustomer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CustomerAccount existingCustomer = session.get(CustomerAccount.class, id);
            if (existingCustomer != null) {
                if (updatedCustomer.getName() != null) existingCustomer.setName(updatedCustomer.getName());
                if (updatedCustomer.getDescription() != null) existingCustomer.setDescription(updatedCustomer.getDescription());
                if (updatedCustomer.getDate() != null) existingCustomer.setDate(updatedCustomer.getDate());
                if (updatedCustomer.getStatus() != null) existingCustomer.setStatus(updatedCustomer.getStatus());
                
                session.update(existingCustomer);
                transaction.commit();
                return existingCustomer;
            }
            return null;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteCustomer(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            CustomerAccount customer = session.get(CustomerAccount.class, id);
            if (customer != null) {
                session.delete(customer);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
