package guiqsassi.gamescraper.Entity;

import guiqsassi.gamescraper.Entity.Enum.GameStore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data
public class GamePrice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    private GameStore gameStore;

    @ManyToOne
    @JoinColumn
    private Game game;
    private LocalDateTime date = LocalDateTime.now();
    private BigDecimal price;
    private String url;



}
