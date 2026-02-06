package guiqsassi.gamescraper.Scraper.impl;

import guiqsassi.gamescraper.Entity.Enum.GameStore;
import guiqsassi.gamescraper.Entity.Game;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.Scraper.core.AbstractScraper;
import guiqsassi.gamescraper.Service.GameService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class NuuvemScraper extends AbstractScraper {

    private static final String URL = "https://www.nuuvem.com/br-pt/catalog/drm/steam/os/windows/platforms/pc/types/games/sort/price/sort-mode/asc/search/";

    private final GameService gameService;

    @Override
    public List<GamePrice> getGame(String title) {
        WebDriver driver = getDriver(URL + URLEncoder.encode(title, StandardCharsets.UTF_8));


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("a")));

        List<GamePrice> games = new ArrayList<>();
        for (WebElement e : driver.findElements(By.className("grid-col-6"))) {
            try {
                WebElement linkEl = e.findElement(By.cssSelector("a[href]"));
                String href = linkEl.getAttribute("href");
                String titleE = linkEl.getAttribute("title");

                if (titleE != null && !titleE.isBlank()) {

                    WebElement priceDiv = driver.findElement(By.cssSelector(".product-price"));

                    String dataPrice = priceDiv.getAttribute("data-price");
                    dataPrice = dataPrice.replace("&quot;", "\"");

                    Pattern p = Pattern.compile("\"v\":(\\d+)");
                    Matcher m = p.matcher(dataPrice);
                    if (!m.find()) {
                        throw new RuntimeException("Preço não encontrado em: " + dataPrice);
                    }
                    BigDecimal price = new BigDecimal(m.group(1))
                                .divide(new BigDecimal("100"));



                    Game  game = gameService.findOrSaveFromSteam(titleE );

                    GamePrice g = new GamePrice();
                    g.setGameStore(GameStore.NUUVEM);
                    g.setDate(LocalDateTime.now());
                    g.setPrice(price);
                    g.setGame(game);

                    games.add(g);
                }
            } catch (StaleElementReferenceException ignored) {
            }

            }
        driver.close();
        return games;

    }



}

