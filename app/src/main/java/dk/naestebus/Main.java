package dk.naestebus;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Start the server.
 */
public class Main {

    public static final String WAR_PATH = "build/libs/";
    public static final String WAR_FILE_ENDING = "war";

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) throws Exception {
        // Find war file
        File warPath = new File(WAR_PATH);

        log.info("Starting server on path {} with war path {}...", new File(".").getAbsolutePath(), warPath.getAbsolutePath());

        File[] wars = warPath.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(WAR_FILE_ENDING);
            }
        });

        if (wars == null || wars.length == 0) {
            log.error("No war file found, exiting.");
            return;
        }

        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setWar(wars[0].getAbsolutePath());
        server.setHandler(context);
        server.start();
        server.join();
    }
}
