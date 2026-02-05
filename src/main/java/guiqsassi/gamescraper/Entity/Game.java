package guiqsassi.gamescraper.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    @Lob
    private String description;
    private LocalDateTime releaseDate;

    @OneToMany(cascade = CascadeType.ALL)
    private List<GameImage> images;
    private LocalDateTime created = LocalDateTime.now();

    private String steamId;


}
