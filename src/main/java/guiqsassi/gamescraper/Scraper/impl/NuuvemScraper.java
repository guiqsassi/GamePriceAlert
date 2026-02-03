package guiqsassi.gamescraper.Scraper.impl;

import guiqsassi.gamescraper.Entity.Enum.GameStore;
import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.Scraper.core.AbstractScraper;
import guiqsassi.gamescraper.Scraper.core.GameScraper;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class NuuvemScraper extends AbstractScraper {

    private static final String URL = "https://www.nuuvem.com/br-pt/catalog/drm/steam/os/windows/platforms/pc/types/games/sort/price/sort-mode/asc/search/";

    @Override
    public List<GamePrice> getGame(String title) {
        WebDriver driver = getDriver(URL + URLEncoder.encode(title, StandardCharsets.UTF_8));
        List<GamePrice> games = new ArrayList<>();
        for (WebElement e : driver.findElements(By.className("grid-col-6"))) {
            try {
                WebElement linkEl = e.findElement(By.cssSelector("a[href]"));
                String href = linkEl.getAttribute("href");
                String titleE = linkEl.getAttribute("title");

                if (titleE != null && !titleE.isBlank()) {

                    WebElement priceE = linkEl.findElement(By.className("product-price--val"));
                    StringBuilder price = new StringBuilder();


                    List<WebElement> span = priceE.findElements(By.tagName("span"));

                    for(WebElement s : span) {
                        price.append(s.getText());
                    }
                    price = new StringBuilder(price.toString().replaceAll("\\s+", " "));
                    price = new StringBuilder(price.toString().replace(",", "."));

                    Game  game = new Game();
                    game.setTitle(titleE);

                    GamePrice g = new GamePrice();
                    g.setGameStore(GameStore.NUUVEM);
                    g.setDate(LocalDateTime.now());
                    g.setPrice(new BigDecimal(price.toString()));
                    g.setGame(game);

                    games.add(g);
                }
            } catch (StaleElementReferenceException ignored) {
            }

            }
        return games;

    }
}
