package org.springframework.samples.petclinic;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

@Component
public class MyFilter implements javax.servlet.Filter {

	private static final int MAX_REQUESTS_PER_SECOND = 10;

	private static final int BUCKET_CAPACITY = 20;

	private static final long MAX_WAIT_TIME = TimeUnit.SECONDS.toNanos(1);

	private static final long REFILL_INTERVAL = TimeUnit.SECONDS.toNanos(1) / MAX_REQUESTS_PER_SECOND;

	private static final Map<String, AtomicLong> ipRequests = new ConcurrentHashMap<>();

	private static final Logger logger = Logger.getLogger(MyFilter.class.getName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization code
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String remoteAddr = request.getRemoteAddr();
		AtomicLong requests = ipRequests.computeIfAbsent(remoteAddr, (key) -> new AtomicLong(0));

		if (requests.get() < BUCKET_CAPACITY) {
			if (requests.incrementAndGet() <= MAX_REQUESTS_PER_SECOND) {
				logger.log(Level.INFO, "Allowing request from {0}", remoteAddr);
				chain.doFilter(request, response);
				return;
			}
			else {
				requests.decrementAndGet();
			}
		}

		long startTime = System.nanoTime();
		long waitTime = 0;
		while (waitTime < MAX_WAIT_TIME) {
			try {
				TimeUnit.NANOSECONDS.sleep(REFILL_INTERVAL);
			}
			catch (InterruptedException e) {
				// ignore
			}
			if (requests.incrementAndGet() <= MAX_REQUESTS_PER_SECOND) {
				logger.log(Level.INFO, "Allowing request from {0}", remoteAddr);
				chain.doFilter(request, response);
				return;
			}
			else {
				requests.decrementAndGet();
			}
			waitTime = System.nanoTime() - startTime;
		}

		logger.log(Level.WARNING, "Rejecting request from {0}", remoteAddr);
		throw new ServletException("Too many requests");
	}

	@Override
	public void destroy() {
		// Cleanup code
	}

}
