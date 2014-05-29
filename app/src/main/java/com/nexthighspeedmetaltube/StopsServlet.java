package com.nexthighspeedmetaltube;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nexthighspeedmetaltube.datasupplier.DataSupplier;
import com.nexthighspeedmetaltube.model.Stop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A {@code StopsServlet} serves nearby {@link com.nexthighspeedmetaltube.model.Stop}s in JSON format.
 */
@Singleton
public class StopsServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(StopsServlet.class);

    private final DataSupplier dataSupplier;

    @Inject
    public StopsServlet(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        String radius = request.getParameter("radius");
        String max = request.getParameter("max");

        if (latitude == null || longitude == null || radius == null || max == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ImmutableList<Stop> stops = dataSupplier.getNearbyStops(Integer.parseInt(latitude), Integer.parseInt(longitude), Integer.parseInt(radius), Integer.parseInt(max));

        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(new Gson().toJson(stops));
        writer.flush();
    }
}
