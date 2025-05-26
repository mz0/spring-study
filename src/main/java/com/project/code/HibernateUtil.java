package com.project.code;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component // Make this class a Spring-managed bean
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class); // For logging

    @Autowired
    public HibernateUtil(EntityManagerFactory entityManagerFactory) {
        logger.info("HibernateUtil constructor called with EntityManagerFactory.");
        if (entityManagerFactory == null) {
            logger.error("EntityManagerFactory injected is null.");
            throw new NullPointerException("EntityManagerFactory injected is null");
        }

        SessionFactory sf = entityManagerFactory.unwrap(SessionFactory.class);
        if (sf == null) {
            logger.error("Failed to unwrap SessionFactory from EntityManagerFactory. It might not be a Hibernate EntityManagerFactory.");
            // Consider throwing a more specific exception or IllegalStateException
            throw new NullPointerException("Not a Hibernate EntityManagerFactory or unwrapping failed.");
        }
        HibernateUtil.sessionFactory = sf; // Assign to the static field
        logger.info("SessionFactory initialized successfully.");
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            // This indicates a problem: either HibernateUtil was not initialized by Spring,
            // or getSessionFactory() is called before Spring context is ready.
            logger.warn("getSessionFactory() called but sessionFactory is null. " +
                        "Ensure HibernateUtil is a Spring bean and properly initialized.");
            // Depending on your application's needs, you might throw an IllegalStateException here
            // throw new IllegalStateException("SessionFactory not initialized. Check Spring configuration and bean lifecycle.");
        }
        return sessionFactory;
    }
}
