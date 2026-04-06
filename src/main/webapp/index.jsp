<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.klef.fsad.exam.CustomerAccount" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="org.hibernate.SessionFactory" %>
<%@ page import="org.hibernate.Transaction" %>
<%@ page import="org.hibernate.cfg.Configuration" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html>
<head>
    <title>Customer Account Operations</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f9; color: #333; margin: 40px; }
        .container { background-color: white; padding: 20px 40px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); max-width: 600px; margin: auto; }
        h2 { color: #0056b3; }
        pre { background-color: #2c3e50; color: #ecf0f1; padding: 15px; border-radius: 5px; font-size: 14px; overflow-x: auto; }
        .success { color: #27ae60; font-weight: bold; }
        .error { color: #c0392b; font-weight: bold; }
    </style>
</head>
<body>

<div class="container">
    <h2>Hibernate Operations Output</h2>
    <%
        SessionFactory sessionFactory = null;
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
            
            // --- Part I: Insert ---
            out.println("<h3>I. Inserting Record</h3>");
            Session session1 = sessionFactory.openSession();
            Transaction tx1 = session1.beginTransaction();
            
            CustomerAccount account = new CustomerAccount();
            account.setName("John Doe Web");
            account.setDescription("Premium Web Account");
            account.setDate(new Date());
            account.setStatus("Pending");
            
            session1.save(account);
            tx1.commit();
            
            Long generatedId = account.getId();
            
            out.println("<p class='success'>Record inserted successfully with ID: " + generatedId + "</p>");
            out.println("<pre>" + account.toString() + "</pre>");
            session1.close();
            
            // --- Part II: Update ---
            out.println("<h3>II. Updating Record</h3>");
            Session session2 = sessionFactory.openSession();
            Transaction tx2 = session2.beginTransaction();
            
            CustomerAccount accountToUpdate = session2.get(CustomerAccount.class, generatedId);
            
            if(accountToUpdate != null) {
                accountToUpdate.setName("John Updated Web");
                accountToUpdate.setStatus("Active");
                
                session2.update(accountToUpdate);
                tx2.commit();
                
                out.println("<p class='success'>Record with ID " + generatedId + " updated successfully.</p>");
                out.println("<pre>New Name: " + accountToUpdate.getName() + "\nNew Status: " + accountToUpdate.getStatus() + "</pre>");
            } else {
                out.println("<p class='error'>Record not found for update!</p>");
            }
            session2.close();
        } catch(Exception e) {
            out.println("<p class='error'>Error occurred: " + e.getMessage() + "</p>");
            e.printStackTrace();
        } finally {
            if(sessionFactory != null) {
                sessionFactory.close();
            }
        }
    %>
</div>

</body>
</html>
