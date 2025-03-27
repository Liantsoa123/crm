package site.easy.to.build.crm.service.ticketleadCsvDTO;

import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.dto.TicketLeadCsvDTO;
import site.easy.to.build.crm.service.customer.CustomerServiceImpl;
import site.easy.to.build.crm.service.ticket.TicketServiceImpl;
import site.easy.to.build.crm.service.user.UserServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TicketLeadCsvDTOServiceImpl implements TicketLeadCsvDTOService {


    private final UserServiceImpl userServiceImpl;
    private final CustomerServiceImpl customerServiceImpl;
    private final TicketServiceImpl ticketServiceImpl;

    public TicketLeadCsvDTOServiceImpl(UserServiceImpl userServiceImpl, CustomerServiceImpl customerServiceImpl, TicketServiceImpl ticketServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.customerServiceImpl = customerServiceImpl;
        this.ticketServiceImpl = ticketServiceImpl;
    }

    @Override
    public Expense convertToExpense(TicketLeadCsvDTO ticketLeadCsvDTO) {
        Expense expense = new Expense();
        expense.setAmount(BigDecimal.valueOf(ticketLeadCsvDTO.getExpense()));
        expense.setDate(LocalDate.now());
        expense.setDescription(ticketLeadCsvDTO.getSubjectOrName() + "Expense");
        return expense;
    }

    @Override
    public Ticket convertToTicket(TicketLeadCsvDTO ticketLeadCsvDTO, StringBuilder errorMessage, int row) {
        if (!ticketLeadCsvDTO.getStatus().matches("^(open|assigned|on-hold|in-progress|resolved|closed|reopened|pending-customer-response|escalated|archived)$")) {
            errorMessage.append(  "<li> Invalid status for the ticket in row " + row + "</li>");
            return null;
        }
        Customer customer = customerServiceImpl.findByEmail(ticketLeadCsvDTO.getEmail());
        if (customer == null) {
            errorMessage.append(" <li> Customer not found for the ticket in row " + row + "</li>");
            return null;
        }
        Ticket ticket = new Ticket();
        ticket.setSubject(ticketLeadCsvDTO.getSubjectOrName());
        ticket.setStatus(ticketLeadCsvDTO.getStatus());
        ticket.setPriority("low");
        ticket.setManager(userServiceImpl.findFirstByOrderByIdAsc());
        ticket.setEmployee(userServiceImpl.findFirstByOrderByIdAsc());
        ticket.setCustomer(customer);
        ticket.setExpense(convertToExpense(ticketLeadCsvDTO));
        ticket.setCreatedAt(LocalDateTime.now());
        return ticket;

    }

    @Override
    public Lead convertToLead(TicketLeadCsvDTO ticketLeadCsvDTO, StringBuilder errorMessage, int row) {
        if (!ticketLeadCsvDTO.getStatus().matches("^(meeting-to-schedule|scheduled|archived|success|assign-to-sales)$")){
            errorMessage.append("<li> Invalid status for the lead in row " + row + "</li>");
            return null;
        }
        Customer customer = customerServiceImpl.findByEmail(ticketLeadCsvDTO.getEmail());
        if (customer == null) {
            errorMessage.append("<li> Customer not found for the lead in row " + row + "</li>");
            return null;
        }
        Lead lead = new Lead();
        lead.setName(ticketLeadCsvDTO.getSubjectOrName());
        lead.setStatus(ticketLeadCsvDTO.getStatus());
        lead.setManager(userServiceImpl.findFirstByOrderByIdAsc());
        lead.setEmployee(userServiceImpl.findFirstByOrderByIdAsc());
        lead.setCustomer(customer);
        lead.setExpense(convertToExpense(ticketLeadCsvDTO));
        lead.setCreatedAt(LocalDateTime.now());
        return lead;
    }
}
