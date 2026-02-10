package guiqsassi.gamescraper.Controller;

import guiqsassi.gamescraper.Entity.Alert;
import guiqsassi.gamescraper.Service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alert")
public class AlertController {

    @Autowired
    private AlertService alertService;


    @PostMapping("")
    private ResponseEntity<?> createAlert(@RequestBody Alert alert){

        return ResponseEntity.ok(alertService.create(alert));
    }
}
