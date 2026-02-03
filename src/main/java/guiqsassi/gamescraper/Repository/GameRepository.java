package guiqsassi.gamescraper.Repository;

import guiqsassi.gamescraper.Entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, String> {}
