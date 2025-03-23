package site.easy.to.build.crm.service.expense;

import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.repository.ExpenseRepository;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense findExpenseById(Long id) {
        return expenseRepository.findByExpenseId(id);
    }

    @Override
    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> findByBudgetId(Long budgetId) {
        return expenseRepository.findByBudget_BudgetId(budgetId);
    }

    @Override
    public List<Expense> findByBudgetIdOrderByDateDesc(Long budgetId) {
        return expenseRepository.findByBudget_BudgetIdOrderByDateDesc(budgetId);
    }

    @Override
    public  Expense save(Expense expense) {
        return  expenseRepository.save(expense);
    }

    @Override
    public  void delete(Expense expense) {
        expenseRepository.delete(expense);
    }

}
