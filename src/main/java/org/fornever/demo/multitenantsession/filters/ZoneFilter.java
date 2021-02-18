package org.fornever.demo.multitenantsession.filters;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class ZoneFilter implements Filter {

    public static final String ZONE_ID = "__ZONE_ID__";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        if (httpReq.getServletPath().startsWith("/api/")) {
            HttpSession session = httpReq.getSession();
            Object sessionZoneId = session.getAttribute(ZONE_ID);
            String requestZoneId = servletRequest.getParameter("zoneId");
            httpReq.setAttribute(ZONE_ID, requestZoneId);
            if (requestZoneId == null) {
                throw new ServletException("zone id not found.");
            }
            if (sessionZoneId != null && !requestZoneId.equals(sessionZoneId)) {
                session.invalidate();
                RequestCache requestCache = new HttpSessionRequestCache();
                requestCache.saveRequest(httpReq, (HttpServletResponse) servletResponse);
                ((HttpServletResponse) servletResponse).sendRedirect("/login");
                return;
            }

            session.setAttribute(ZONE_ID, requestZoneId);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
