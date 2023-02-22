package johncervantes.springproject.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import johncervantes.springproject.auth.CustomUserDetails;
import johncervantes.springproject.entity.Player;
import johncervantes.springproject.entity.PlayerStats;
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
	public String matchResults(@RequestParam("playerOne") int playerOne, @RequestParam("playerTwo") int playerTwo,
			   @RequestParam("playerThree") int playerThree, Model theModel) {
		
		scorers.clear();
		Random rand = new Random();
		userScore = 0;
		oppScore = rand.nextInt(0, 5);
		
		System.out.println(userScore + " - " + oppScore);
		
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
			
			User user = userRepository.getById(details.getId());
			
			HashMap<Integer, Integer> playerIndexMap = new HashMap<>();
			playerIndexMap.put(1, playerOne);
			playerIndexMap.put(2, playerTwo);
			playerIndexMap.put(3, playerThree);
			
			if (user.getCurrency() < 0) {
				user.setCurrency(0);
			}
			
			int playerOneScored = getGoalsScored(playerRepository.getById(playerOne));
			int playerTwoScored = getGoalsScored(playerRepository.getById(playerTwo));
			int playerThreeScored = getGoalsScored(playerRepository.getById(playerThree));
			
			
			userScore += playerOneScored + playerTwoScored + playerThreeScored;
			
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
			
			user.setCurrency(user.getCurrency() + matchEarnings);
			
			if (user.getCurrency() < 0) {
				user.setCurrency(0);
			}
			
//			int tempScore = userScore;
//			while (tempScore > 0) {
//				int playerScored = rand.nextInt(0, tempScore+1);
//				tempScore -= playerScored;
//				
//				if (playerScored > 0) {
//					int whoScored = rand.nextInt(1, 4);
//					Player player = playerRepository.getById(playerIndexMap.get(whoScored));
//					
//					// Add player to the score sheet to be printed in recap
//					scorers.add(player.getFirstName() + " " + player.getLastName());
//					
//					PlayerStats playerStats = player.getPlayerStats();
//					HashMap<String, Integer> goalMap;
//					
//					if (playerStats.getDailyGoalMapJSON() == null) {
//						playerStats.setDailyGoalMap(new HashMap<>());
//						goalMap = playerStats.getDailyGoalMap();
//					}
//					else {
//						try {
//							playerStats.deserializeDailyGoalMap();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						goalMap = playerStats.getDailyGoalMap();
//					}
//					
//					player.getPlayerStats().setGoals(player.getPlayerStats().getGoals()+playerScored);
//					
//					int xpMultiplier = rand.nextInt(1, 6);
//					
//					if ((player.getCurrentPower() + playerScored * xpMultiplier) > player.getPotentialPower()) {
//						player.setCurrentPower(player.getPotentialPower());
//					}
//					else {
//						player.setCurrentPower(player.getCurrentPower() + playerScored * xpMultiplier);
//					}
//					
//					String date = java.time.LocalDate.now().toString();
//					
//					goalMap.put(date, goalMap.getOrDefault(date, 0) + playerScored);
//					
//					try {
//						playerStats.serializeDailyGoalMap();
//					} catch (JsonProcessingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					System.out.println("Player " + player.getId() + " scored total: " + goalMap.get(date));
//					playerRepository.save(player);
//				}
//			}
			
			System.out.println("Updating Player " + playerOne);
			updateAppearanceStats(playerOne);
			
			System.out.println("Updating Player " + playerTwo);
			updateAppearanceStats(playerTwo);
			
			System.out.println("Updating Player " + playerThree);
			updateAppearanceStats(playerThree);
			
			userRepository.save(user);
		}
		
		return "match";
	}
	
	private void updateAppearanceStats(int playerId) {
		Player player = playerRepository.getById(playerId);
		PlayerStats playerStats = player.getPlayerStats();
		
		HashMap<String, Integer> appearanceHashMap = null;
		
		if (playerStats.getDailyAppearanceMapJSON() == null) {
			playerStats.setDailyAppearanceMap(new HashMap<>());
			appearanceHashMap = playerStats.getDailyAppearanceMap();
		}
		else {
			try {
				playerStats.deserializeDailyAppearanceMap();
				appearanceHashMap = playerStats.getDailyAppearanceMap();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		playerStats.setAppearances(playerStats.getAppearances() + 1);
		
		String date = java.time.LocalDate.now().toString();
		
		appearanceHashMap.put(date, appearanceHashMap.getOrDefault(date, 0) + 1);
		
		try {
			playerStats.serializeDailyAppearanceMap();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		playerRepository.save(player);
		
	}
	
	private int getGoalsScored(Player player) {
		Random rand = new Random();
		int chancesGenerated = rand.nextInt(0, 5);
		int goalsScored = 0;
		
		double playerPower = (double)player.getCurrentPower() / (float)1000;
		System.out.println("Player power: " + playerPower);
		
		for (int i = 0; i < chancesGenerated; i++) {
			double pivot = rand.nextDouble(0.00, 1.00);
			
			if (pivot < playerPower) {
				goalsScored++;
			}
		}
		
		PlayerStats playerStats = player.getPlayerStats();
		HashMap<String, Integer> goalMap;
		
		if (playerStats.getDailyGoalMapJSON() == null) {
			playerStats.setDailyGoalMap(new HashMap<>());
			goalMap = playerStats.getDailyGoalMap();
		}
		else {
			try {
				playerStats.deserializeDailyGoalMap();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			goalMap = playerStats.getDailyGoalMap();
		}
		
		player.getPlayerStats().setGoals(player.getPlayerStats().getGoals()+goalsScored);
		
		int beforePower = player.getCurrentPower();
		
		int xpMultiplier = rand.nextInt(1, 6);
		
		if ((player.getCurrentPower() + goalsScored * xpMultiplier) > player.getPotentialPower()) {
			player.setCurrentPower(player.getPotentialPower());
		}
		else {
			player.setCurrentPower(player.getCurrentPower() + goalsScored * xpMultiplier);
		}
		
		int afterPower = player.getCurrentPower();
		
		scorers.add(player.getFirstName() + " " + player.getLastName() + " scored " + goalsScored + " goals " + '\t' + " + " + (afterPower - beforePower));
		
		String date = java.time.LocalDate.now().toString();
		
		goalMap.put(date, goalMap.getOrDefault(date, 0) + goalsScored);
		
		try {
			playerStats.serializeDailyGoalMap();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Player " + player.getId() + " scored total: " + goalMap.get(date));
		playerRepository.save(player);
		
		return goalsScored;
	}
	
	@GetMapping("recap")
	public String showResult(Model theModel) {
		theModel.addAttribute("userscore", userScore);
		theModel.addAttribute("oppscore", oppScore);
		theModel.addAttribute("scorers", scorers);
		
		return "match-recap";
	}
	
}
