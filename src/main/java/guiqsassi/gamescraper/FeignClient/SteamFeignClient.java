package guiqsassi.gamescraper.FeignClient;

import guiqsassi.gamescraper.Dto.SteamAppDetails;
import guiqsassi.gamescraper.Dto.SteamSearchDto;
import guiqsassi.gamescraper.Dto.SteamSubDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(url = "https://store.steampowered.com/api", name = "steam")
public interface SteamFeignClient {

    @GetMapping("/storesearch")
    SteamSearchDto searchItems(
            @RequestParam("term") String title,
            @RequestParam("l") String language,
            @RequestParam("cc") String country
    );


    @GetMapping("/appdetails")
    Map<String, SteamAppDetails> getAppDetails(
            @RequestParam("appids") String id,
            @RequestParam("l") String language,
            @RequestParam("cc") String country);



    @GetMapping("/packagedetails")
    Map<String, SteamSubDetails> getSubDetails(
            @RequestParam("packageids") String id,
            @RequestParam("l") String language,
            @RequestParam("cc") String country);




}
