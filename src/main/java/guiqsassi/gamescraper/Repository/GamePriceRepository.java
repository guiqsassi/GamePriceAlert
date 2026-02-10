package guiqsassi.gamescraper.Repository;

import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GamePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GamePriceRepository extends JpaRepository<GamePrice, String > {

    @Query("""
    SELECT gp
    FROM GamePrice gp
    WHERE gp.game = :game
      AND gp.date = (
        SELECT MAX(gp2.date)
        FROM GamePrice gp2
        WHERE gp2.gameStore = gp.gameStore
          AND gp2.game = :game
    )
    """)
    List<GamePrice> findLatestPricePerStore(Game game );
    Optional<GamePrice> findTopByGameOrderByPriceAsc(Game game);
}
