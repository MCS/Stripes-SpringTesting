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
package com.github.mcs.sst;

import javax.servlet.ServletContext;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.integration.spring.SpringHelper;

import net.sourceforge.stripes.integration.spring.SpringHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class SpringTestHelper {

    private SpringTestHelper() {
        // no instance allowed
    }

    /**
     * Injects Spring managed beans into using a Web Application Context that is
     * derived from the ServletContext, which is in turn looked up using the
     * ActionBeanContext.
     *
     * @param bean    the object into which to inject spring managed bean
     * @param context the ActionBeanContext represented by the current request
     */
    public static void injectBeans(Object bean, ActionBeanContext context) {
        injectBeans(bean, StripesFilter.getConfiguration().getServletContext());
    }

    /**
     * Injects Spring managed beans using a Web Application Context derived from
     * the ServletContext.
     *
     * @param bean the object to have beans injected into
     * @param ctx the ServletContext to use to find the Spring ApplicationContext
     */
    public static void injectBeans(Object bean, ServletContext ctx) {
        ApplicationContext ac = (ApplicationContext) ctx.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        injectBeans(bean, ac);
    }

    public static void injectBeans(Object bean, ApplicationContext ac) {
        SpringHelper.injectBeans(bean, ac);
    }
}
