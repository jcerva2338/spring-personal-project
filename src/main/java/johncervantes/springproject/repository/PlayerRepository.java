package johncervantes.springproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import johncervantes.springproject.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
	@Query("SELECT u FROM Player u where u.user.id=?1")
	public List<Player> findAllByUserId(int i);
}
