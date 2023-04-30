package org.springframework.samples.petclinic.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Order(3)
@Component
public class SessionFilter implements Filter {

	private static final Logger logger = Logger.getLogger(SessionFilter.class.getName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		logger.log(Level.INFO, "Inside sessionFilter filter...");

		// Get the current request and response objects as HttpServletRequest and
		// HttpServletResponse
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpSession session = httpRequest.getSession(false);
		String requestURI = httpRequest.getRequestURI();

		if (requestURI.contains("login") || requestURI.equals("/") || requestURI.contains("validate")
				|| requestURI.contains(".css") || requestURI.contains(".jpeg")) {
			// Exclude requests to /login and /

			logger.log(Level.INFO, "allowed endpoints...passing sessionFilter filter...");
			chain.doFilter(request, response);
			return;
		}

		if (session != null && session.getAttribute("username") != null) {
			String username = (String) session.getAttribute("username");
			logger.log(Level.INFO, "Passing Session filter for " + username);
		}
		else {
			logger.log(Level.INFO, "Unauthorized Access found");
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access");
			return;
		}

		logger.log(Level.INFO, "CSRF check success");
		// Continue with the filter chain
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// Nothing to do
	}

}
