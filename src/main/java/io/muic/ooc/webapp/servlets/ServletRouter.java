package io.muic.ooc.webapp.servlets;

import io.muic.ooc.webapp.security.SecurityService;
import io.muic.ooc.webapp.security.UserService;
import io.muic.ooc.webapp.servlets.AbstractRoutableHttpServlet;
import io.muic.ooc.webapp.servlets.HomeServlet;
import io.muic.ooc.webapp.servlets.LoginServlet;
import io.muic.ooc.webapp.servlets.LogoutServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.util.ArrayList;
import java.util.List;

public class ServletRouter {
    private final List<Class<? extends AbstractRoutableHttpServlet>> servletClasses = new ArrayList<>();

    {
        servletClasses.add(HomeServlet.class);
        servletClasses.add(LoginServlet.class);
        servletClasses.add(LogoutServlet.class);
    }

    public void init(Context ctx){

        UserService userService = new UserService();
        SecurityService securityService = new SecurityService();
        securityService.setUserService(userService);

        for (Class<? extends AbstractRoutableHttpServlet> servletClass:servletClasses) {
            try {
                AbstractRoutableHttpServlet httpServlet = servletClass.newInstance();
                httpServlet.setSecurityService(securityService);
                Tomcat.addServlet(ctx, servletClass.getSimpleName(), httpServlet);
                // TRICK: mapping with index.jsp, allow access to root path "/"
                ctx.addServletMapping(httpServlet.getPattern(), servletClass.getSimpleName());

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
