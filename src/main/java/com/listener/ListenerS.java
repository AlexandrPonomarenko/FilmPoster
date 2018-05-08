package com.listener;

import com.al.pon.util.UtilCreateAndDeleteCollectionInDB;
import com.mongo.util.MongoUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener()
public class ListenerS implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {
    UtilCreateAndDeleteCollectionInDB utilCreateAndDeleteCollectionInDB;

    // Public constructor is required by servlet spec
    public ListenerS() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("contextInitialized...");
        utilCreateAndDeleteCollectionInDB = new UtilCreateAndDeleteCollectionInDB();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("contextDestroyed...");
        utilCreateAndDeleteCollectionInDB.dropCollections();
        MongoUtil.getMongoClientConnection().close();
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attibute
         is replaced in a session.
      */
    }
}
