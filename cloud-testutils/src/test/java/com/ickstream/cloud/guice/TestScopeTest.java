/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Implements a Guice test scope, the typical usage together with TestNG is as follows, which will
 * result in RequestScoped being mapped to a scope valid for a specific test method
 * <p/>
 * <pre>
 * private TestScope testMethodScope = new TestScope();
 * public class RequestScopeModule extends AbstractModule {
 *     &#064;Override
 *     protected void configure() {
 *         bindScope(RequestScoped.class, testMethodScope);
 *     }
 * }
 * &#064;BeforeMethod
 * private void setTestScope(Method m) {
 *     testMethodScope.setContext(m.getName());
 * }
 * </pre>
 */
public class TestScopeTest {
    Injector injector;
    TestScope testScope = new TestScope();

    class TestObject {
        int id;

        public TestObject(int id) {
            this.id = id;
        }
    }

    @RequestScoped
    static class TestObject2 {
    }

    class RequestScopeModule extends AbstractModule {
        @Override
        protected void configure() {
            bindScope(RequestScoped.class, testScope);
        }

        int createdInstances = 0;

        @Provides
        @RequestScoped
        public TestObject createTestObject() {
            return new TestObject(++createdInstances);
        }
    }


    @BeforeMethod
    void setupTestScope(Method m) {
        testScope.setContext(m.getName());
    }

    @BeforeClass
    public void setUp() {
        injector = com.google.inject.Guice.createInjector(new RequestScopeModule());
    }

    @Test
    public void testWithinSameMethodFromProvider() {
        TestObject obj1 = injector.getInstance(TestObject.class);
        TestObject obj2 = injector.getInstance(TestObject.class);
        Assert.assertTrue(obj1 == obj2);
    }

    @Test
    public void testWithinSameMethodFromAnnotation() {
        TestObject2 obj1 = injector.getInstance(TestObject2.class);
        TestObject2 obj2 = injector.getInstance(TestObject2.class);
        Assert.assertTrue(obj1 == obj2);
    }

    @Test
    public void testWithinSubMethods() {
        Assert.assertTrue(getFirstTestObject() == getSecondTestObject());
    }

    private TestObject getFirstTestObject() {
        return injector.getInstance(TestObject.class);
    }

    private TestObject getSecondTestObject() {
        return injector.getInstance(TestObject.class);
    }

    @Test(dependsOnMethods = {"testSimple"})
    public void testDependentMethod() {
        TestObject object = injector.getInstance(TestObject.class);
        Assert.assertNotNull(currentObj);
        Assert.assertTrue(object != currentObj);
    }

    TestObject currentObj;

    @Test
    void testSimple() {
        currentObj = injector.getInstance(TestObject.class);
        Assert.assertNotNull(currentObj);
    }

    @Test
    public void testDifferentObjects() {
        TestObject obj1 = injector.getInstance(TestObject.class);
        TestObject2 obj2 = injector.getInstance(TestObject2.class);
        Assert.assertNotNull(obj1);
        Assert.assertNotNull(obj2);
    }
}
