package site.easy.to.build.crm.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.dto.BudgetCsvDTO;
import site.easy.to.build.crm.dto.CustomerCsvDTO;
import site.easy.to.build.crm.dto.TicketLeadCsvDTO;
import site.easy.to.build.crm.service.csv.CsvImportService;
import site.easy.to.build.crm.util.AuthorizationUtil;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/manager/system/importCsv")
@AllArgsConstructor
public class ImportCsvController {

    private final CsvImportService csvImportService;

    @GetMapping("/form")
    public String showImportForm(Authentication authentication) {
        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            return "error/access-denied";
        }
        return "import/form";
    }

    @PostMapping
    public String importCsvFiles(@RequestParam("customerFile") MultipartFile customerFile,
                                 @RequestParam("ticketLeadFile") MultipartFile ticketLeadFile,
                                 @RequestParam("budgetFile") MultipartFile budgetFile,
                                 Model model) {
        StringBuilder customerErrors = new StringBuilder();
        StringBuilder ticketLeadErrors = new StringBuilder();
        StringBuilder budgetErrors = new StringBuilder();

        try {
            // Process Customer CSV
            List<CustomerCsvDTO> customers = csvImportService.read(CustomerCsvDTO.class, customerFile.getInputStream(), customerErrors);

            // Process Ticket and Lead CSV
            List<TicketLeadCsvDTO> ticketLeads = csvImportService.read(TicketLeadCsvDTO.class, ticketLeadFile.getInputStream(), ticketLeadErrors);

            // Process Budget CSV
            List<BudgetCsvDTO> budgets = csvImportService.read(BudgetCsvDTO.class, budgetFile.getInputStream(), budgetErrors);

            // Add error messages to the model
            if (customerErrors.length() > 0) {
                model.addAttribute("customerErrors", customerErrors.toString());
            }
            if (ticketLeadErrors.length() > 0) {
                model.addAttribute("ticketLeadErrors", ticketLeadErrors.toString());
            }
            if (budgetErrors.length() > 0) {
                model.addAttribute("budgetErrors", budgetErrors.toString());
            }

            // Check if there are any errors
            if (customerErrors.length() > 0 || ticketLeadErrors.length() > 0 || budgetErrors.length() > 0) {
                return "import/form";
            }

            // Add success message
            model.addAttribute("successMessage", "CSV files imported successfully!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error reading CSV files: " + e.getMessage());
        }

        return "import/form";
    }

}
