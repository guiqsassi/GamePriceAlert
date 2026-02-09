package guiqsassi.gamescraper.Repository;

import guiqsassi.gamescraper.Entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, String> {
    Optional<Game> findGameByNormalizedTitle(String title);
    Optional<Game> findByTitle(String title);

}
