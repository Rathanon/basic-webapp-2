package io.muic.ooc.webapp;

import java.io.File;
import javax.servlet.ServletException;

import io.muic.ooc.webapp.servlets.ServletRouter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class Webapp {

    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8082);

        File docBase = new File("src/main/webapp/");
        docBase.mkdirs();

        try {
            Context ctx = tomcat.addWebapp("", docBase.getAbsolutePath());

            ServletRouter servletRouter = new ServletRouter();
            servletRouter.init(ctx);

            tomcat.start();
            tomcat.getServer().await();
        } catch (ServletException e){
            e.printStackTrace();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

    }
}
