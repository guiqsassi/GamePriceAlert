package guiqsassi.gamescraper.Service;

import guiqsassi.gamescraper.Dto.SteamAppDetails;
import guiqsassi.gamescraper.Dto.SteamData;
import guiqsassi.gamescraper.Entity.Enum.GameStore;
import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.FeignClient.SteamFeignClient;
import guiqsassi.gamescraper.Repository.GamePriceRepository;
import guiqsassi.gamescraper.Scraper.impl.InstantGamingScraper;
import guiqsassi.gamescraper.Scraper.impl.NuuvemScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GamePriceService {

    @Autowired
    private GameService gameService;

    @Autowired
    private NuuvemScraper nuuvemScraper;

    @Autowired
    private SteamService steamService;

    @Autowired
    private GamePriceRepository gamePriceRepository;

    @Autowired
    private SteamFeignClient steamClient;
    @Autowired
    private InstantGamingScraper instantGamingScraper;



    public GamePrice findBestPrice(String title){
        Game g = gameService.findOrSaveFromSteam(title).orElseThrow();

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


    public List<GamePrice> searchGame(String title){


        List<GamePrice> gps = nuuvemScraper.getGame(title);


        gps.addAll(steamService.searchGameOnSteam(title));
        gps.addAll(instantGamingScraper.getGame(title));
        Map<Game, Optional<GamePrice>> menoresPorJogo =
                gps.stream()
                        .collect(Collectors.groupingBy(
                                GamePrice::getGame,
                                Collectors.minBy(Comparator.comparing(GamePrice::getPrice))
                        ));

        gamePriceRepository.saveAll(gps);
        return menoresPorJogo.values()
                .stream()
                .flatMap(Optional::stream)
                .toList();


    }

}
