package site.easy.to.build.crm.service.expense;

import site.easy.to.build.crm.entity.Expense;

import java.util.List;

public interface ExpenseService {
    public Expense findExpenseById(Long id);

    public List<Expense> findAll();

    public List<Expense> findByBudgetId(Long budgetId);

    public List<Expense> findByBudgetIdOrderByDateDesc(Long budgetId);

    public  Expense save(Expense expense);
}
