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
        return expenseRepository.findExpenseById(id);
    }

    @Override
    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> findByBudgetId(Long budgetId) {
        return expenseRepository.findByBudgetId(budgetId);
    }

    @Override
    public List<Expense> findByBudgetIdOrderByDateDesc(Long budgetId) {
        return expenseRepository.findByBudgetIdOrderByDateDesc(budgetId);
    }

    @Override
    public  void save(Expense expense) {
        expenseRepository.save(expense);
    }

}
