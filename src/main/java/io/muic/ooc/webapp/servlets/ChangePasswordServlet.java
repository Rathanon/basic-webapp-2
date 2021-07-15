package io.muic.ooc.webapp.servlets;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.model.User;
import io.muic.ooc.webapp.service.SecurityService;
import io.muic.ooc.webapp.service.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangePasswordServlet extends HttpServlet implements Routable {

    private SecurityService securityService;


    @Override
    public String getMapping() { return "/user/password";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            String username = StringUtils.trim((String)request.getParameter("username")); // from query part
            UserService userService = UserService.getInstance();

            // prefill
            User user = userService.findByUsername(username);
            request.setAttribute("user", user);
            request.setAttribute("username", user.getUsername());


            //if cannot create
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/password.jsp");
            rd.include(request, response);

            //removes attributes after they are used( flash session)
            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");
        } else {
            //add for precaution to delete attributes
            request.removeAttribute("hasError");
            request.removeAttribute("message");
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);

        if (authorized) {
            //change password is similar to edit used, but we only allow editing display name
            // ensure username do not contain leading and trailing spaces
            String username = StringUtils.trim((String)request.getParameter("username")); // from query part
            String password = (String)request.getParameter("password");
            String cpassword = (String)request.getParameter("cpassword");

            UserService userService = UserService.getInstance();
            User user = userService.findByUsername(username);

            String errorMessage = null;
            // validity
            // existence
            // Display Name
            if(user == null){
                // exists
                errorMessage = String.format("User %s does not exist.", username);
            }
            else  if(StringUtils.isBlank(password)) {
                // cannot be blank
                errorMessage = "Password cannot be blank.";
            }
            else if(!StringUtils.equals(password,cpassword)){
                // password not same
                errorMessage = "Passwords do not match";
            }

            if(errorMessage != null){
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message",errorMessage);
            }else{
                // edit a user
                try{
                    userService.changePassword(username, password);
                    // if no error redirect
                    request.getSession().setAttribute("hasError", false);
                    request.getSession().setAttribute("message",String.format("Password for user  %s has successfully been changed.", username));
                    response.sendRedirect("/");
                    return;
                } catch(Exception e){
                    request.getSession().setAttribute("hasError", true);
                    request.getSession().setAttribute("message",e.getMessage());
                }
            }

            // prefill
            request.setAttribute("username", username);
            //if cannot create
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/password.jsp");
            rd.include(request, response);

            //removes attributes after they are used( flash session)
            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");
        } else {
            //add for precaution to delete attributes
            request.removeAttribute("hasError");
            request.removeAttribute("message");
            response.sendRedirect("/login");
        }
    }
}
