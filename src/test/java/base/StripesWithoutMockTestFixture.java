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

import com.github.mcs.sst.SpringTestInterceptor;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.Filter;
import javax.servlet.ServletContextEvent;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.mock.MockServletContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.web.context.ContextLoaderListener;

/**
 * Extend this class to create a unit test that will run tests against some {@link ActionBean Stripes Action Beans}.
 * <p> This fixture provides a full featured {@link MockServletContext}. Already
 * configured are the
 * {@link SpringTestInterceptor}, the package where your action beans are
 * located, a stripes extension package and the default {@link ResourceBundle}
 * pointing at
 * <code>StripesResources.properties</code>.
 */
public abstract class StripesWithoutMockTestFixture extends SpringServiceTestFixture {

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
        filterParams.put("Extension.Packages", "myproj.stripesext");
        filterParams.put("Interceptor.Classes", "net.sourceforge.stripes.integration.spring.SpringInterceptor");
        CTX.addFilter(StripesFilter.class, "StripesFilter", filterParams);
    }

    private static void initServlet() {
        CTX.addInitParameter("contextConfigLocation", SPRING_APPLICATION_CONTEXT_XML);

        ContextLoaderListener springContextListener = new ContextLoaderListener();
        springContextListener.contextInitialized(new ServletContextEvent(CTX));

        CTX.setServlet(DispatcherServlet.class, "StripesDispatcher", null);
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