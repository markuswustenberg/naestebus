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
 * A {@code DeparturesServlet} serves {@link com.nexthighspeedmetaltube.model.Departure}s for a {@link com.nexthighspeedmetaltube.model.Stop} in JSON format.
 */
@Singleton
public class DeparturesServlet extends HttpServlet {

    private static final Gson serializer = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
            .create();

    private final DataSupplier dataSupplier;

    @Inject
    public DeparturesServlet(DataSupplier dataSupplier) {
        this.dataSupplier = dataSupplier;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String stopId = request.getParameter("stopId");

        if (stopId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ImmutableList<Departure> departures = dataSupplier.getNextDepartures(stopId, DateTime.now());

        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(serializer.toJson(departures));
        writer.flush();
    }

    private static class DateTimeSerializer implements JsonSerializer<DateTime> {
        @Override
        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }
}
