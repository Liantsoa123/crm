package site.easy.to.build.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "alert_rate")
public class AlertRate {
    @Id
    @Column(name = "alert_rate_id")
    private int alertRateId;

    @NotNull(message = "Rate is required")
    @Column(name = "rate")
    private Double rate;

    public AlertRate(int alertRateId, Double rate) {
        this.alertRateId = alertRateId;
        this.rate = rate;
    }

    public AlertRate() {
    }


    public int getAlertRateId() {
        return alertRateId;
    }

    public void setAlertRateId(int alertRateId) {
        this.alertRateId = alertRateId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
