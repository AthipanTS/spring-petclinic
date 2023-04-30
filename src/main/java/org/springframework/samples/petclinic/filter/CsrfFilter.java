package org.springframework.samples.petclinic.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Order(2)
@Component
public class CsrfFilter implements Filter {

	private static final String CSRF_TOKEN_ATTR_NAME = "csrfToken";

	private static final String CSRF_PARAM_NAME = "csrfParam";

	private static final Logger logger = Logger.getLogger(SessionFilter.class.getName());

	private SecureRandom secureRandom;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		secureRandom = new SecureRandom();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		logger.log(Level.INFO, "Inside csrf filter");

		// Get the current request and response objects as HttpServletRequest and
		// HttpServletResponse
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {

			String csrfParamValue = null;

			Cookie[] cookies = ((HttpServletRequest) request).getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("itsCSRF")) {
						csrfParamValue = cookie.getValue();

					}
				}
			}

			if (csrfParamValue == null) {
				logger.log(Level.INFO, "CSRF check Failure...");
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
				return;
			}
		}

		logger.log(Level.INFO, "Passing CSRF check...");
		// Continue with the filter chain
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// Nothing to do
	}

}
