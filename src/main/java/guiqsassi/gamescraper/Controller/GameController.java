package guiqsassi.gamescraper.Controller;

import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.FeignClient.SteamFeignClient;
import guiqsassi.gamescraper.Scraper.impl.NuuvemScraper;
import guiqsassi.gamescraper.Service.GamePriceService;
import guiqsassi.gamescraper.Service.GameService;
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

    @Autowired
    private SteamFeignClient  steamFeignClient;

    @Autowired
    private GameService gameService;

    @Autowired
    private GamePriceService gpService;

    @GetMapping("/{title}")
    private ResponseEntity<?> getGame(@PathVariable String title) {
        GamePrice gp = gpService.findBestPrice(title);
        gp.getGame().getImages().clear();
        return ResponseEntity.ok(gp);
    }

    @GetMapping("/steam/{title}")
    private ResponseEntity<?> getSteam(@PathVariable String title) {
        System.out.println(title);
        title = "Persona+3+Reload";
        return ResponseEntity.ok(steamFeignClient.searchItems(title, "portuguese", "br"));
    }
    @GetMapping("/steamId/{id}")
    private ResponseEntity<?> getSteamGame(@PathVariable String id) {
        return ResponseEntity.ok(steamFeignClient.getAppDetails(id, "portuguese", "br"));
    }
}
