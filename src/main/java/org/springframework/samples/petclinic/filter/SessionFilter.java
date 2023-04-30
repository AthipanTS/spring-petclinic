package org.springframework.samples.petclinic.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.*;
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

		logger.log(Level.INFO, "inside sessionFilter filter");

		// Get the current request and response objects as HttpServletRequest and
		// HttpServletResponse
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpSession session = httpRequest.getSession(false);

		if (session != null && session.getAttribute("username") != null) {
			String username = (String) session.getAttribute("username");
			System.out.println("Username: " + username);
		}
		else {
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
