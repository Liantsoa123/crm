package site.easy.to.build.crm.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.expense.ExpenseServiceImpl;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketApiController {

    private final TicketService ticketService;
    private final ExpenseServiceImpl expenseServiceImpl;

    @Autowired
    public TicketApiController(TicketService ticketService, ExpenseServiceImpl expenseServiceImpl) {
        this.ticketService = ticketService;
        this.expenseServiceImpl = expenseServiceImpl;
    }

    // Obtenir un ticket par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable("id") int id) {
        Ticket ticket = ticketService.findByTicketId(id);
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    // Mettre à jour un ticket existant
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable("id") int id,
                                               @RequestBody Ticket ticket) {
        Ticket existingTicket = ticketService.findByTicketId(id);
        if (existingTicket == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ticket.setTicketId(id);
        Ticket updatedTicket = ticketService.save(ticket);
        return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
    }

    // Obtenir tous les tickets
    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.findAll();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    // Supprimer un ticket
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") int id) {
        Ticket ticket = ticketService.findByTicketId(id);
        if (ticket == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        expenseServiceImpl.delete(ticket.getExpense());
        ticketService.delete(ticket);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Créer un nouveau ticket
    @PostMapping("/create")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        Ticket savedTicket = ticketService.save(ticket);
        return new ResponseEntity<>(savedTicket, HttpStatus.CREATED);
    }
}