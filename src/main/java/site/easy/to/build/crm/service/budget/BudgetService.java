package site.easy.to.build.crm.service.budget;


import site.easy.to.build.crm.entity.Budget;

import java.time.LocalDate;
import java.util.List;

public interface BudgetService {
    public Budget findByBudgetId(int budgetId);

    public List<Budget> findAll();

    public List<Budget> findBudgetsByCustomer_CustomerId(int customerCustomerId);

    public List<Budget> findBudgetsByCustomer_CustomerIdAndStartDateAndEndDate(Integer customerCustomerId, LocalDate startDate,  LocalDate endDate);

}
