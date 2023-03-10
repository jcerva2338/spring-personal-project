package johncervantes.springproject.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import johncervantes.springproject.entity.Player;
import johncervantes.springproject.entity.Role;
import johncervantes.springproject.entity.User;

public class CustomUserDetails implements UserDetails {
	private User user;
	
	public CustomUserDetails(User  user) {
		this.user = user;
	}
	
	public CustomUserDetails(String username, String password, Collection<Role> authorities) {
		User newUser = new User();
		
		newUser.setUsername(username);
		newUser.setPassword(password);
		newUser.setRoles(authorities);
		this.user = newUser;
		
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	public String getTeam() {
		return user.getTeam();
	}
	
	public List<Player> getPlayers() {
		return user.getPlayers();
	}
	
	public int getId() {
		return user.getId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
