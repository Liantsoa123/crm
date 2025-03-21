package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.service.system.SystemService;
import site.easy.to.build.crm.util.AuthorizationUtil;

@Controller
@RequestMapping("/manager/system")
public class SystemController {

    private final SystemService systemService;

    @Autowired
    public SystemController(SystemService systemService) {
        this.systemService = systemService;
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
}