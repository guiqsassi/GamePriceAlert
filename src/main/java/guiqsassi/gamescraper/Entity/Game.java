package guiqsassi.gamescraper.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;
    private LocalDateTime releaseDate;

    @OneToMany(cascade = CascadeType.ALL)
    private List<GameImage> images;
    private LocalDateTime created = LocalDateTime.now();


}
