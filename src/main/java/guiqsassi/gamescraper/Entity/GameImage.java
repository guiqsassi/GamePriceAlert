package guiqsassi.gamescraper.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class GameImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String url;
    private LocalDateTime saved = LocalDateTime.now();
    @ManyToOne
    private Game game;
}
