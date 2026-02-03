package guiqsassi.gamescraper.Scraper.core;
import guiqsassi.gamescraper.Entity.GamePrice;

import java.util.List;

public interface GameScraper {

    List<GamePrice> getGame(String title);
}
