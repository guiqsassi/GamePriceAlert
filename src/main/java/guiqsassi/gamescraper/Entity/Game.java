package guiqsassi.gamescraper.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true)
    private String title;
    @Lob
    private String description;
    private LocalDateTime releaseDate;

    @OneToMany(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    private List<GameImage> images;
    private LocalDateTime created = LocalDateTime.now();

    private String steamId;


}
