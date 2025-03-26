package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.dto.BudgetStatusDTO;
import site.easy.to.build.crm.entity.AlertRate;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.service.alertRate.AlertRateService;
import site.easy.to.build.crm.service.customer.CustomerServiceImpl;
import site.easy.to.build.crm.service.expense.ExpenseService;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetStatusService {

    private final BudgetService budgetService;
    private final ExpenseService expenseService;
    private final AlertRateService alertRateService;
    private final CustomerServiceImpl customerServiceImpl;

    @Autowired
    public BudgetStatusService(BudgetService budgetService,
                               ExpenseService expenseService,
                               AlertRateService alertRateService, CustomerServiceImpl customerServiceImpl) {
        this.budgetService = budgetService;
        this.expenseService = expenseService;
        this.alertRateService = alertRateService;
        this.customerServiceImpl = customerServiceImpl;
    }

    public List<BudgetStatusDTO> getBudgetStatusForCustomer(int customerId) {

        List<AlertRate> alertRates = alertRateService.findAll();
        Double alertRate = alertRates.isEmpty() ? 0.2 : alertRates.get(0).getRate();


        List<Budget> budgets = budgetService.findBudgetsByCustomer_CustomerId(customerId);


        List<Expense> expenses = budgets.stream()
                .map(budget -> expenseService.findByBudgetId(budget.getBudgetId()))
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

        /*BigDecimal totalRemainingAmount = budgetStatusDTOS.stream()
                .map(BudgetStatusDTO::getRemainingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);*/

        double totalexpenseTicket = customerServiceImpl.getTotalExpensesTicketByCustomerId(customerId);
        double totalexpenseLead = customerServiceImpl.getTotalExpensesLeadByCustomerId(customerId);
        System.out.println("totalexpenseLead = " + totalexpenseLead);
        System.out.println("totalexpenseTicket = " + totalexpenseTicket);



        BigDecimal totalBudgetAmount = budgetStatusDTOS.stream()
                .map(BudgetStatusDTO::getBudget)
                .map(Budget::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRemainingAmount = totalBudgetAmount.subtract(BigDecimal.valueOf(totalexpenseLead + totalexpenseTicket));
        BigDecimal totalExpense = BigDecimal.valueOf(totalexpenseLead + totalexpenseTicket);

        System.out.println("Total budget initial = " + totalBudgetAmount);
        System.out.println("Total Depense = " + totalExpense);
        System.out.println("Total Restant  = " + totalRemainingAmount);
        System.out.println("Alert Rate = " + alertRate * 100 + "%");
        System.out.println("Depense %= " + totalExpense.divide(totalBudgetAmount, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)) + "%");
        System.out.println("Restant %= " + totalRemainingAmount.divide(totalBudgetAmount, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)) + "%");

       if (totalExpense.compareTo(totalBudgetAmount.multiply(BigDecimal.valueOf(alertRate))) >= 0) {
            return "ALERTE: Le total des budgets a atteint un niveau critique. Il reste " + totalRemainingAmount + " €  (limite =  " + (alertRate * 100) + "% du budget total de " + totalBudgetAmount + " €).";
        } else {
            return "Total des budgets : Il reste " + totalRemainingAmount + " € (limite = " + (alertRate * 100) + "% du budget total de " + totalBudgetAmount + " €).";
        }

    }
}