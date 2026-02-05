package guiqsassi.gamescraper.Service;

import guiqsassi.gamescraper.Dto.SteamData;
import guiqsassi.gamescraper.Entity.Enum.GameStore;
import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.FeignClient.SteamFeignClient;
import guiqsassi.gamescraper.Scraper.impl.NuuvemScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class GamePriceService {

    @Autowired
    private GameService gameService;

    @Autowired
    private NuuvemScraper nuuvemScraper;


    @Autowired
    private SteamFeignClient steamClient;

    public GamePrice findBestPrice(String title){
        Game g = gameService.findOrSaveFromSteam(title);

        List<GamePrice> gp = nuuvemScraper.getGame(g.getTitle());

        SteamData data = steamClient.getAppDetails(g.getSteamId(), "portuguese", "br").get(g.getSteamId()).getData();
        GamePrice gpSteam = new GamePrice();
        gpSteam.setGameStore(GameStore.STEAM);
        gpSteam.setPrice(data.getPrice_overview().priceToBigDecimal());
        gpSteam.setGame(g);

        gp.add(gpSteam);
        GamePrice lowestGamePrice = gp.stream().min(Comparator.comparing(GamePrice::getPrice))
                .orElseThrow();

        return lowestGamePrice;


    }



}
