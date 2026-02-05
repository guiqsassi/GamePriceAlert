package guiqsassi.gamescraper.Service;

import guiqsassi.gamescraper.Dto.ItemDto;

import guiqsassi.gamescraper.Dto.SteamData;
import guiqsassi.gamescraper.Dto.SteamSearchDto;
import guiqsassi.gamescraper.Entity.Game
import guiqsassi.gamescraper.Entity.GameImage;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.FeignClient.SteamFeignClient;
import guiqsassi.gamescraper.Repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SteamFeignClient steamClient;

    public Game findByTitle(String title) {
        return gameRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Game not found"));
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public Game findOrSaveFromSteam(String title){
        try{
            return  findByTitle(title);
        }
        catch (Exception e){
            return saveFromSteam(title);
        }
    }

    public Game saveFromSteam(String title){
        SteamSearchDto data =steamClient.searchItems(title, "portuguese", "br");
        List<ItemDto> items= data.getItems();
        String bestMatchId = items.stream().filter( itemDto -> {
            return itemDto.getName().toLowerCase().contains(title.toLowerCase());
        } ).toList().getFirst().getId();

         SteamData gameData = steamClient.getAppDetails(bestMatchId, "portuguese", "br").get(bestMatchId).getData();
        Locale ptBR = new Locale("pt", "BR");

        // remove barras e vírgulas estranhas
        String raw = gameData.getRelease_date().getDate().replace("/", " ")
                .replace(",", "")
                .trim();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("d MMM yyyy", ptBR);

        List<GameImage> images = new ArrayList<>();
         Game g = new Game();
         g.setTitle(gameData.getName());
         g.setReleaseDate(LocalDate.parse(raw, formatter).atStartOfDay() );
         g.setDescription(gameData.getShort_description());

         gameData.getScreenshots().stream().forEach( screenshot ->{
             GameImage gi = new GameImage();
             gi.setGame(g);
             gi.setUrl(screenshot.getPath_full());
             images.add(gi);
         });

         return this.save(g);

    }



}
