package johncervantes.springproject.controller;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public String processRegistration(@Validated @ModelAttribute("user") CrmUser user, BindingResult theBindingResult, Model theModel) {
		String userName = user.getUsername();
		
		logger.info("Processing registration form for: " + userName);
		logger.warn(theBindingResult.getFieldErrorCount());
		if (theBindingResult.hasErrors()) {
			logger.warn(theBindingResult.getFieldErrorCount());
			return "register-form";
		}
		else {
			logger.warn("No errors!");
		}
		
		User existing = userService.findByUserName(userName);
		
		if (existing != null) {
			theModel.addAttribute("user", new CrmUser());
			theModel.addAttribute("registrationError", "User name already exists");
			
			logger.warn("User name already exists");
			
			return "register-form";
		}
		
		user.setCurrency(900);
		
		userService.save(user);
		
		logger.info("Successfully created user: " + userName);
		
		return "register-success";
	}
}
