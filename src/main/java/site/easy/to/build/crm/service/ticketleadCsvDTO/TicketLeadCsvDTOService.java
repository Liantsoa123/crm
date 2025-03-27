package site.easy.to.build.crm.service.ticketleadCsvDTO;

import site.easy.to.build.crm.dto.TicketLeadCsvDTO;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;

public interface TicketLeadCsvDTOService {

    Expense convertToExpense(TicketLeadCsvDTO ticketLeadCsvDTO);

    Ticket convertToTicket(TicketLeadCsvDTO ticketLeadCsvDTO , StringBuilder errorMessage, int row);

    Lead convertToLead(TicketLeadCsvDTO ticketLeadCsvDTO,  StringBuilder errorMessage, int row );
}
