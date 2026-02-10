package guiqsassi.gamescraper.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    private Game game;
    private String email;
    private LocalDateTime created = LocalDateTime.now();

}
