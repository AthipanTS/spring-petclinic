package org.springframework.samples.petclinic.filter;

import jakarta.servlet.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class MyFilter implements Filter {

	private final int MAX_REQUESTS_PER_IP = 20;

	private final int TIME_WINDOW_SECONDS = 10;

	private Map<String, Integer> ipRequestCounts = new ConcurrentHashMap<>();

	private final ConcurrentHashMap<String, AtomicInteger> ipRequestCount = new ConcurrentHashMap<>();

	private static final Logger logger = Logger.getLogger(MyFilter.class.getName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization code
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		logger.log(Level.INFO, "inside filter");

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String ipAddress = httpRequest.getRemoteAddr();
		int requestCount = ipRequestCounts.getOrDefault(ipAddress, 0);
		if (requestCount >= MAX_REQUESTS_PER_IP) {
			httpResponse.setStatus(429);
			return;
		}

		ipRequestCounts.put(ipAddress, requestCount + 1);
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				ipRequestCounts.remove(ipAddress);
			}
		}, TIME_WINDOW_SECONDS * 1000);

		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		// Cleanup code
	}

}
