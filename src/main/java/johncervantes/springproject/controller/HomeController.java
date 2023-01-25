package johncervantes.springproject.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import johncervantes.springproject.auth.CustomUserDetails;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	Authentication auth;
	
	@GetMapping("") 
	public String showHomePage(Model theModel) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
			System.out.println(details.getTeam());
			theModel.addAttribute("team", new String(details.getTeam()));
		}
		
		return "home";
	}
}
