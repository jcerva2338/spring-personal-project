package johncervantes.springproject.user;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CrmUser {
	
	@Valid
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	private String username;
	
	@Valid
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	private String password;
	
	@Valid
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	private String email;
	
	@Valid
	@NotNull(message = "is required")
	@Size(min = 1, message = "is required")
	private String team;
	
	private int currency;
	
	public CrmUser() {}

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

}
