package com.nexthighspeedmetaltube;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class TestServlet extends HttpServlet {

    private static final long serialVersionUID = -226445128944332564L;

    private static final Logger log = LoggerFactory.getLogger(TestServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("GET.");
    }
}
