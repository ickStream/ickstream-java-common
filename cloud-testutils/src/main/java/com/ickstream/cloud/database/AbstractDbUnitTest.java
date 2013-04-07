/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.database;

import com.google.inject.Inject;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.SQLException;

/**
 * Abstract base class for all test cases which needs to load/access database.
 * The responsibility of this class is to abstract the database setup/tear down from all the other test cases
 */
public abstract class AbstractDbUnitTest extends AbstractDatabaseTest {

    protected AbstractDbUnitTest(String databaseUrl) {
        super(databaseUrl);
    }

    @Inject
    private EntityManagerFactory emFactory;

    @Inject
    private EntityManager em;

    /**
     * Clear entity manager to ensure one test method doesn't affect the result of a following test method
     */
    @BeforeMethod
    protected void clearEntityManager() {
        if (em != null && em.isOpen()) {
            em.clear();
        }
    }

    /**
     * Makes sure any open transaction is rolled back and completed after each test method
     *
     * @param m The test case method
     */
    @AfterMethod
    protected void rollbackTransaction(Method m) {
        if (em != null && em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    /**
     * Shutdown database provider and entity manager after the test case hase been executed.
     */
    @AfterTest
    protected void tearDownProvider() {
        // We don't want to fail the test case if the entity manager is already closed
        if (em != null && em.isOpen()) {
            em.close();
        }

        // We don't want to fail if the entity manager factory is already closed
        if (emFactory != null && emFactory.isOpen()) {
            emFactory.close();
        }
    }

    /**
     * Get the parent directory of the path specified, this will return /src if you specify /src/test as put
     *
     * @param path The path to get parent directory for
     * @return The parent directory
     */
    protected String getParentDirectory(String path) {
        String parentDir = "/";
        int lastIndex;

        // Only decend into paths that actually contains something
        if (path != null && path.trim().length() > 0) {
            path = path.trim();

            // Ignore any / character at the end
            if (path.endsWith("/") && path.length() > 1) {
                path = path.substring(0, path.length() - 1);
            }

            if (path.length() > 1) {
                lastIndex = path.lastIndexOf("/");

                if (lastIndex > 0) {
                    // Get path up until the last /
                    parentDir = path.substring(0, lastIndex);
                }
            }
        }

        return parentDir;
    }

    /**
     * Get the directory where the DbUnit test data files can be found
     *
     * @return The DbUnit test data file directory
     */
    protected String getTestDataDirectory() {
        // We need to get the directory from the classpath to make sure the test case works the same independent of the current directory
        // when running the test case. This is required to make the test case easy to run both from Eclipse, IntelliJ IDEA and maven.
        String path = getClass().getResource("/META-INF/persistence.xml").getPath();
        if (path != null) {
            path = getParentDirectory(path);
            if (path != null) {
                path = getParentDirectory(path);
            }
            if (path != null) {
                path = getParentDirectory(path);
            }
            if (path != null) {
                path = getParentDirectory(path);
            }
        }
        if (path != null) {
            return path + "/" + "src/test/test-data/";
        } else {
            return "src/test/test-data/";
        }
    }

    /**
     * Remove the specified DbUnit test data tables into the currently used database provider, this function will just remove any existing test data
     * from the tables specified in the test data file
     */
    protected void cleanTestData(String pkg, String file) {
        loadTestData(DatabaseOperation.DELETE_ALL, getTestDataDirectory() + pkg.replaceAll("\\.", "/") + "/" + file);
    }

    /**
     * Load the specified DbUnit test data file into the currently used database provider, this function will first remove and existing test data
     * from the tables specified in the test data file and then load the new data.
     *
     * @param pkg  The package which the test data files are stored in
     * @param file The file name of the test data file to use
     */
    protected void loadTestData(String pkg, String file) {
        loadTestData(DatabaseOperation.CLEAN_INSERT, getTestDataDirectory() + pkg.replaceAll("\\.", "/") + "/" + file);
    }

    /**
     * Load the specified DbUnit test data file into the currently used database provider, in comparison to {@link #loadTestData(String, String)}
     * this function will not remove any existing test data, it will just load the new data in the test data file on top of any existing data
     *
     * @param pkg  The package which the test data files are stored in
     * @param file The file name of the test data file to use
     */
    protected void addTestData(String pkg, String file) {
        loadTestData(DatabaseOperation.INSERT, getTestDataDirectory() + pkg.replaceAll("\\.", "/") + "/" + file);
    }

    /**
     * Load the specified DbUnit test data file into the currently used database provider using the specified {@link DatabaseOperation} operation
     * type. It's prefered that you use {@link #addTestData(String, String)} or {@link #loadTestData(String, String)} instead, this method is just a
     * backup if you need to do something special.
     *
     * @param dboperation The {@link DatabaseOperation} operation type which should be used when loading the test data
     * @param file        The full path to the test data file, it's recommended to use {@link #getTestDataDirectory()} to calculate this
     */
    protected void loadTestData(DatabaseOperation dboperation, String file) {
        try {
            IDatabaseConnection connection = new DatabaseConnection(getDatabaseConnection());
            DatabaseConfig config = connection.getConfig();
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());

            FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            builder.setColumnSensing(true);
            IDataSet ds = builder.build(new File(file));

            // Load the data set through a sequence filter to ensure statements are executed in correct order
            ITableFilter filter = new DatabaseSequenceFilter(connection);
            IDataSet dataset = new FilteredDataSet(filter, ds);

            dboperation.execute(connection, dataset);
        } catch (DatabaseUnitException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    protected EntityManager getEntityManager() {
        return em;
    }
}
