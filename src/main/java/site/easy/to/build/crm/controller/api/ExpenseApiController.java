package site.easy.to.build.crm.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.service.expense.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseApiController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseApiController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Obtenir une dépense par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable("id") Long id) {
        Expense expense = expenseService.findExpenseById(id);
        if (expense == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    // Obtenir toutes les dépenses
    @GetMapping()
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseService.findAll();
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    // Créer une nouvelle dépense
    @PostMapping("/create")
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.save(expense);
        return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
    }

    // Mettre à jour une dépense existante
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable("id") Long id,
                                                 @RequestBody Expense expense) {
        Expense existingExpense = expenseService.findExpenseById(id);
        if (existingExpense == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        expense.setId(id);
        Expense updatedExpense = expenseService.save(expense);
        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    // Supprimer une dépense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") Long id) {
        Expense expense = expenseService.findExpenseById(id);
        if (expense == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        expenseService.delete(expense);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Obtenir les dépenses pour un budget spécifique
    @GetMapping("/budget/{budgetId}")
    public ResponseEntity<List<Expense>> getExpensesByBudget(@PathVariable("budgetId") Long budgetId) {
        List<Expense> expenses = expenseService.findByBudgetId(budgetId);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }
}