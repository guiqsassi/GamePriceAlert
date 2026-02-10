package guiqsassi.gamescraper.Repository;

import guiqsassi.gamescraper.Entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, String> {
}
