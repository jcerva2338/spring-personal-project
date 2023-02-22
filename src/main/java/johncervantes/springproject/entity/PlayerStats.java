package johncervantes.springproject.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "player_statistics")
public class PlayerStats {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE},
			fetch = FetchType.LAZY)
	@JoinColumn(name = "player_id")
	private Player player;
	
	@Column(name = "goals")
	private int goals;
	
	@Column(name = "appearances")
	private int appearances;
	
	@Column(name = "daily_goal_map_json")
	private String dailyGoalMapJSON;
	
	@Column(name = "daily_appearance_map_json")
	private String dailyAppearanceMapJSON;
	
	@Convert(converter = HashMapConverter.class)
	@Transient
	private HashMap<String, Integer> dailyGoalMap;
	
	@Convert(converter = HashMapConverter.class)
	@Transient
	private HashMap<String, Integer> dailyAppearanceMap;
	
	public void serializeDailyGoalMap() throws JsonProcessingException {
		System.out.println("Doing serialization!");
		ObjectMapper objectMapper = new ObjectMapper();
		this.dailyGoalMapJSON = objectMapper.writeValueAsString(dailyGoalMap);
	}
	
	public void deserializeDailyGoalMap() throws IOException {
		if (dailyGoalMapJSON == null) { 
			this.dailyGoalMap = new HashMap<>();
			return; 
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		this.dailyGoalMap = objectMapper.readValue(dailyGoalMapJSON, new TypeReference<HashMap<String, Integer>>() {});
	}
	
	public void serializeDailyAppearanceMap() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		this.dailyAppearanceMapJSON = objectMapper.writeValueAsString(dailyAppearanceMap);
	}
	
	public void deserializeDailyAppearanceMap() throws IOException {
		if (dailyAppearanceMapJSON == null) { 
			this.dailyAppearanceMap = new HashMap<>();
			return; 
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		this.dailyAppearanceMap = objectMapper.readValue(dailyAppearanceMapJSON, new TypeReference<HashMap<String, Integer>>() {});
	}
	
	
	public PlayerStats() {
	}
	
	public PlayerStats(Player player) {
		this.player = player;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}

	public HashMap<String, Integer> getDailyGoalMap() {
		return dailyGoalMap;
	}

	public void setDailyGoalMap(HashMap<String, Integer> dailyGoalMap) {
		this.dailyGoalMap = dailyGoalMap;
	}

	public String getDailyGoalMapJSON() {
		return dailyGoalMapJSON;
	}

	public void setDailyGoalMapJSON(String dailyGoalMapJSON) {
		this.dailyGoalMapJSON = dailyGoalMapJSON;
	}

	public int getAppearances() {
		return appearances;
	}

	public void setAppearances(int appearances) {
		this.appearances = appearances;
	}

	public String getDailyAppearanceMapJSON() {
		return dailyAppearanceMapJSON;
	}

	public void setDailyAppearanceMapJSON(String dailyAppearanceMapJSON) {
		this.dailyAppearanceMapJSON = dailyAppearanceMapJSON;
	}

	public HashMap<String, Integer> getDailyAppearanceMap() {
		return dailyAppearanceMap;
	}

	public void setDailyAppearanceMap(HashMap<String, Integer> dailyAppearanceMap) {
		this.dailyAppearanceMap = dailyAppearanceMap;
	}

	@Override
	public String toString() {
		return "PlayerStats [id=" + id + ", player=" + player + ", goals=" + goals + "]";
	}
	
}
