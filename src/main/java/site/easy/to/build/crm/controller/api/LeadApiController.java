package site.easy.to.build.crm.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.service.expense.ExpenseServiceImpl;
import site.easy.to.build.crm.service.lead.LeadService;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadApiController {

    private final LeadService leadService;
    private final ExpenseServiceImpl expenseServiceImpl;

    @Autowired
    public LeadApiController(LeadService leadService, ExpenseServiceImpl expenseServiceImpl) {
        this.leadService = leadService;
        this.expenseServiceImpl = expenseServiceImpl;
    }

    // Obtenir un lead par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLeadById(@PathVariable("id") int id) {
        Lead lead = leadService.findByLeadId(id);
        if (lead == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lead, HttpStatus.OK);
    }

    // Obtenir tous les leads
    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads() {
        List<Lead> leads = leadService.findAll();
        return new ResponseEntity<>(leads, HttpStatus.OK);
    }

    // Créer un nouveau lead
    @PostMapping
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        Lead savedLead = leadService.save(lead);
        return new ResponseEntity<>(savedLead, HttpStatus.CREATED);
    }

    // Mettre à jour un lead existant
    @PutMapping("/{id}")
    public ResponseEntity<Lead> updateLead(@PathVariable("id") int id, @RequestBody Lead lead) {
        Lead existingLead = leadService.findByLeadId(id);
        if (existingLead == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        lead.setLeadId(id);
        Lead updatedLead = leadService.save(lead);
        return new ResponseEntity<>(updatedLead, HttpStatus.OK);
    }

    // Supprimer un lead

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteLead(@PathVariable("id") int id) {
        Lead lead = leadService.findByLeadId(id);
        if (lead == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (lead.getExpense() != null ){
            expenseServiceImpl.delete(lead.getExpense());
        }
        leadService.delete(lead);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}