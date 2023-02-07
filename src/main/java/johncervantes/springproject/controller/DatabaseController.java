package johncervantes.springproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import johncervantes.springproject.auth.CustomUserDetails;
import johncervantes.springproject.entity.Player;
import johncervantes.springproject.entity.User;
import johncervantes.springproject.repository.PlayerRepository;
import johncervantes.springproject.repository.UserRepository;

@Controller
@RequestMapping("/database")
public class DatabaseController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PlayerRepository playerRepository;
	
	Authentication auth;
	
	@GetMapping("")
	public String showDatabase(@RequestParam String sortBy, Model theModel) {
		
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
			theModel.addAttribute("user", userRepository.getById(details.getId()));
			
			List<User> users = userRepository.findAll();
			theModel.addAttribute("users", users);
			
			List<Player> players = playerRepository.findAll(Sort.by(sortBy));
			theModel.addAttribute("players", players);
		
		}
		
		return "player-database";
	}
	
}
