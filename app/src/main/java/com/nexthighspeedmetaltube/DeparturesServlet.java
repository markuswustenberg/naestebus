package com.nexthighspeedmetaltube;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nexthighspeedmetaltube.datasupplier.DataSupplier;
import com.nexthighspeedmetaltube.model.Departure;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

/**
 * A {@code DeparturesServlet} serves {@link com.nexthighspeedmetaltube.model.Departure}s for a {@link com.nexthighspeedmetaltube.model.Stop}.
 * <p>
 * Retrieve departures using HTTP GET, given the parameters {@code stopId} and {@code max}. The response is in JSON format.
 * <p>
 * {@code stopId} is the id of the stop. Note that this value is volatile, so don't depend on it being stable over large time periods (i.e. days).
 * {@code max} is the maximum number of departures returned. Note that there is an unspecified internal maximum also.
 * <p>
 * Example usage:
 * <p>
 * <code>$ curl "http://localhost:8080/departures?stopId=751464200&max=2"</code>
 * <p>
 * <code>[{"name":"Bus 2A","direction":"Bjødstrupvej/Karetmagertoften (Aarhus)","hasDirection":true,"time":"21:05"},
 * {"name":"Bus 2A","direction":"Aarhus Uni. hospital  Skejby indg 8-11","hasDirection":true,"time":"21:12"}]</code>
 */
@Singleton
public class DeparturesServlet extends HttpServlet {

    private static final String STOP_ID_PARAMETER_NAME = "stopId";
    private static final String MAX_PARAMETER_NAME = "max";
    private static final String TIME_PATTERN = "HH:mm";

    private static final Gson serializer = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
            .create();

    private final DataSupplier dataSupplier;

    @Inject
    DeparturesServlet(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String stopId = request.getParameter(STOP_ID_PARAMETER_NAME);
        String max = request.getParameter(MAX_PARAMETER_NAME);

        if (stopId == null || max == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ImmutableList<Departure> departures = dataSupplier.getNextDepartures(stopId, DateTime.now());
        departures = departures.subList(0, Integer.parseInt(max));

        response.setContentType(ServletConfig.MIME_RESPONSE_TYPE);
        PrintWriter writer = response.getWriter();
        writer.write(serializer.toJson(departures));
        writer.flush();
    }

    private static class DateTimeSerializer implements JsonSerializer<DateTime> {
        @Override
        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString(TIME_PATTERN));
        }
    }
}
