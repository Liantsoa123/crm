package site.easy.to.build.crm.service.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.dto.BudgetCsvDTO;
import site.easy.to.build.crm.dto.CustomerCsvDTO;
import site.easy.to.build.crm.dto.TicketLeadCsvDTO;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.budget.BudgetCsvDTOServiceImpl;
import site.easy.to.build.crm.service.customer.CustomerCsvDTOServiceImpl;
import site.easy.to.build.crm.service.ticketleadCsvDTO.TicketLeadCsvDTOServiceImpl;
import site.easy.to.build.crm.service.user.UserServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CsvImportService {

    private final CsvMapper csvMapper = new CsvMapper();
    private final Validator validator;
    private final CustomerCsvDTOServiceImpl customerCsvDTOServiceImpl;
    private final BudgetCsvDTOServiceImpl budgetCsvDTOServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final TicketLeadCsvDTOServiceImpl ticketLeadCsvDTOServiceImpl;

    @PersistenceContext
    private final EntityManager entityManager ;

    public <T> List<T> read(Class<T> clazz, InputStream inputStream, StringBuilder errorMessage) throws IOException {
        // Configure CSV mapper
        csvMapper.enable(CsvParser.Feature.TRIM_SPACES);
        csvMapper.enable(CsvParser.Feature.ALLOW_COMMENTS);
        CsvSchema csvSchema = csvMapper.schemaFor(clazz).withHeader().withColumnSeparator(',');
        // Parse CSV data
        MappingIterator<T> mappingIterator = csvMapper.readerFor(clazz).with(csvSchema).readValues(inputStream);
        List<T> result = mappingIterator.readAll();

        // Validate each row and collect errors
        for (int i = 0; i < result.size(); i++) {
            T item = result.get(i);
            Set<ConstraintViolation<T>> violations = validator.validate(item);

            if (!violations.isEmpty()) {
                String rowErrors = violations.stream()
                        .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                        .collect(Collectors.joining(","));

                errorMessage.append(String.format("Row %d has validation errors: [%s]%n", i + 2, rowErrors));
            }
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveALl (List<CustomerCsvDTO> customerCsvDTOS , List<BudgetCsvDTO> budgetCsvDTOS, List<TicketLeadCsvDTO> ticketLeadCsvDTOS , StringBuilder errorMessage)throws  Exception{
        User admin = userServiceImpl.findFirstByOrderByIdAsc();
        List<Customer> customers = customerCsvDTOServiceImpl.convertToCustomers(customerCsvDTOS, admin);
        for ( Customer customer: customers ){
            entityManager.persist(customer.getCustomerLoginInfo());
            entityManager.persist(customer);
        }

        List<Budget> budgets = budgetCsvDTOServiceImpl.convertToBudgets(budgetCsvDTOS, errorMessage);
        for ( Budget budget: budgets ){
            entityManager.persist(budget);
        }

        System.out.println("size ticket = " + ticketLeadCsvDTOS.size());
        for (int i = 0; i < ticketLeadCsvDTOS.size(); i++) {
            TicketLeadCsvDTO ticketLeadCsvDTO = ticketLeadCsvDTOS.get(i);
            if (ticketLeadCsvDTO.getType().equalsIgnoreCase("ticket")){
                Ticket ticket = ticketLeadCsvDTOServiceImpl.convertToTicket(ticketLeadCsvDTO, errorMessage, i);
                if (ticket != null) {
                    entityManager.persist(ticket.getExpense());
                    entityManager.persist(ticket);
                }
            }else if (ticketLeadCsvDTO.getType().equalsIgnoreCase("lead")){
                Lead lead = ticketLeadCsvDTOServiceImpl.convertToLead(ticketLeadCsvDTO, errorMessage, i);
                if (lead != null) {
                    entityManager.persist(lead.getExpense());
                    entityManager.persist(lead);
                }
            }
        }
        System.out.println("errorMessage=" + errorMessage.length());
        if ( errorMessage.length() > 0 ){
            throw new Exception(errorMessage.toString());
        }
    }
}