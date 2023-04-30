package org.springframework.samples.petclinic.system;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ErrorController {

	@GetMapping("/error")
	public String showErrorPage(HttpServletRequest request, Model model) {
		// HttpSession session = request.getSession(false);
		// if (session != null) {
		// session.invalidate();
		// }
		model.addAttribute("errorMessage", "No session key found");
		return "redirect:/login";
	}

	@PostMapping("/error")
	public String redirect(HttpServletRequest request, Model model) {
		// HttpSession session = request.getSession(false);
		// if (session != null) {
		// session.invalidate();
		// }
		model.addAttribute("errorMessage", "No session key found");
		return "redirect:/login";
	}

}
