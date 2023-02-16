package johncervantes.springproject.dao;

import johncervantes.springproject.entity.User;

public interface UserDao {
	public User findByUserName(String userName);
	
	public void save(User user);
}
