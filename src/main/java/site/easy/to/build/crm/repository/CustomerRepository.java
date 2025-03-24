package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.dto.CustomerStatisticsDTO;
import site.easy.to.build.crm.entity.Customer;


import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public Customer findByCustomerId(int customerId);

    public List<Customer> findByUserId(int userId);

    public Customer findByEmail(String email);

    public List<Customer> findAll();

    public List<Customer> findByUserIdOrderByCreatedAtDesc(int userId, Pageable pageable);

    long countByUserId(int userId);

    @Query(value = """
                SELECT new site.easy.to.build.crm.dto.CustomerStatisticsDTO(
                    c.name as customerName,
                    COUNT(DISTINCT t.ticketId) as ticketCount, 
                    COUNT(DISTINCT l.leadId) as leadCount,
                    c.customerId as customerId,
                    COALESCE(SUM(b.amount), 0) as totalBudget)
                FROM Customer c
                LEFT JOIN Ticket t ON c.customerId = t.customer.customerId
                LEFT JOIN Lead l ON c.customerId = l.customer.customerId
                LEFT JOIN Budget b ON c.customerId = b.customer.customerId
                GROUP BY c.customerId, c.name
            """)
    List<CustomerStatisticsDTO> getCustomerStatistics();

    @Query(value = """
                SELECT COALESCE(SUM(e.amount), 0) FROM Ticket t 
                join Expense e on e.expenseId = t.expense.expenseId 
                where t.customer.customerId = :customerId
            """)
    double getTotalExpensesTicketByCustomerId(int customerId);

    @Query(value = """
                    SELECT COALESCE(SUM(e.amount), 0) FROM Lead l
                    join Expense e on e.expenseId = l.expense.expenseId
                    where l.customer.customerId = :customerId
            """)
    double getTotalExpensesLeadByCustomerId(int customerId);

    @Query(value = """
                SELECT COALESCE(SUM(b.amount), 0) FROM Budget b group by b.customer.customerId
            """)
    double getTotalBudgetByCustomerId(int customerId);
}
