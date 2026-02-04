package guiqsassi.gamescraper.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamAppDetails {

    private boolean success;
    private SteamData data;
}
