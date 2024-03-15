package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


@WebServlet({"/time"})
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        String urlParam = req.getParameter("time");

        if (urlParam == null) urlParam = "UTC+0";

        urlParam = urlParam.replace(" ", "+");
        LocalDateTime localNow = LocalDateTime.now(TimeZone.getTimeZone(ZoneId.of(urlParam)).toZoneId());
        String formattedDateTime = localNow.format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss"));

        out.println("<html><body>");
        out.println("<h1> ${time} ${offset}</h1>"
                .replace("${time}", urlParam)
                .replace("${offset}", formattedDateTime));
        out.println("</html></body>");

        out.close();
    }
}
