package guiqsassi.gamescraper.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseDate {

    private boolean coming_soon;
    private String date;
}
