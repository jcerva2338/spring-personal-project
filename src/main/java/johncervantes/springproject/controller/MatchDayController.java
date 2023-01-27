package johncervantes.springproject.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import johncervantes.springproject.auth.CustomUserDetails;
import johncervantes.springproject.entity.User;
import johncervantes.springproject.repository.UserRepository;

@Controller
@RequestMapping("/matchday")
public class MatchDayController {
	
	@Autowired
	private UserRepository userRepository;
	
	private Authentication auth;
	
	@GetMapping("")
	public String showMatchMenu() {
		return "matchday";
	}
	
	@GetMapping("predictOutcome")
	public String predictOutcome() {
		Random rand = new Random();
		
		int res = rand.nextInt(0, 100);
		
		int matchEarnings = 0;
		
		if (res % 2 == 0) {
			System.out.println("Win!");
			matchEarnings = rand.nextInt(1, 100);
		}
		else {
			System.out.println("Loss!");
			matchEarnings = rand.nextInt(-25, -1);
		}
		
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
			
			User user = userRepository.getById(details.getId());
			
			user.setCurrency(user.getCurrency() + matchEarnings);
			
			if (user.getCurrency() < 0) {
				user.setCurrency(0);
			}
			
			userRepository.save(user);
		}
		
		return "redirect:/matchday";
	}
	
}
