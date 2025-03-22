package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.dto.BudgetStatusDTO;
import site.easy.to.build.crm.entity.AlertRate;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.service.alertRate.AlertRateService;
import site.easy.to.build.crm.service.expense.ExpenseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetStatusService {

    private final BudgetService budgetService;
    private final ExpenseService expenseService;
    private final AlertRateService alertRateService;

    @Autowired
    public BudgetStatusService(BudgetService budgetService,
                               ExpenseService expenseService,
                               AlertRateService alertRateService) {
        this.budgetService = budgetService;
        this.expenseService = expenseService;
        this.alertRateService = alertRateService;
    }

    public List<BudgetStatusDTO> getBudgetStatusForCustomer(int customerId) {

        List<AlertRate> alertRates = alertRateService.findAll();
        Double alertRate = alertRates.isEmpty() ? 0.2 : alertRates.get(0).getRate();


        List<Budget> budgets = budgetService.findBudgetsByCustomer_CustomerId(customerId);


        List<Expense> expenses = budgets.stream()
                .map(budget -> expenseService.findByBudgetId(budget.getId()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return budgets.stream()
                .map(budget -> new BudgetStatusDTO(budget, expenses, alertRate))
                .collect(Collectors.toList());
    }

    public String alertMessage(int customerId){
        List<AlertRate> alertRates = alertRateService.findAll();
        Double alertRate = alertRates.isEmpty() ? 0.2 : alertRates.get(0).getRate();
        List<BudgetStatusDTO> budgetStatusDTOS = getBudgetStatusForCustomer(customerId);

        BigDecimal totalRemainingAmount = budgetStatusDTOS.stream()
                .map(BudgetStatusDTO::getRemainingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalRemainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            return "ALERTE: Le total des budgets a atteint un niveau critique. Reste: " + totalRemainingAmount + " €";
        } else {
            return "Total des budgets : Reste " + totalRemainingAmount + " €";
        }

    }
}