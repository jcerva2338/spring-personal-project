package johncervantes.springproject.controller;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import johncervantes.springproject.entity.User;
import johncervantes.springproject.service.UserService;
import johncervantes.springproject.user.CrmUser;

@Controller
@RequestMapping("/register")
public class RegisterController {
	@Autowired
	private UserService userService;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@GetMapping()
	public String showRegisterForm(Model theModel) {
		theModel.addAttribute("user", new CrmUser());
		
		return "register-form";
	}
	
	@PostMapping("/process-registration")
	public String processRegistration(@ModelAttribute("user") CrmUser user) {
		String userName = user.getUsername();
		
		logger.info("Processing registration form for: " + userName);
		
		User existing = userService.findByUserName(userName);
		
		if (existing != null) {
			logger.warn("User name already exists");
			
			return "register-form";
		}
		
		userService.save(user);
		
		logger.info("Successfully created user: " + userName);
		
		return "register-success";
	}
}
