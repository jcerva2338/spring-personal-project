package johncervantes.springproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import johncervantes.springproject.auth.CustomUserDetails;
import johncervantes.springproject.entity.Player;
import johncervantes.springproject.entity.PlayerStats;
import johncervantes.springproject.entity.User;
import johncervantes.springproject.repository.PlayerRepository;
import johncervantes.springproject.repository.UserRepository;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	Authentication auth;
	
	@GetMapping("") 
	public String showHomePage(Model theModel) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
			theModel.addAttribute("user", userRepository.getById(details.getId()));
		}
		
		return "home";
	}
	
	@GetMapping("/addPlayer")
	public String addPlayer(Model theModel) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
			User user = userRepository.findByEmail(details.getEmail());
			
			Player player = new Player("Ok", "Ok", new PlayerStats());
			
			player.getPlayerStats().setPlayer(player);
			
			user.addPlayer(player);
			
			playerRepository.save(player);
		}
		
		return "redirect:/home";
	}
}
