package site.easy.to.build.crm.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.service.budget.BudgetService;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetApiController {
    private  final BudgetService budgetService;

    public BudgetApiController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getAllBudgets() {
        List<Budget> budgets = budgetService.findAll();
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

}
