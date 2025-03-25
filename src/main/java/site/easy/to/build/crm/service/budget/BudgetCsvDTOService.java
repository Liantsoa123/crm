package site.easy.to.build.crm.service.budget;

import site.easy.to.build.crm.dto.BudgetCsvDTO;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;

import java.util.List;

public interface BudgetCsvDTOService {
    Budget convertToBudget(BudgetCsvDTO budgetCsvDTO, StringBuilder errorMessage, int row);

    List<Budget> convertToBudgets(List<BudgetCsvDTO> budgetCsvDTOs, StringBuilder errorMessage)  ;

}
