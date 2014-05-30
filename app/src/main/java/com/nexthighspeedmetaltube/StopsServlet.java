package com.nexthighspeedmetaltube;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nexthighspeedmetaltube.datasupplier.DataSupplier;
import com.nexthighspeedmetaltube.model.Coordinate;
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
 * A {@code StopsServlet} serves nearby {@link com.nexthighspeedmetaltube.model.Stop}s.
 * <p>
 * Retrieve nearby stops using HTTP GET, given the parameters {@code latitude}, {@code longitude}, {@code radius}, and {@code max}. The response is in JSON format.
 * <p>
 * {@code latitude} is the latitude in WGS84 format, except that it is first multiplied by a million (1000000) and then converted to an integer.
 * {@code longitude} is the longitude in WGS84 format, likewise except that it is first multiplied by a million (1000000) and then converted to an integer.
 * {@code radius} gives the search radius in meters. The range is unspecified.
 * {@code max} is the maximum number of stops returned. Note that there is an unspecified internal maximum also.
 * <p>
 * Example usage:
 * <p>
 * <code>$ curl "http://localhost:8080/stops?latitude=56172628&longitude=10186995&radius=1000&max=2"</code>
 * <p>
 * <code>[{"id":"751464200","name":"Paludan-Müllers Vej/Ekkodalen (Aarhus)","coordinate":{"latitude":56172728,"longitude":10183438}},
 * {"id":"751464100","name":"Paludan-Müllers Vej/Åbogade (Aarhus)","coordinate":{"latitude":56170247,"longitude":10186692}}]</code>
 */
@Singleton
public class StopsServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(StopsServlet.class);

    private final DataSupplier dataSupplier;

    @Inject
    StopsServlet(DataSupplier dataSupplier) {
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

        Coordinate coordinate = new Coordinate(Integer.parseInt(latitude), Integer.parseInt(longitude));
        ImmutableList<Stop> stops = dataSupplier.getNearbyStops(coordinate, Integer.parseInt(radius), Integer.parseInt(max));

        response.setContentType(ServletConfig.MIME_RESPONSE_TYPE);
        PrintWriter writer = response.getWriter();
        writer.write(new Gson().toJson(stops));
        writer.flush();
    }
}
