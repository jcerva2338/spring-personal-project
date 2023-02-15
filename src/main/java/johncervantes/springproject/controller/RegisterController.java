package johncervantes.springproject.controller;

import javax.persistence.GeneratedValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import johncervantes.springproject.entity.User;
import johncervantes.springproject.repository.UserRepository;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping()
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
}
