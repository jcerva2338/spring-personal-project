package johncervantes.springproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import johncervantes.springproject.auth.CustomUserDetails;
import johncervantes.springproject.auth.CustomUserDetailsService;
import johncervantes.springproject.entity.Player;
import johncervantes.springproject.entity.User;
import johncervantes.springproject.repository.PlayerRepository;
import johncervantes.springproject.repository.UserRepository;

@Controller
@RequestMapping("/players")
public class PlayerController {
	
	Authentication auth;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PlayerRepository playerRepository;
	
	@GetMapping("")
	public String showPlayers(Model theModel) {
		
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
		
			List<Player> players = playerRepository.findAllByUserId(details.getId());
		
			theModel.addAttribute("players", players);
		}
		
		return "players";
	}
	
	
	@GetMapping("updatePlayer")
	public String showUpdatePlayer(@RequestParam("playerId") int playerId, Model theModel) {
		Player tempPlayer = playerRepository.getById(playerId);
		
		theModel.addAttribute("player", tempPlayer);
		
		return "add-update-player";
	}
	
	@GetMapping("/addPlayer")
	public String addPlayer(Model theModel) {
		Player tempPlayer = new Player();
		
		theModel.addAttribute("player", tempPlayer);
		
//		auth = SecurityContextHolder.getContext().getAuthentication();
//		if (!(auth instanceof AnonymousAuthenticationToken)) {
//			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
//			User user = userRepository.findByEmail(details.getEmail());
//			
//			Player player = new Player("Ok", "Ok");
//			
//			user.addPlayer(player);
//			
//			playerRepository.save(player);
//		}
		
		return "add-update-player";
	}
	
	@PostMapping("/savePlayer")
	public String savePlayer(@ModelAttribute("player") Player player, Model theModel) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
			User user = userRepository.findByEmail(details.getEmail());
			
			if (player.getId() == 0) {
				user.addPlayer(player);
			}
			
			player.setUser(user);
			playerRepository.save(player);
		}
		
		return "redirect:/players";
	}
	
	@GetMapping("/deletePlayer")
	public String deletePlayer(@RequestParam("playerId") int playerId) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			playerRepository.deleteById(playerId);
		}
		return "redirect:/players";
	}
	
	@GetMapping("/aboutPlayer")
	public String aboutPlayer(@RequestParam("playerId") int playerId, Model theModel) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			Player player = playerRepository.getById(playerId);
			
			theModel.addAttribute("player", player);
		}
		
		return "player-profile";
	}
}
