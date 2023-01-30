package johncervantes.springproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import johncervantes.springproject.entity.User;
import johncervantes.springproject.repository.UserRepository;

@Controller
public class LoginController {
	
	private boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
			return false;
		}
		
		return authentication.isAuthenticated();
	}
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("")
	public String l() {
		if (isAuthenticated()) {
			return "redirect:/home";
		}
		
		return "fancy-login";
	}
	
	@GetMapping("/register")
	public String showRegisterForm(Model theModel) {
		theModel.addAttribute("user", new User());
		
		return "register-form";
	}
	
	@PostMapping("/process-registration")
	public String processRegistration(@ModelAttribute("user") User user) {
		
		// Encode the user entered password and reinsert BCrypt equivalent into password field
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setCurrency(100);
		
		userRepository.save(user);
		
		return "register-success";
	}
	
	@GetMapping("/showMyLoginPage")
	public String showMyLoginPage() {
		return "fancy-login";
	}
	
	// add request mapping for /access-denied
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		return "access-denied";
	}
}
