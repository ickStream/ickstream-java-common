/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.database;

import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AbstractDatabaseTest {
    private String databaseUrl;

    protected AbstractDatabaseTest(String databaseUrl) {
        this.databaseUrl = "jdbc:h2:mem:" + databaseUrl;
    }

    public Connection getDatabaseConnection() {
        try {
            Class.forName("org.h2.Driver");
            String username = "";
            String password = "";
            return DriverManager.getConnection(databaseUrl, username, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Logs the name of the test method
     *
     * @param m The test case method
     */
    @BeforeMethod
    public void logTestMethod(Method m) {
        System.out.println("Executing " + getClass().getSimpleName() + "." + m.getName() + "...");
    }
}