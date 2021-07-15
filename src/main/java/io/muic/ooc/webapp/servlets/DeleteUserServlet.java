package io.muic.ooc.webapp.servlets;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.model.User;
import io.muic.ooc.webapp.service.SecurityService;
import io.muic.ooc.webapp.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author gigadot
 */
public class DeleteUserServlet extends HttpServlet implements Routable {

    private SecurityService securityService;


    @Override
    public String getMapping() {
        return "/user/delete";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            String username = (String) request.getSession().getAttribute("username");
            UserService userService = UserService.getInstance();
            try{
                User currentUser = userService.findByUsername(username);
                //delete user by user username
                User deletingUser = userService.findByUsername(request.getParameter("username"));
                if(userService.deleteUserByUsername(deletingUser.getUsername())){
                    //go to user list page with successfully deleted message
                    //error message in session
                    //attributes added to persist unless removed from session
                    // ensure that they are deleted then they are read next time

                    request.getSession().setAttribute("hasError", false);
                    request.getSession().setAttribute("message",String.format("User %s is deleted", deletingUser.getUsername()));
                } else{
                    // go to user list page with error message
                    request.getSession().setAttribute("hasError", true);
                    request.getSession().setAttribute("message",String.format("Unable to delete user%s", deletingUser.getUsername()));

                }
            } catch (Exception e){
                // go to user list page with error message
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message",String.format("Unable to delete user%s",request.getParameter("username")));

            }

            response.sendRedirect("/");;
        } else {
            response.sendRedirect("/login");
        }
    }
}