package guiqsassi.gamescraper.Scraper.impl;

import guiqsassi.gamescraper.Entity.Enum.GameStore;
import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.Scraper.core.AbstractScraper;
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
public class GreenManGamingScraper extends AbstractScraper {
    @Autowired
    private GameService gameService;

    @Override
    public List<GamePrice> getGame(String title) {
        WebDriver d = getDriver("https://www.greenmangaming.com/pt/search/?query=" + URLEncoder.encode(title, StandardCharsets.UTF_8) + "&platform=PC&drm=Steam");

        WebDriverWait wait = new WebDriverWait(d, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("ais-Hits-item")));
        List<GamePrice> games = new ArrayList<>();
        Integer lenght = 0;
        for (WebElement e : d.findElements(By.className("ais-Hits-item"))) {
            String display = e.getCssValue("display");

            if (!"inline-block".equals(display)) {
                break;
            }

            if (lenght >= 10) {
                break;
            }

            lenght++;
            WebElement link = e.findElement(By.tagName("a"));
            WebElement name = e.findElement(By.className("prod-name"));
            System.out.println(name.getText().split("-")[0].trim());
            WebElement priceE = e.findElement(By.className("current-price"));
            String price = priceE.getText().replace("R$", "").replace(",", ".");

            GamePrice gamePrice = new GamePrice();
            Optional<Game> game = gameService.findOrSaveFromSteam(name.getText().split("-")[0].trim());
            if (game.isEmpty()) {
                continue;
            }
            gamePrice.setGameStore(GameStore.GREENMANGAMING);
            gamePrice.setGame(game.get());
            gamePrice.setUrl(link.getAttribute("href"));
            try {
                gamePrice.setPrice(new BigDecimal(price));

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                continue;
            }

            games.add(gamePrice);

        }

        d.close();
        return games;
    }
}
