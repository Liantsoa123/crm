package site.easy.to.build.crm.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.budget.BudgetServiceImpl;
import site.easy.to.build.crm.service.expense.ExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.lead.LeadServiceImpl;
import site.easy.to.build.crm.service.ticket.TicketServiceImpl;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.util.List;

@Controller
@RequestMapping("/employee/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final LeadService leadService;
    private final AuthenticationUtils authenticationUtils;
    private final LeadServiceImpl leadServiceImpl;
    private final BudgetServiceImpl budgetServiceImpl;
    private final TicketServiceImpl ticketServiceImpl;

    @Autowired
    public ExpenseController(ExpenseService expenseService, LeadService leadService, AuthenticationUtils authenticationUtils, LeadServiceImpl leadServiceImpl, BudgetServiceImpl budgetServiceImpl, TicketServiceImpl ticketServiceImpl) {
        this.expenseService = expenseService;
        this.leadService = leadService;
        this.authenticationUtils = authenticationUtils;
        this.leadServiceImpl = leadServiceImpl;
        this.budgetServiceImpl = budgetServiceImpl;
        this.ticketServiceImpl = ticketServiceImpl;
    }

    @PostMapping("/lead/{leadId}")
    public String showExpenseForm(@PathVariable("leadId") int leadId, Model model) {
        Lead lead = leadServiceImpl.findByLeadId(leadId);
        if (lead == null) {
            return "redirect:/employee/lead/my-leads";
        }

        Expense expense = new Expense();
        lead.setExpense(expense);

        List<Budget> budgets = budgetServiceImpl.findBudgetsByCustomer_CustomerId(lead.getCustomer().getCustomerId());

        model.addAttribute("lead", lead);
        model.addAttribute("expense", expense);
        model.addAttribute("budgets", budgets);
        model.addAttribute("ticketId", null);


        return "expense/create-expense";
    }

    @PostMapping("/ticket/{ticketId}")
    public String showExpenseFormTicket(@PathVariable("ticketId") int ticketId, Model model) {
        Ticket ticket = ticketServiceImpl.findByTicketId(ticketId);
        if (ticket == null) {
            return "redirect:/employee/ticket/my-tickets";
        }

        Expense expense = new Expense();
        ticket.setExpense(expense);

        List<Budget> budgets = budgetServiceImpl.findBudgetsByCustomer_CustomerId(ticket.getCustomer().getCustomerId());

        model.addAttribute("ticket", ticket);
        model.addAttribute("expense", expense);
        model.addAttribute("budgets", budgets);
        model.addAttribute("leadId", null);

        return "expense/create-expense";
    }

  /*  @PostMapping("/save-expense")
    public String saveExpense(@ModelAttribute("expense") @Valid Expense expense,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Lead lead = expense.getLead();
            model.addAttribute("lead", lead);
            model.addAttribute("home", authenticationUtils.getHomePagePath());
            return "expense/create-expense";
        }

        // Sauvegarder la dépense
        expenseService.save(expense);

        return "redirect:/employee/lead/my-leads";
    }*/
}