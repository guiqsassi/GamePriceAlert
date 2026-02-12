package guiqsassi.gamescraper.Scraper.core;

import guiqsassi.gamescraper.Exception.DriverException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;



public abstract class AbstractScraper implements GameScraper {

    public WebDriver getDriver (String url){

        try{
            WebDriverManager.chromedriver()
                    .clearDriverCache()
                    .setup();
            ChromeOptions options = new ChromeOptions();

            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");

            WebDriver driver = new ChromeDriver(options);

            driver.get(url);


            return driver;

        }
        catch (Exception e){
            throw new DriverException(e.getMessage());
        }
    }



}
