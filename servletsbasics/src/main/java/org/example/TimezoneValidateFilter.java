package org.example;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String urlParam = req.getParameter("time");

        if (urlParam == null)
            chain.doFilter(req, res);
        else {
            if ( urlParam.matches("UTC(-|\\s)(\\d)[0-2]{0,1}") )
                chain.doFilter(req, res);
            else {
                res.setStatus(400);
                PrintWriter out = res.getWriter();
                out.println("<html><body>");
                out.println("<h1>Invalid timezone</h1>");
                out.println("</html></body>");
            }
        }
    }
}
