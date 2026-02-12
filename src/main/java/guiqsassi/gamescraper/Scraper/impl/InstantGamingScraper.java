package guiqsassi.gamescraper.Scraper.impl;

import guiqsassi.gamescraper.Entity.Enum.GameStore;
import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.Scraper.core.AbstractScraper;
import guiqsassi.gamescraper.Scraper.core.GameScraper;
import guiqsassi.gamescraper.Service.GameService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstantGamingScraper extends AbstractScraper {

    @Autowired
    private GameService gameService;

    @Override
    public List<GamePrice> getGame(String title) {
        WebDriver d = getDriver("https://www.instant-gaming.com/en/search/?query="+URLEncoder.encode(title, StandardCharsets.UTF_8) +"&platform%5B%5D=1&type%5B%5D=Steam&product_types%5B%5D=preorder&product_types%5B%5D=game");

        WebDriverWait wait = new WebDriverWait(d, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("a")));
        List<GamePrice> games = new ArrayList<>();
        Integer lenght = 0;
        for (WebElement e : d.findElements(By.className("item"))) {

            if(lenght >= 10){
                break;
            }

            lenght++;
            try{

            WebElement link = e.findElement(By.tagName("a"));
            WebElement priceE = e.findElement(By.className("price"));
            String price = priceE.getText().replace("R$ ", "").replace(",", ".");

            GamePrice gamePrice = new GamePrice();
            Optional<Game> game = gameService.findOrSaveFromSteam(link.getAttribute("title").split("-")[0].trim());
            if(game.isEmpty()){
                continue;
            }
            gamePrice.setGameStore(GameStore.INSTANTGAMING);
            gamePrice.setGame(game.get());
            gamePrice.setUrl(link.getAttribute("href"));
            gamePrice.setPrice(new BigDecimal(price));
            games.add(gamePrice);

            } catch (Exception ex) {
                continue;
            }


        }

        d.close();
        return games;
    }


}
