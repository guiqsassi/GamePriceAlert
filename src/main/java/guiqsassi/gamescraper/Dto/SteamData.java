package guiqsassi.gamescraper.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamData {

    private String name;
    private Integer steam_appid;
    private String short_description;
    private List<Screenshot> screenshots;
    private ReleaseDate release_date;
    private PriceDto price_overview;

}
