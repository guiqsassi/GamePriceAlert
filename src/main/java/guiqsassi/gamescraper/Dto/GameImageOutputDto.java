package guiqsassi.gamescraper.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameImageOutputDto {
    private String id;
    private String url;
    private LocalDateTime saved;

}
