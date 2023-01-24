package johncervantes.springproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import johncervantes.springproject.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("SELECT user FROM User user WHERE user.email=?1")
	public User findByEmail(String email);
}
