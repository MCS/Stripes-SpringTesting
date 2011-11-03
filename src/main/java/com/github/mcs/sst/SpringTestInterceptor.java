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

import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.util.Log;

/**
 * <p>An {@link Interceptor} that uses a Spring context to inject Spring beans into newly created
 * ActionBeans immediately following ActionBeanResolution.  For more information on how the injection
 * is performed see {@link SpringHelper#injectBeans(Object,
 *  net.sourceforge.stripes.action.ActionBeanContext)}.</p>
 *
 * <p>To configure the SpringInterceptor for use you will need to add the following to your
 * web.xml (assuming no other interceptors are yet configured):</p>
 *
 * <pre>
 * &lt;init-param&gt;
 *     &lt;param-name&gt;Interceptor.Classes&lt;/param-name&gt;
 *     &lt;param-value&gt;
 *         net.sourceforge.stripes.integration.spring.SpringInterceptor,
 *         net.sourceforge.stripes.controller.BeforeAfterMethodInterceptor
 *     &lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 *
 * <p>If one or more interceptors are already configured in your web.xml simply separate the
 * fully qualified names of the interceptors with commas (additional whitespace is ok).</p>
 *
 * @see SpringBean
 * @author Tim Fennell
 * @author Marcus Krassmann
 * @since Stripes 1.3
 */
@Intercepts(LifecycleStage.ActionBeanResolution)
public class SpringTestInterceptor implements Interceptor {

    private static final Log log = Log.getInstance(SpringTestInterceptor.class);

    /**
     * Allows ActionBean resolution to proceed and then once the ActionBean has been
     * located invokes the {@link SpringTestHelper} to perform Spring based dependency injection.
     *
     * @param context the current execution context
     * @return the Resolution produced by calling context.proceed()
     * @throws Exception if the Spring binding process produced unrecoverable errors
     */
    public Resolution intercept(ExecutionContext context) throws Exception {
        Resolution resolution = context.proceed();
        log.debug("Running Spring dependency injection for instance of ",
                context.getActionBean().getClass().getSimpleName());
        // using our own helper class to make Spring beans replacable and configurable at runtime
        SpringTestHelper.injectBeans(context.getActionBean(), context.getActionBeanContext());
        return resolution;
    }
}
