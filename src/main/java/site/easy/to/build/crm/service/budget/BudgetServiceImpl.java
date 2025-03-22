package site.easy.to.build.crm.service.budget;

import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.repository.BudgetRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Budget findById(int budgetId) {
        return budgetRepository.findById(budgetId);
    }

    @Override
    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    @Override
    public List<Budget> findBudgetsByCustomer_CustomerId(int customerCustomerId) {
        return budgetRepository.findBudgetsByCustomer_CustomerId(customerCustomerId);
    }

    @Override
    public List<Budget> findBudgetsByCustomer_CustomerIdAndStartDateAndEndDate(Integer customerCustomerId, LocalDate startDate, LocalDate endDate) {
        return budgetRepository.findBudgetsByCustomer_CustomerIdAndStartDateAndEndDate(customerCustomerId, startDate, endDate);
    }

    @Override
    public void saveBudget(Budget budget) {
        budgetRepository.save(budget);
    }
}
