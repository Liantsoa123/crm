package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import site.easy.to.build.crm.service.generate.GenerateData;
import site.easy.to.build.crm.service.system.SystemService;
import site.easy.to.build.crm.util.AuthorizationUtil;

import java.sql.SQLDataException;

@Controller
@RequestMapping("/manager/system")
public class SystemController {

    private final SystemService systemService;
    private final GenerateData generateData;

    @Autowired
    public SystemController(SystemService systemService, GenerateData generateData) {
        this.systemService = systemService;
        this.generateData = generateData;
    }

    @GetMapping("reset/confirm")
    public String resetData(Authentication authentication, RedirectAttributes redirectAttributes) {
        // Check if user has manager role
        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            return "error/access-denied";
        }

        try {
            systemService.resetAllData();
            redirectAttributes.addFlashAttribute("successMessage", "Les données ont été réinitialisées avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la réinitialisation des données.");
        }

        return "redirect:/manager/system/reset";
    }

    @GetMapping("reset")
    public String showResetPage(Model model,
                                @ModelAttribute("successMessage") String successMessage,
                                @ModelAttribute("errorMessage") String errorMessage) {
        if (!successMessage.isEmpty()) {
            System.out.println(successMessage);
            model.addAttribute("successMessage", successMessage);
        }
        if (!errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "reset/reset";
    }

    @GetMapping("generate/form")
    public String showGenerateForm(Authentication authentication, Model model) {
        // Check if user has manager role
        if (!AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            return "error/access-denied";
        }
        return "generate/generateData";
    }

    @PostMapping("generate")
    public String generateData(Authentication authenticationa,
                               @RequestParam("customerCount") int customerCount,
                               @RequestParam("budgetPerCustomer") int budgetPerCustomer,
                               @RequestParam("ticketPerCustomer") int ticketPerCustomer,
                               @RequestParam("leadPerCustomer") int leadPerCustomer,Model model) throws SQLDataException {
        // Check if user has manager role
        if (!AuthorizationUtil.hasRole(authenticationa, "ROLE_MANAGER")){
            return "error/access-denied";
        }
        generateData.generateAndSaveRandomData(customerCount, budgetPerCustomer, ticketPerCustomer, leadPerCustomer);
        model.addAttribute("successMessage", "Les données ont été générées avec succès.");
        return "generate/generateData";
    }

}