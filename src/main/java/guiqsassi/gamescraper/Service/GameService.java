package guiqsassi.gamescraper.Service;

import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;


    public Game findByTitle(String title) {
        return gameRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Game not found"));
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }
}
