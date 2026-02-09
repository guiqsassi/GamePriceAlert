package guiqsassi.gamescraper.Service;

import guiqsassi.gamescraper.Dto.ItemDto;

import guiqsassi.gamescraper.Dto.SteamData;
import guiqsassi.gamescraper.Dto.SteamSearchDto;
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

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SteamFeignClient steamClient;


    @Autowired
    private GamePriceRepository gamePriceRepository;



    public Game findByTitle(String title) {
        return gameRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Game not found"));
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
        SteamSearchDto data =steamClient.searchItems(title, "portuguese", "br");
        List<ItemDto> items= data.getItems();
        try {
            String bestMatchId = items.stream().filter(itemDto -> {
                return itemDto.getName().toLowerCase().contains(title.toLowerCase());
            }).toList().getFirst().getId();

            SteamData gameData = steamClient.getAppDetails(bestMatchId, "portuguese", "br").get(bestMatchId).getData();
            if(!gameData.getType().equals("game")){
                return Optional.ofNullable(null);

            }

            Locale ptBR = new Locale("pt", "BR");

            String raw = gameData.getRelease_date().getDate().replace("/", " ")
                    .replace(",", "")
                    .trim();

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("d MMM yyyy", ptBR);

            List<GameImage> images = new ArrayList<>();
            Game g = new Game();
            g.setTitle(gameData.getName());
            g.setReleaseDate(LocalDate.parse(raw, formatter).atStartOfDay());
            g.setDescription(gameData.getShort_description());



            gameData.getScreenshots().forEach(screenshot -> {
                GameImage gi = new GameImage();
                gi.setGame(g);
                gi.setUrl(screenshot.getPath_full());
                images.add(gi);
            });
            g.setSteamId(bestMatchId);
            g.setImages(images);
            GamePrice gp = new GamePrice();

            gp.setGame(g);
            gp.setUrl("https://store.steampowered.com/app/" + g.getSteamId());
            gp.setGameStore(GameStore.STEAM);
            if(gameData.getPrice_overview() != null){
                gp.setPrice(gameData.getPrice_overview().priceToBigDecimal());
            }else{
                gp.setPrice(BigDecimal.ZERO);
            }
            this.save(g);
            gamePriceRepository.save(gp);
            return Optional.of(g);
        } catch (Exception e) {
            return Optional.ofNullable(null);
        }
    }



}
