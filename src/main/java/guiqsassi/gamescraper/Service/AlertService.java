package guiqsassi.gamescraper.Service;

import guiqsassi.gamescraper.Entity.Alert;
import guiqsassi.gamescraper.Entity.GamePrice;
import guiqsassi.gamescraper.Repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private GamePriceService gamePriceService;

    public Alert create(Alert alert){
        return alertRepository.save(alert);
    }
    public Alert findAlertById(String id){
        return alertRepository.findById(id).get();
    }
    public void deleteAlertById(String id){
        alertRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void alert(){
        List<Alert> alerts = alertRepository.findAll();

        alerts.stream().forEach(alert->{
            GamePrice gp = gamePriceService.findBestPrice(alert.getGame().getNormalizedTitle());
            GamePrice bestHistory = gamePriceService.findBestPriceEverByGame(alert.getGame().getNormalizedTitle());
            int comparable = gp.getPrice().compareTo( bestHistory.getPrice());
            if(comparable >= 0 ){
                System.out.println("Está no melhor preço");
            }
            System.out.println(gp.getPrice().toString());
        });

    }

}
