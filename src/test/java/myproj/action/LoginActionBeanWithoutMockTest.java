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
package myproj.action;

import base.StripesWithoutMockTestFixture;
import myproj.exception.ImplementedLaterException;
import net.sourceforge.stripes.exception.StripesServletException;
import net.sourceforge.stripes.mock.MockHttpSession;
import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.ValidationErrors;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class LoginActionBeanWithoutMockTest extends StripesWithoutMockTestFixture {

    private static final Class<LoginActionBean> CLAZZ = LoginActionBean.class;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpass";
    private MockHttpSession session;
    private MockRoundtrip trip;

    @Before
    public void initRoundtrip() {
        session = new MockHttpSession(CTX);
        trip = new MockRoundtrip(CTX, CLAZZ, session);
    }

    @Test
    public void validCredentials() throws Exception {
        try {
            trip.setParameter("username", USERNAME);
            trip.setParameter("password", PASSWORD);

            trip.execute("login");

            // BOOM! Exception from real LoginService!
            fail("Expected exception from LoginService!");
        } catch (StripesServletException e) {
            assertThat(e.getRootCause(), is(ImplementedLaterException.class));
        }
    }

    @Test
    public void wrongCredentials() throws Exception {
        try {
            String wrongpass = "wrongpass";
            trip.setParameter("username", USERNAME);
            trip.setParameter("password", wrongpass);

            trip.execute("login");

            // BOOM! Exception from real LoginService!
            fail("Expected exception from LoginService!");
        } catch (StripesServletException e) {
            assertThat(e.getRootCause(), is(ImplementedLaterException.class));
        }
    }

    @Test
    public void noCredentials() throws Exception {
        trip.setParameter("username", "");
        trip.setParameter("password", "");

        trip.execute("login");

        // From here on the real unit tests
        ValidationErrors errors = trip.getValidationErrors();
        assertThat(errors.size(), is(2));
        LocalizableError error = (LocalizableError) errors.get("username").get(0);
        assertThat(error.getMessageKey(), equalTo("validation.required.valueNotPresent"));
        error = (LocalizableError) errors.get("password").get(0);
        assertThat(error.getMessageKey(), equalTo("validation.required.valueNotPresent"));
        assertThat(session.getAttribute("user"), is(nullValue()));
    }
}
