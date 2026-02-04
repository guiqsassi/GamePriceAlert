package guiqsassi.gamescraper.Scraper.core;

import guiqsassi.gamescraper.Exception.DriverException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public abstract class AbstractScraper implements GameScraper {

    public WebDriver getDriver (String url){

        try{
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless", "--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--start-maximized");

            WebDriver driver = new ChromeDriver(options);

            driver.get(url);


            return driver;

        }
        catch (Exception e){
            throw new DriverException(e.getMessage());
        }
    }



}
