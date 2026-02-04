package guiqsassi.gamescraper.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Screenshot {

    private int id;
    private String path_thumbnail;
    private String path_full;
}
