package org.route;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
/** GIVEN - do not edit. Runs Tomcat inside main() and gives it YOUR Spring container. */
public class WebServer implements AutoCloseable {
    private final AnnotationConfigWebApplicationContext context;
    private final int port;
    private final Tomcat tomcat = new Tomcat();
    public WebServer(AnnotationConfigWebApplicationContext context, int port) {
        this.context = context;
        this.port = port;
    }
    public void start() {
        try {
            Path base = Files.createTempDirectory("nilemart-tomcat");
            tomcat.setBaseDir(base.toString());
            tomcat.setPort(port);
            tomcat.getConnector();
            Context webApp = tomcat.addContext("", base.toString());
            Tomcat.addServlet(webApp, "dispatcher", new DispatcherServlet(context)).setLoadOnStartup(1);
            webApp.addServletMappingDecoded("/", "dispatcher");
            tomcat.start();
        } catch (LifecycleException | IOException e) {
            throw new IllegalStateException("Could not start the web server on port " + port, e);
        }
        if (!context.isActive()) {
            throw new IllegalStateException("The Spring container did not start - read the log above.");
        }
        System.out.println("Web store running at http://localhost:" + port + "/");
    }
    @Override
    public void close() {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            System.err.println("Web server did not stop cleanly: " + e.getMessage());
        }
    }
}
