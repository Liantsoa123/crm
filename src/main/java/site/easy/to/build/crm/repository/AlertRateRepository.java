package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.AlertRate;

import java.util.List;

@Repository
public interface AlertRateRepository extends JpaRepository<AlertRate, Integer> {
    public AlertRate findByAlertRateId(int alertRateId);
    public List<AlertRate> findAll();
    public void deleteByAlertRateId(int alertRateId);

}
