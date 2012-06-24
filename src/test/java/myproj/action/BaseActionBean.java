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

import myproj.stripesext.MyprojActionBeanContext;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

public class BaseActionBean implements ActionBean {

    private MyprojActionBeanContext context;

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = (MyprojActionBeanContext) context;
    }

    @Override
    public MyprojActionBeanContext getContext() {
        return context;
    }
}