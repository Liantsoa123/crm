package site.easy.to.build.crm.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.util.Optional;

@Controller
@RequestMapping("/employee/customer")
public class BudgetController {

    private final BudgetService budgetService;
    private final CustomerService customerService;
    private final AuthenticationUtils authenticationUtils;

    @Autowired
    public BudgetController(BudgetService budgetService, CustomerService customerService, AuthenticationUtils authenticationUtils) {
        this.budgetService = budgetService;
        this.customerService = customerService;
        this.authenticationUtils = authenticationUtils;
    }

    @PostMapping("/budget-customer/{customerId}")
    public String showBudgetForm(@PathVariable("customerId") int customerId, Model model) {
        Customer customer = customerService.findByCustomerId(customerId);

        Budget budget = new Budget();
        budget.setCustomer(customer);

        model.addAttribute("customer", customer);
        model.addAttribute("budget", budget);

        return "budget/create-budget";
    }

}