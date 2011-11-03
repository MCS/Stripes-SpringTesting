/*
 * This file is part of Stripes-Spring-Testing.
 * 
 * Stripes-Spring-Testing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Stripes-Spring-Testing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Stripes-Spring-Testing. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2011 Marcus Krassmann, Email: marcus.krassmann@gmail.com
 */
package base;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.Filter;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import com.github.mcs.sst.SpringTestInterceptor;
import net.sourceforge.stripes.mock.MockServletContext;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.WebApplicationContext;

/**
 * Extend this class to create a unit test that will run tests against some {@link ActionBean Stripes Action Beans}.
 * <p>
 * This fixture provides a full featured {@link MockServletContext}. Already configured are the
 * {@link SpringTestInterceptor}, the package where your action beans are located, a stripes extension package and the
 * default {@link ResourceBundle} pointing at <code>StripesResources.properties</code>.
 */
@ContextConfiguration(locations = StripesTestFixture.APPLICATION_CONTEXT_XML)
public abstract class StripesTestFixture extends SpringServiceTestFixture {

    public static final String APPLICATION_CONTEXT_XML = "classpath:test-context.xml";
    protected static final MockServletContext CTX = new MockServletContext("mock_ctx");

    /**
     * This method is invoked by JUnit. <b>Do not invoke it yourself!</b>
     */
    @BeforeClass
    public static void setupFixture() {
        initFilter();
        initServlet();
    }

    private static void initFilter() {
        Map<String, String> filterParams = new HashMap<String, String>();
        // required entry
        filterParams.put("ActionResolver.Packages", "myproj.action");
        // Here we add our own SpringTestInterceptor instead of the default one
        filterParams.put("Interceptor.Classes", "com.github.mcs.sst.SpringTestInterceptor");
        CTX.addFilter(StripesFilter.class, "StripesFilter", filterParams);
    }

    private static void initServlet() {
        CTX.addInitParameter("contextConfigLocation", APPLICATION_CONTEXT_XML);
        CTX.setServlet(DispatcherServlet.class, "StripesDispatcher", null);
    }

    @Before
    public void injectSpringContextInServletContext() {
        CTX.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
    }

    /**
     * This method is invoked by JUnit. <b>Do not invoke it yourself!</b>
     */
    @AfterClass
    public static void tearDown() {
        for (Filter filter : CTX.getFilters()) {
            filter.destroy();
        }
    }
}