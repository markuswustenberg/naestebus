package dk.naestebus;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Start the server.
 */
public class Main {

    public static final String WAR_PATH = "./build/libs/";
    public static final String WAR_FILE_ENDING = "war";

    public static void main(String... args) throws Exception {
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        // Find war file
        File[] wars = new File(WAR_PATH).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(WAR_FILE_ENDING);
            }
        });
        context.setWar(wars[0].getAbsolutePath());
        server.setHandler(context);
        server.start();
        server.join();
    }
}
