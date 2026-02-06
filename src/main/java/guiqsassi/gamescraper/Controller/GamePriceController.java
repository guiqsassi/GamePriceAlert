package guiqsassi.gamescraper.Controller;

import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.Mapper.GamePriceMapper;
import guiqsassi.gamescraper.Service.GamePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/scrap")
public class GamePriceController {


    @Autowired
    private GamePriceService gpService;

    @Autowired
    private GamePriceMapper gamePriceMapper;


    @GetMapping("/{title}")
    private ResponseEntity<?> getGame(@PathVariable String title) {
        GamePrice gp = gpService.findBestPrice(title);

        return ResponseEntity.ok(gamePriceMapper.toDto(gp));
    }

    @GetMapping("/stores/{title}")
    private ResponseEntity<?> getAllPrices(@PathVariable String title){
        List<GamePrice> gp = gpService.findBestPerStore(title);

        return ResponseEntity.ok(gp.stream().map(gamePriceMapper::toDto).toList());
    }




}
