package org.springframework.samples.petclinic.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilter implements Filter {

    private static final int MAX_REQUESTS_PER_SECOND = 10;
    private static final long MILLISECONDS_IN_SECOND = 1000L;
    private long lastTime = System.currentTimeMillis();
    private long count = 0;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (allowRequest()) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Too many requests");
        }
    }

    private synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        if (now - lastTime >= MILLISECONDS_IN_SECOND) {
            lastTime = now;
            count = 0;
        }
        return ++count <= MAX_REQUESTS_PER_SECOND;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
