package guiqsassi.gamescraper.Service;

import guiqsassi.gamescraper.Dto.*;

import guiqsassi.gamescraper.Entity.Enum.GameStore;
import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GameImage;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.FeignClient.SteamFeignClient;
import guiqsassi.gamescraper.Repository.GamePriceRepository;
import guiqsassi.gamescraper.Repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SteamFeignClient steamClient;


    @Autowired
    private GamePriceRepository gamePriceRepository;



    public Game findByTitle(String title) {
        title = title.toLowerCase()
                .replaceAll("\\biii\\b", "3")
                .replaceAll("\\bii\\b", "2")
                .replaceAll("\\biv\\b", "4")
                .replaceAll("\\bv\\b", "5")
                .replaceAll("[^\\p{L}\\p{N} ]", "")
                .trim();

        Game g =  gameRepository.findGameByNormalizedTitle(title).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        return g;
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public Optional<Game> findOrSaveFromSteam(String title){
        try{
            return Optional.of(findByTitle(title));
        }
        catch (Exception e){
            return saveFromSteam(title);
        }
    }

    public Optional<Game> saveFromSteam(String title){
        SteamSearchDto data =steamClient.searchItems(title.toLowerCase()
                .replaceAll("\\biii\\b", "3")
                .replaceAll("\\bii\\b", "2")
                .replaceAll("\\biv\\b", "4")
                .replaceAll("\\bv\\b", "5")
                .replaceAll("[^\\p{L}\\p{N} ]", "")
                .trim(), "portuguese", "br");
        List<ItemDto> items= data.getItems();
        try {
            String bestMatchId = items.getFirst().getId();
            if(items.getFirst().getName().contains("Demo")){
                return Optional.ofNullable(null);
            }
            else if(items.getFirst().getType().equals("sub")){
                SteamSubData gameData= steamClient.getSubDetails(bestMatchId, "portuguese", "br").get(bestMatchId).getData();
                return Optional.of(saveSub(gameData));
            }
            else if(items.getFirst().getType().equals("app")){
                SteamData gameData= steamClient.getAppDetails(bestMatchId, "portuguese", "br").get(bestMatchId).getData();
                return Optional.of(saveApp(gameData));

            }
            else{
                return Optional.ofNullable(null);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.ofNullable(null);
        }
    }


    public Game saveSub(SteamSubData gameData){
        List<GameImage> images = new ArrayList<>();
        Game g = new Game();
        g.setTitle(gameData.getName().replaceAll("[^\\p{L}\\p{N} ]", ""));
        SteamData mainGame= getMainGameFromSub(gameData.getApps());


        mainGame.getScreenshots().forEach(screenshot -> {
            GameImage gi = new GameImage();
            gi.setGame(g);
            gi.setUrl(screenshot.getPath_full());
            images.add(gi);
        });

            g.setCoverUrl("https://cdn.akamai.steamstatic.com/steam/apps/"+ mainGame.getSteam_appid() +"/library_600x900_2x.jpg");

        g.setSteamId(gameData.getId());
        g.setImages(images);
        GamePrice gp = new GamePrice();

        gp.setGame(g);
        g.setNormalizedTitle(gameData.getName().replaceAll("[^\\p{L}\\p{N} ]", "").toLowerCase()
                .replaceAll("\\biii\\b", "3")
                .replaceAll("\\bii\\b", "2")
                .replaceAll("\\biv\\b", "4")
                .replaceAll("\\bv\\b", "5")
                .replaceAll("[^\\p{L}\\p{N} ]", "")
                .trim());


        gp.setUrl("https://store.steampowered.com/sub/" + g.getSteamId());
        gp.setGameStore(GameStore.STEAM);
        if(gameData.getPrice() != null){
            gp.setPrice(gameData.getPrice().priceToBigDecimal());
        }else{
            gp.setPrice(BigDecimal.ZERO);
        }
        this.save(g);
        gamePriceRepository.save(gp);
        return g;
    }

    public SteamData getMainGameFromSub(List<SteamData> apps){
        AtomicReference<SteamData> mainGame = null;

        for (int i = 0; i < apps.size(); i++) {
            SteamData app = apps.get(i);

            SteamData steamData = steamClient
                    .getAppDetails(app.getId(), "portuguese", "br")
                    .get(app.getId())
                    .getData();

            if ("game".equals(steamData.getType())) {
                return steamData;
            }
        }
        return null;

    }

    public Game saveApp(SteamData gameData){
        if(gameData.getType().equals("dlc")){
            return null;
        }

        Locale ptBR = new Locale("pt", "BR");

        String raw = gameData.getRelease_date().getDate().replace("/", " ")
                .replace(",", "")
                .trim();

        List<GameImage> images = new ArrayList<>();
        Game g = new Game();
        g.setTitle(gameData.getName().replaceAll("[^\\p{L}\\p{N} ]", ""));
        DateTimeFormatter formatter =
           DateTimeFormatter.ofPattern("d MMM yyyy", ptBR);
        g.setReleaseDate(LocalDate.parse(raw, formatter).atStartOfDay());

        g.setDescription(gameData.getShort_description());

        gameData.getScreenshots().forEach(screenshot -> {
            GameImage gi = new GameImage();
            gi.setGame(g);
            gi.setUrl(screenshot.getPath_full());
            images.add(gi);
        });
        g.setSteamId(gameData.getSteam_appid().toString());
        g.setCoverUrl("https://cdn.akamai.steamstatic.com/steam/apps/"+ gameData.getSteam_appid() +"/library_600x900_2x.jpg");

        g.setImages(images);
        GamePrice gp = new GamePrice();

        gp.setGame(g);
        g.setNormalizedTitle(gameData.getName().replaceAll("[^\\p{L}\\p{N} ]", "").toLowerCase()
                .replaceAll("\\biii\\b", "3")
                .replaceAll("\\bii\\b", "2")
                .replaceAll("\\biv\\b", "4")
                .replaceAll("\\bv\\b", "5")
                .replaceAll("[^\\p{L}\\p{N} ]", "")
                .trim());

        gp.setUrl("https://store.steampowered.com/"+ gameData.getType() + "/" + g.getSteamId());
        gp.setGameStore(GameStore.STEAM);
        if(gameData.getPrice_overview() != null){
            gp.setPrice(gameData.getPrice_overview().priceToBigDecimal());
        }else{
            gp.setPrice(BigDecimal.ZERO);
        }
        this.save(g);
        gamePriceRepository.save(gp);

        return g;
    }


}
