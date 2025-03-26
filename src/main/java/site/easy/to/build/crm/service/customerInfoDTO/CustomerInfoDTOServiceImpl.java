package site.easy.to.build.crm.service.customerInfoDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.checkerframework.common.value.qual.ArrayLen;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.dto.CustomerInfoDTO;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CustomerInfoDTOServiceImpl  implements CustomerInfoDTOService{

    @PersistenceContext
    private final EntityManager entityManager ;

    @Override
    public CustomerInfoDTO changeValueDuplicate(CustomerInfoDTO customerInfoDTO) {
        Customer customer = customerInfoDTO.getCustomer();
        customer.setEmail( "copied"+ customer.getEmail());
        customer.setName("copie"+ customer.getName());
        customer.setCustomerId(null);
        List<Ticket> ticketsList = new ArrayList<>();
        for (Ticket ticket : customerInfoDTO.getAlltickets()){
            ticket.setTicketId(-1);
            ticket.setCustomer(customer);
            ticketsList.add(ticket);
        }
        List<Lead> leads = new ArrayList<>();
        for (Lead lead : customerInfoDTO.getAllLeads()){
            lead.setLeadId(-1);
            lead.setCustomer(customer);
            leads.add(lead);
        }

        return new CustomerInfoDTO(customer,ticketsList, leads);
    }


    @Override
    @Transactional
    public void save(CustomerInfoDTO customerInfoDTO) {
        // Merge le customer d'abord
        Customer customer = entityManager.merge(customerInfoDTO.getCustomer());

        for (Ticket ticket : customerInfoDTO.getAlltickets()) {
            ticket.setTicketId(0);
            if (ticket.getExpense() != null) {
                ticket.getExpense().setExpenseId(0L);
                ticket.setExpense(entityManager.merge(ticket.getExpense()));
            }

            ticket.setCustomer(customer);
            entityManager.merge(ticket);
        }

        for (Lead lead : customerInfoDTO.getAllLeads()) {
            lead.setLeadId(0);
            if (lead.getExpense() != null) {
                lead.getExpense().setExpenseId(0L);
                lead.setExpense(entityManager.merge(lead.getExpense()));
            }
            lead.setCustomer(customer);
            entityManager.merge(lead);
        }
    }

}
