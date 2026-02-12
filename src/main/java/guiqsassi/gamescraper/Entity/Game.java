package guiqsassi.gamescraper.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(of = "normalizedTitle")
@Entity
@AllArgsConstructor @NoArgsConstructor @Data
public class Game implements Serializable {

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
    private String normalizedTitle;

    private String steamId;
    private String coverUrl;

}
