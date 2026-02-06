package guiqsassi.gamescraper.Dto;

import guiqsassi.gamescraper.Entity.GameImage;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GameOutputDto {
    private String id;
    private String title;
    private String description;
    private LocalDateTime releaseDate;
    private List<GameImageOutputDto> images;
    private LocalDateTime created;
    private String steamId;

}
