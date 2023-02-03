package johncervantes.springproject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/matchday")
public class MatchDayController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	private Authentication auth;
	
	private int userScore;
	
	private int oppScore;
	
	List<String> scorers = new ArrayList<>();
	
	@GetMapping("")
	public String showMatchMenu(Model theModel) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
			
			User user = userRepository.getById(details.getId());
			
			theModel.addAttribute("user", user);
			
			// Add the players to the Model for the drop down selection
			List<Player> players = user.getPlayers();
			
			theModel.addAttribute("players", players);
		}
		
		return "matchday";
	}
	
	@GetMapping("matchResults")
	public String matchResults(@RequestParam("playerOne") int playerOne, @RequestParam("playerOne") int playerTwo,
			   @RequestParam("playerThree") int playerThree, Model theModel) {
		scorers.clear();
		Random rand = new Random();
		
		userScore = rand.nextInt(0, 5);
		oppScore = rand.nextInt(0, 5);
		
		System.out.println(userScore + " - " + oppScore);
		
		// Generate match earnings
		int matchEarnings = 0;
		
		if (userScore > oppScore) {
			System.out.println("Win!");
			matchEarnings = rand.nextInt(1, 100);
		}
		else if (userScore < oppScore){
			System.out.println("Loss!");
			matchEarnings = rand.nextInt(-25, -1);
		}
		else {
			System.out.println("Tie.");
		}
		
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
			
			User user = userRepository.getById(details.getId());
			
			user.setCurrency(user.getCurrency() + matchEarnings);
			
			if (user.getCurrency() < 0) {
				user.setCurrency(0);
			}
			
			int tempScore = userScore;
			while (tempScore > 0) {
				int playerScored = rand.nextInt(0, tempScore+1);
				tempScore -= playerScored;
				
				if (playerScored > 0) {
					int whoScored = rand.nextInt(1, 4);
					Player player = playerRepository.getById(whoScored);
					
					// Add player to the score sheet to be printed in recap
					scorers.add(player.getFirstName() + " " + player.getLastName());
					
					switch (whoScored) {
					case 1:
						player.setGoals(player.getGoals()+playerScored);
						System.out.println("p1 scored " + playerScored);
						break;
					case 2:
						player.setGoals(player.getGoals()+playerScored);
						System.out.println("p2 scored " + playerScored);
						break;
					case 3:
						player.setGoals(player.getGoals()+playerScored);
						System.out.println("p3 scored " + playerScored);
						break;
					default:
						break;
					}
				}
			}
			
			userRepository.save(user);
		}
		
		return "match";
	}
	
	@GetMapping("recap")
	public String showResult(Model theModel) {
		theModel.addAttribute("userscore", userScore);
		theModel.addAttribute("oppscore", oppScore);
		theModel.addAttribute("scorers", scorers);
		
		return "match-recap";
	}
	
}
