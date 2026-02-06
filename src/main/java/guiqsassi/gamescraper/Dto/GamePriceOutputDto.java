package guiqsassi.gamescraper.Dto;

import guiqsassi.gamescraper.Entity.Enum.GameStore;
import guiqsassi.gamescraper.Entity.Game;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor @NoArgsConstructor
public class GamePriceOutputDto {

    private String id;
    private GameStore gameStore;
    private GameOutputDto game;
    private LocalDateTime date;
    private BigDecimal price;

}
