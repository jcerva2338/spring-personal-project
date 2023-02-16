package johncervantes.springproject.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import johncervantes.springproject.entity.User;
import johncervantes.springproject.user.CrmUser;

public interface UserService extends UserDetailsService {
	
	public User findByUserName(String userName);
	
	public void save(CrmUser crmUser);
}
