package site.easy.to.build.crm.service.alertRate;

import site.easy.to.build.crm.entity.AlertRate;

import java.util.List;

public interface AlertRateService {
    public AlertRate findByAlertRateId(int alertRateId);

    public List<AlertRate> findAll();

    public AlertRate save(AlertRate alertRate);

    public void delete(AlertRate alertRate);

}
