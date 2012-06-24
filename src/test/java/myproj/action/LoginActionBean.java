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

import myproj.exception.WrongPasswordException;
import myproj.model.User;
import myproj.service.LoginService;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;

public class LoginActionBean extends BaseActionBean {

    static final String SUCCESS_JSP = "/WEB-INF/pages/success.jsp";
    @Validate(required = true)
    public String username;
    @Validate(required = true)
    public String password;
    private User user;
    @SpringBean
    private LoginService loginService;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Resolution login() {
        try {
            user = loginService.login(username, password);
            getContext().setUser(user);
            return new RedirectResolution(SUCCESS_JSP);
        } catch (WrongPasswordException e) {
            getContext().getValidationErrors().add("password", new SimpleError("You entered a wrong password."));
        }
        return getContext().getSourcePageResolution();
    }
}
