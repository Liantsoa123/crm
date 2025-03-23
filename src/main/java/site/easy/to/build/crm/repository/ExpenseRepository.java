package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Expense;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    public Expense findByExpenseId(Long id);

    public List<Expense> findAll();

    public List<Expense> findByBudget_BudgetId(Long budgetId);

    List<Expense> findByBudget_BudgetIdOrderByDateDesc(Long budgetBudgetId);

    public  Expense save(Expense expense);

}
