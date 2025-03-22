package site.easy.to.build.crm.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.dto.BudgetStatusDTO;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.budget.BudgetServiceImpl;
import site.easy.to.build.crm.service.budget.BudgetStatusService;
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
    private final BudgetStatusService budgetStatusService;

    @Autowired
    public ExpenseController(ExpenseService expenseService, LeadService leadService, AuthenticationUtils authenticationUtils, LeadServiceImpl leadServiceImpl, BudgetServiceImpl budgetServiceImpl, TicketServiceImpl ticketServiceImpl, BudgetStatusService budgetStatusService) {
        this.expenseService = expenseService;
        this.leadService = leadService;
        this.authenticationUtils = authenticationUtils;
        this.leadServiceImpl = leadServiceImpl;
        this.budgetServiceImpl = budgetServiceImpl;
        this.ticketServiceImpl = ticketServiceImpl;
        this.budgetStatusService = budgetStatusService;
    }

    @PostMapping("/lead/{leadId}")
    public String showExpenseForm(@PathVariable("leadId") int leadId, Model model) {
        Lead lead = leadServiceImpl.findByLeadId(leadId);
        if (lead == null) {
            return "redirect:/employee/lead/my-leads";
        }

        Expense expense = new Expense();
        lead.setExpense(expense);

        List<BudgetStatusDTO> budgetStatusDTOS = budgetStatusService.getBudgetStatusForCustomer(lead.getCustomer().getCustomerId());

        model.addAttribute("lead", lead);
        model.addAttribute("expense", expense);
        model.addAttribute("budgetStatus", budgetStatusDTOS);
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

        List<BudgetStatusDTO> budgetStatusDTOS = budgetStatusService.getBudgetStatusForCustomer(ticket.getCustomer().getCustomerId());


        model.addAttribute("ticket", ticket);
        model.addAttribute("expense", expense);
        model.addAttribute("budgetStatus", budgetStatusDTOS);
        model.addAttribute("leadId", null);

        return "expense/create-expense";
    }

    @PostMapping("/save-expense")
    public String saveExpense(@ModelAttribute("expense") @Valid Expense expense,
                              BindingResult bindingResult,
                              @RequestParam(value = "leadId", required = false) Integer leadId,
                              @RequestParam(value = "ticketId", required = false) Integer ticketId,
                              Model model) {

        // Validation des entrées
        if (bindingResult.hasErrors()) {
            // Recharger les données nécessaires pour le formulaire
            if (leadId != null) {
                Lead lead = leadServiceImpl.findByLeadId(leadId);
                List<BudgetStatusDTO> budgetStatusDTOS = budgetStatusService.getBudgetStatusForCustomer(lead.getCustomer().getCustomerId());
                model.addAttribute("lead", lead);
                model.addAttribute("budgetStatus", budgetStatusDTOS);
                model.addAttribute("ticketId", null);
            } else if (ticketId != null) {
                Ticket ticket = ticketServiceImpl.findByTicketId(ticketId);
                List<BudgetStatusDTO> budgetStatusDTOS = budgetStatusService.getBudgetStatusForCustomer(ticket.getCustomer().getCustomerId());
                model.addAttribute("ticket", ticket);
                model.addAttribute("budgetStatus", budgetStatusDTOS);
                model.addAttribute("leadId", null);
            }
            return "expense/create-expense";
        }

        expenseService.save(expense);

        // Associer la dépense au lead ou au ticket et mettre à jour l'entité
        if (leadId != null) {
            Lead lead = leadServiceImpl.findByLeadId(leadId);
            if (lead != null) {
                lead.setExpense(expense);
                leadService.save(lead);
                return "redirect:/employee/lead/manager/all-leads";
            }
        } else if (ticketId != null) {
            Ticket ticket = ticketServiceImpl.findByTicketId(ticketId);
            if (ticket != null) {
                ticket.setExpense(expense);
                ticketServiceImpl.save(ticket);
                return "redirect:/employee/ticket/manager/all-tickets";
            }
        }

        return "redirect:/employee/dashboard";
    }

}