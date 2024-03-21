package org.example;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


@WebServlet({"/time"})
public class TimeServlet extends HttpServlet {

    private final TemplateEngine templateEngine = new TemplateEngine();
    private final Map<String, String> coockiesMap = new HashMap<>();


    @Override
    public void init() {
        JavaxServletWebApplication javaxServletWebApplication =
                JavaxServletWebApplication.buildApplication(this.getServletContext());
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(javaxServletWebApplication);

        resolver.setPrefix("/WEB-INF/temp/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(templateEngine.getTemplateResolvers().size());
        resolver.setCacheable(false);

        templateEngine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        resp.setContentType("text/html");
        String formattedDateTime;

        String urlParam = req.getParameter("time");


        if (req.getCookies() != null)
            Arrays.stream(req.getCookies()).forEach((c) ->
                coockiesMap.put(c.getName(), c.getValue()));

        if (urlParam == null) {
            urlParam = coockiesMap.getOrDefault("lastTimezone", "UTC+0");
            formattedDateTime = getFormattedTime(urlParam);
        } else {
            urlParam = urlParam.replace(" ", "+");
            formattedDateTime = getFormattedTime(urlParam);
            Cookie cookie = new Cookie("lastTimezone", urlParam);
            cookie.setMaxAge(10 * 60);
            resp.addCookie(cookie);
        }

        Context context = new Context(req.getLocale(), Map.of("timeText", formattedDateTime));
        templateEngine.process("time", context, resp.getWriter());
    }

    private String getFormattedTime(String urlParam) {
        LocalDateTime localNow = LocalDateTime.now(TimeZone.getTimeZone(ZoneId.of(urlParam)).toZoneId());
        return localNow.format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss")) + " " + urlParam;
    }
}
