package com.nexthighspeedmetaltube;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.nexthighspeedmetaltube.datasupplier.DataSupplier;
import com.nexthighspeedmetaltube.datasupplier.RejseplanDataSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstrapping code.
 */
public class ServletConfig extends GuiceServletContextListener {

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

                bind(DataSupplier.class).to(RejseplanDataSupplier.class);
            }
        });
    }
}
