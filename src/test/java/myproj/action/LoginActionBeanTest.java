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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import myproj.exception.WrongPasswordException;
import myproj.model.User;
import myproj.service.LoginService;
import net.sourceforge.stripes.mock.MockHttpSession;
import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import base.StripesTestFixture;

public class LoginActionBeanTest extends StripesTestFixture {

    private static final Class<LoginActionBean> CLAZZ = LoginActionBean.class;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpass";
    @Autowired
    private LoginService loginService;
    private MockHttpSession session;
    private MockRoundtrip trip;

    @Before
    public void initFixture() throws Exception {
        initMockService();
        resetStripesMocks();
    }

    public void initMockService() throws Exception {
        when(loginService.login(USERNAME, PASSWORD)).thenReturn(new User());
    }

    private void resetStripesMocks() {
        session = new MockHttpSession(CTX);
        trip = new MockRoundtrip(CTX, CLAZZ, session);
    }

    @Test
    public void validCredentials() throws Exception {
        trip.setParameter("username", USERNAME);
        trip.setParameter("password", PASSWORD);

        trip.execute("login");

        LoginActionBean bean = trip.getActionBean(CLAZZ);
        assertThat(bean.getUser(), notNullValue());
        assertThat(trip.getValidationErrors().size(), is(0));
        assertThat(trip.getRedirectUrl(), is("/mock_ctx" + LoginActionBean.SUCCESS_JSP));
    }

    @Test
    public void wrongCredentials() throws Exception {
        String wrongpass = "wrongpass";
        when(loginService.login(USERNAME, wrongpass)).thenThrow(new WrongPasswordException());
        trip.setParameter("username", USERNAME);
        trip.setParameter("password", wrongpass);

        trip.execute("login");

        assertThat(trip.getForwardUrl(), equalTo(MockRoundtrip.DEFAULT_SOURCE_PAGE));
        assertThat(trip.getValidationErrors().size(), is(1));
        assertThat(session.getAttribute("user"), is(nullValue()));
    }

    @Test
    public void noCredentials() throws Exception {
        trip.execute("login");

        ValidationErrors errors = trip.getValidationErrors();
        assertThat(errors.size(), is(2));
        LocalizableError error = (LocalizableError) errors.get("username").get(0);
        assertThat(error.getMessageKey(), equalTo("validation.required.valueNotPresent"));
        error = (LocalizableError) errors.get("password").get(0);
        assertThat(error.getMessageKey(), equalTo("validation.required.valueNotPresent"));
        assertThat(session.getAttribute("user"), is(nullValue()));
    }
}
