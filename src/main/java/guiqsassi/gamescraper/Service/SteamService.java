package guiqsassi.gamescraper.Service;

import guiqsassi.gamescraper.Dto.ItemDto;
import guiqsassi.gamescraper.Dto.SteamSearchDto;
import guiqsassi.gamescraper.Entity.Enum.GameStore;
import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.FeignClient.SteamFeignClient;
import guiqsassi.gamescraper.Repository.GamePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SteamService {

    @Autowired
    private SteamFeignClient steamFeignClient;

    @Autowired
    private GameService gameService;


    public List<GamePrice> searchGameOnSteam(String title){
        List<ItemDto> dto = steamFeignClient.searchItems(title, "portuguese", "br").getItems();

        List<GamePrice> gpList = new ArrayList<>();
        for (ItemDto item : dto){
            GamePrice gp = new GamePrice();
            Optional<Game> game = gameService.findOrSaveFromSteam(item.getName());
            if(game.isEmpty()){
                continue;
            }
            gp.setGame(game.get());
            gp.setUrl("https://store.steampowered.com/app/" + game.get().getSteamId());
            gp.setGameStore(GameStore.STEAM);
            if(item.getPrice() != null){
                gp.setPrice(item.getPrice().priceToBigDecimal());
            }else{
                gp.setPrice(BigDecimal.ZERO);
            }

            gpList.add(gp);
        }

        return gpList;

    }


}
