package johncervantes.springproject.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

@Entity
@Table(name = "user")
public class User {
	@Valid
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Valid
	@Column(name = "username")
	private String username;
	@Valid
	@Column(name = "password")
	private String password;
	@Valid
	@Column(name = "email")
	private String email;
	@Valid
	@Column(name = "team")
	private String team;
	@Valid
	@Column(name = "currency")
	private int currency;
	
	@OneToMany(fetch=FetchType.LAZY,
			   mappedBy= "user",
			   cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<Player> players;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", 
	joinColumns = @JoinColumn(name = "user_id"), 
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;
	
	public User() {}

	public User(String username, String password, String email, int currency, String team) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.currency = currency;
		this.team = team;
	}

	public User(String username, String password, String email, String team, int currency, List<Player> players,
			Collection<Role> roles) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.team = team;
		this.currency = currency;
		this.players = players;
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public void addPlayer(Player player) {
		if (this.players == null) {
			this.players = new ArrayList<>();
		}
		
		this.players.add(player);
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", team="
				+ team + ", currency=" + currency + ", players=" + players + ", roles=" + roles + "]";
	}
	
	
}
