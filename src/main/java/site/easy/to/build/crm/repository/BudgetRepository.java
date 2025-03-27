package site.easy.to.build.crm.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Budget;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    public Budget findById(int budgetId);

    public List<Budget> findAll();

    List<Budget> findBudgetsByCustomer_CustomerId(int customerCustomerId);

    List<Budget> findBudgetsByCustomer_CustomerIdAndStartDateAndEndDate(Integer customerCustomerId, LocalDate startDate,  LocalDate endDate);

}
