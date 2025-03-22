package site.easy.to.build.crm.service.alertRate;

import site.easy.to.build.crm.entity.AlertRate;
import site.easy.to.build.crm.repository.AlertRateRepository;

import java.util.List;

public class AlertRateServiceImpl  implements AlertRateService {
    private final AlertRateRepository alertRateRepository;

    public AlertRateServiceImpl(AlertRateRepository alertRateRepository) {
        this.alertRateRepository = alertRateRepository;
    }

    @Override
    public AlertRate findByAlertRateId(int alertRateId) {
        return alertRateRepository.findAlertRateBy(alertRateId);
    }

    @Override
    public List<AlertRate> findAll() {
        return alertRateRepository.findAll();
    }

    @Override
    public AlertRate save(AlertRate alertRate) {
        return alertRateRepository.save(alertRate);
    }

    @Override
    public void delete(AlertRate alertRate) {
        alertRateRepository.delete(alertRate);
    }
}
