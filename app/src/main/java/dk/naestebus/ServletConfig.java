package dk.naestebus;

import com.google.common.base.Charsets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import dk.naestebus.datasupplier.CachingDataSupplier;
import dk.naestebus.datasupplier.DataSupplier;
import dk.naestebus.datasupplier.RejseplanDataSupplier;
import dk.naestebus.serialize.JsonSerializer;
import dk.naestebus.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstrapping code.
 */
public class ServletConfig extends GuiceServletContextListener {

    public static final String MIME_RESPONSE_TYPE = "application/json";
    public static final String CHARACTER_ENCODING = Charsets.UTF_8.name();

    private static final Logger log = LoggerFactory.getLogger(ServletConfig.class);

    public ServletConfig() {
        log.info("Bootstrapping...");
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                serve("/stops").with(StopsServlet.class);
                serve("/departures").with(DeparturesServlet.class);

                bind(DataSupplier.class).annotatedWith(DataSupplier.Caching.class).to(CachingDataSupplier.class);
                bind(DataSupplier.class).to(RejseplanDataSupplier.class);

                bind(Serializer.class).to(JsonSerializer.class);
            }
        });
    }
}
