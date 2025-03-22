package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Expense;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    public Expense findExpenseById(Long id);

    public List<Expense> findAll();

    public List<Expense> findByBudgetId(Long budgetId);

    public List<Expense> findByBudgetIdOrderByDateDesc(Long budgetId);

    public  Expense save(Expense expense);

}
