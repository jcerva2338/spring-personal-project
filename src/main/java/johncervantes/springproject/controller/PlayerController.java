package johncervantes.springproject.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import johncervantes.springproject.entity.PlayerStats;
import johncervantes.springproject.entity.User;
import johncervantes.springproject.repository.PlayerRepository;
import johncervantes.springproject.repository.UserRepository;
import johncervantes.springproject.service.UserService;

@Controller
@RequestMapping("/players")
public class PlayerController {
	
	Authentication auth;
	
	@Autowired
	UserService userService;
	
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
		
			theModel.addAttribute("user", userRepository.getById(details.getId()));
			theModel.addAttribute("players", players);
			
			List<Player> sortedPlayers = playerRepository.findAll(Sort.by("currentPower"));
			System.out.println("Size: " + sortedPlayers.size());
			int topThreePowerSum;
			
			if (sortedPlayers == null || sortedPlayers.size() < 3) {
				topThreePowerSum = 0;
			}
			else {
			 topThreePowerSum = sortedPlayers.get(0).getCurrentPower()
					+ sortedPlayers.get(1).getCurrentPower()  
					+ sortedPlayers.get(2).getCurrentPower();
			}
			
			theModel.addAttribute("topPower", topThreePowerSum);
			
			
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
		tempPlayer.setPlayerStats(new PlayerStats());
		tempPlayer.getPlayerStats().setPlayer(tempPlayer);
		
		theModel.addAttribute("player", tempPlayer);
		
//		auth = SecurityContextHolder.getContext().getAuthentication();
//		if (!(auth instanceof AnonymousAuthenticationToken)) {
//			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
//			User user = userRepository.findByEmail(details.getEmail());
//			user.setCurrency(user.getCurrency() - 300);
//			
//			userRepository.save(user);
////			
////			Player player = new Player("Ok", "Ok");
////			
////			user.addPlayer(player);
////			
////			playerRepository.save(player);
//		}
//		
		
		return "add-update-player";
	}
	
	@PostMapping("/savePlayer")
	public String savePlayer(@ModelAttribute("player") Player player, Model theModel) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();

			User user = userService.findByUserName(details.getUsername());
			
			Random random = new Random();
			
			final int NUM_HEADS_AVAILABLE = 2;
			final int NUM_BODYS_AVAILABLE = 1;
			
			final int powerLimit = 1000;
			
			if (player.getId() == 0) {
				player.setHead(random.nextInt(1, NUM_HEADS_AVAILABLE+1));
				player.setBody(random.nextInt(1, NUM_BODYS_AVAILABLE+1));
				player.setPlayerStats(new PlayerStats(player));
				player.getPlayerStats().setGoals(0);
				player.getPlayerStats().setAppearances(0);
				player.setJoinDate(java.time.LocalDate.now().toString());
				
				int curPower = random.nextInt(1, powerLimit+1);
				int potentialPower = random.nextInt(curPower, powerLimit+1);
				
				player.setCurrentPower(curPower);
				player.setPotentialPower(potentialPower);
				
				user.setCurrency(user.getCurrency() - 300);
				
				player.setUser(user);
				user.addPlayer(player);
			}
			
//			player.setUser(user);
			playerRepository.save(player);
		}
		
		return "redirect:/players";
	}
	
	@GetMapping("/deletePlayer")
	public String deletePlayer(@RequestParam("playerId") int playerId) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			playerRepository.deleteById(playerId);
			System.out.println("DONE!");
		}
		return "redirect:/players";
	}
	
	@GetMapping("/aboutPlayer")
	public String aboutPlayer(@RequestParam("playerId") int playerId, Model theModel) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
			Player player = playerRepository.getById(playerId);
			
			try {
				player.getPlayerStats().deserializeDailyGoalMap();
				player.getPlayerStats().serializeDailyGoalMap();
				
				player.getPlayerStats().deserializeDailyAppearanceMap();
				player.getPlayerStats().serializeDailyAppearanceMap();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("ERROR!");
			}
			
			theModel.addAttribute("player", player);
			
//			try {
//				player.getPlayerStats().deserializeDailyGoalMap();
//				player.getPlayerStats().serializeDailyGoalMap();
//				System.out.println("hi");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
			System.out.println("In progress!");
			HashMap<String, Integer> playerStats = player.getPlayerStats().getDailyGoalMap();
			HashMap<String, Integer> playerAppearances = player.getPlayerStats().getDailyAppearanceMap();
			
			Set<String> dateSet = playerStats.keySet();
			String[] dates = dateSet.toArray(new String[dateSet.size()]);
			
			Integer[] goals = (Integer[]) playerStats.values().toArray(new Integer[playerStats.values().size()]);
			Integer[] appearances = (Integer[]) playerAppearances.values().toArray(new Integer[playerAppearances.values().size()]);
			
			for (String s : dates) {
				System.out.println(s);
			}
			
			for (Integer i : goals) {
				System.out.println(i);
			}
			
			for (Integer a: appearances) {
				System.out.println("APP: " + a);
			}
			
			System.out.println("Completed!");
			
			theModel.addAttribute("user", userRepository.getById(details.getId()));
			theModel.addAttribute("dates", dates);
			theModel.addAttribute("goals", goals);
			theModel.addAttribute("appearancesTotal", player.getPlayerStats().getAppearances());
			theModel.addAttribute("goalAppearanceAvg", (float)player.getPlayerStats().getGoals() / (float)player.getPlayerStats().getAppearances());
			theModel.addAttribute("appearances", appearances);
			                
			theModel.addAttribute("data", player.getPlayerStats().getDailyGoalMapJSON());
		}
		
		return "player-profile";
	}
}
