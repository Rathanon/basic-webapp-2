package io.muic.ooc.webapp.servlets;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;
import io.muic.ooc.webapp.service.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateUserServlet extends HttpServlet implements Routable {

    private SecurityService securityService;


    @Override
    public String getMapping() { return "/user/create";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            // do MVC in here
//            String username = (String) request.getSession().getAttribute("username");
//            UserService userService = UserService.getInstance();

//            request.setAttribute("user", userService.findByUsername(username));

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/create.jsp");
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

            // ensure username and displayName do not contain leading and trailing spaces
            String username = StringUtils.trim((String)request.getParameter("username"));
            String displayName = StringUtils.trim((String)request.getParameter("displayName"));
            String password = (String)request.getParameter("password");
            String cpassword = (String)request.getParameter("cpassword");

            UserService userService = UserService.getInstance();
            String errorMessage = null;
            // validity
                    // Username
            if(userService.findByUsername(username) != null){
                // exists
                errorMessage = String.format("Username %s has already been taken.", username);
            }
                    // Display Name
            else if(StringUtils.isBlank(displayName)){
                // cannot be blank
                errorMessage = " Display name cannot be blank.";

            }
                    // Password
            else  if(StringUtils.isBlank(password)) {
                // cannot be blank
                errorMessage = "Password cannot be blank.";
            }
            if(!StringUtils.equals(password,cpassword)){
                // password not same
                errorMessage = "Passwords do not match";
            }

            if(errorMessage != null){
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message",errorMessage);
            }else{
                // create a user
                try{
                    userService.createUser(username, password, displayName);
                    // if no error redirect
                    request.getSession().setAttribute("hasError", false);
                    request.getSession().setAttribute("message",String.format("User %s has successfully been created.", username));
                    response.sendRedirect("/");
                    return;
                } catch(Exception e){
                    request.getSession().setAttribute("hasError", true);
                    request.getSession().setAttribute("message",e.getMessage());
                }
            }

            // prefill
            request.setAttribute("username", username);
            request.setAttribute("displayName", displayName);
            request.setAttribute("password", password);
            request.setAttribute("cpassword", cpassword);

            //if cannot create
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/create.jsp");
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
