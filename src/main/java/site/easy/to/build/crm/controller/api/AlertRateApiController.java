package site.easy.to.build.crm.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.AlertRate;
import site.easy.to.build.crm.service.alertRate.AlertRateService;

import java.util.List;

@RestController
@RequestMapping("/api/alert-rate")
public class AlertRateApiController {

    private final AlertRateService alertRateService;

    @Autowired
    public AlertRateApiController(AlertRateService alertRateService) {
        this.alertRateService = alertRateService;
    }

    // Obtenir un taux d'alerte par son ID
    @GetMapping("/{id}")
    public ResponseEntity<AlertRate> getAlertRateById(@PathVariable("id") int id) {
        AlertRate alertRate = alertRateService.findByAlertRateId(id);
        System.out.println("Taux d'alerte id : " + id);
        if (alertRate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(alertRate, HttpStatus.OK);
    }


    // Mettre à jour un taux d'alerte existant
    @PutMapping("/{id}")
    public ResponseEntity<AlertRate> updateAlertRate(@PathVariable("id") int id,
                                                     @RequestBody AlertRate alertRate) {
        System.out.println("Mise à jour du taux d'alerte id : " + id);
        AlertRate existingAlertRate = alertRateService.findByAlertRateId(id);
        if (existingAlertRate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        alertRate.setAlertRateId(id);
        AlertRate updatedAlertRate = alertRateService.save(alertRate);
        return new ResponseEntity<>(updatedAlertRate, HttpStatus.OK);
    }


    // Obtenir le taux d'alerte actuel (par défaut ou configuré)
    @GetMapping("/current")
    public ResponseEntity<Double> getCurrentAlertRate() {
        List<AlertRate> alertRates = alertRateService.findAll();
        Double currentRate = alertRates.isEmpty() ? 0.2 : alertRates.get(0).getRate();
        return new ResponseEntity<>(currentRate, HttpStatus.OK);
    }
}