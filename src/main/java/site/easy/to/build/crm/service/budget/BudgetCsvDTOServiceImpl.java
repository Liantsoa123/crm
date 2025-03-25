package site.easy.to.build.crm.service.budget;

import com.google.auto.value.extension.serializable.SerializableAutoValue;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.dto.BudgetCsvDTO;
import site.easy.to.build.crm.dto.BudgetStatusDTO;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.customer.CustomerServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetCsvDTOServiceImpl implements BudgetCsvDTOService {
    private final CustomerServiceImpl customerServiceImpl;

    public BudgetCsvDTOServiceImpl(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @Override
    public Budget convertToBudget(BudgetCsvDTO budgetCsvDTO, StringBuilder errorMessage, int row) {
        Budget budget = new Budget();
        Customer customer = customerServiceImpl.findByEmail(budgetCsvDTO.getCustomerEmail());
        if (customer == null) {
            errorMessage.append("<li> Customer not found for the budget in row " + row + "</li>");
            return null;
        }
        budget.setName("Budget " + customer.getName());
        budget.setAmount(BigDecimal.valueOf(budgetCsvDTO.getBudget()));
        budget.setStartDate(LocalDate.now());
        budget.setEndDate(LocalDate.now().plusMonths(1));
        budget.setCustomer(customer);

        return budget;
    }

    @Override
    public List<Budget> convertToBudgets(List<BudgetCsvDTO> budgetCsvDTOs, StringBuilder errorMessage) {
        List<Budget> budgets = new ArrayList<>();
        for (int i = 0; i < budgetCsvDTOs.size(); i++) {
            Budget budget = convertToBudget(budgetCsvDTOs.get(i), errorMessage, i + 1);
            if (budget == null) {
                continue;
            }
            budgets.add(budget);
        }
        return budgets;
    }
}
