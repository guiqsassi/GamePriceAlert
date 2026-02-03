package guiqsassi.gamescraper.Controller;

import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.Scraper.impl.NuuvemScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/scrap")
public class GameController {


    @Autowired
    private NuuvemScraper nuuvemScraper;


    @GetMapping("/{title}")
    private ResponseEntity<?> getGame(@PathVariable String title) {
        List<GamePrice> gp = nuuvemScraper.getGame(title);

        return ResponseEntity.ok(gp);
    }
}
