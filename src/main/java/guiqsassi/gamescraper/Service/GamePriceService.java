package guiqsassi.gamescraper.Service;

import guiqsassi.gamescraper.Dto.SteamData;
import guiqsassi.gamescraper.Entity.Enum.GameStore;
import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.FeignClient.SteamFeignClient;
import guiqsassi.gamescraper.Repository.GamePriceRepository;
import guiqsassi.gamescraper.Scraper.impl.NuuvemScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
public class GamePriceService {

    @Autowired
    private GameService gameService;

    @Autowired
    private NuuvemScraper nuuvemScraper;

    @Autowired
    private GamePriceRepository gamePriceRepository;

    @Autowired
    private SteamFeignClient steamClient;

    public GamePrice findBestPrice(String title){
        Game g = gameService.findOrSaveFromSteam(title);

        List<GamePrice> gp = new java.util.ArrayList<>(nuuvemScraper.getGame(g.getTitle()).stream().filter(gamePrice ->
                gamePrice.getGame().getTitle() != null && gamePrice.getGame().getTitle().contains(title)).toList());

        SteamData data = steamClient.getAppDetails(g.getSteamId(), "portuguese", "br").get(g.getSteamId()).getData();
        GamePrice gpSteam = new GamePrice();
        gpSteam.setGameStore(GameStore.STEAM);
        gpSteam.setPrice(data.getPrice_overview().priceToBigDecimal());
        gpSteam.setGame(g);

        gp.add(gpSteam);
        GamePrice lowestGamePrice = gp.stream().min(Comparator.comparing(GamePrice::getPrice))
                .orElseThrow();

        gamePriceRepository.saveAll(gp);
        return lowestGamePrice;


    }

    public List<GamePrice> findBestPerStore(String title){
        Game game = gameService.findByTitle(title);

        return gamePriceRepository.findLatestPricePerStore(game);

    }


}
