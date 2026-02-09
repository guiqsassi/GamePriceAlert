package guiqsassi.gamescraper.Scraper.impl;

import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.Scraper.core.AbstractScraper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GreenManGamingScaper extends AbstractScraper {
    @Override
    public List<GamePrice> getGame(String title) {
        return List.of();
    }
}
