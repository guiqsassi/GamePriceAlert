package guiqsassi.gamescraper.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamSubData {

    private String id;
    private String name;
    private PriceDto price;
    private List<SteamData> apps;

}
