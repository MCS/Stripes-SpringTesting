package myproj.stripesext;

import myproj.model.User;
import net.sourceforge.stripes.action.ActionBeanContext;

public class MyprojActionBeanContext extends ActionBeanContext {

    public static final String SESSION_USER = "user";

    public User getUser() {
        return (User) getRequest().getSession().getAttribute("user");
    }

    public void setUser(User user) {
        getRequest().getSession().setAttribute(SESSION_USER, user);
    }
}
