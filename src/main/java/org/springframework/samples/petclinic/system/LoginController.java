package org.springframework.samples.petclinic.system;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jakarta.servlet.ServletContext;
import org.springframework.samples.petclinic.filter.DosFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class LoginController {

	private static final Logger logger = Logger.getLogger(LoginController.class.getName());

	@GetMapping("/validate")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password) {

		logger.log(Level.INFO, "inside logincontroller filter   " + username + "    pwd= " + password);

		if (username.equals("admin") && password.equals("admin")) {

			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getResponse();

			String csrfToken = UUID.randomUUID().toString();

			Cookie cookie = new Cookie("itsCSRF", csrfToken);
			response.addCookie(cookie);

			// authentication successful
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest()
				.getSession(true);

			session.setAttribute("username", username);
			return "redirect:/owners";

		}
		else {
			// authentication failed
			HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest()
				.getSession();
			session.setAttribute("username", "false");
			return "redirect:/login?error=true";
		}
	}

}
