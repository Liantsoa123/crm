package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.dto.CustomerStatisticsDTO;
import site.easy.to.build.crm.entity.Customer;


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
        c.customerId as customerId)
    FROM Customer c
    LEFT JOIN Ticket t ON c.customerId = t.customer.customerId
    LEFT JOIN Lead l ON c.customerId = l.customer.customerId
    GROUP BY c.customerId, c.name
    """)
    List<CustomerStatisticsDTO> getCustomerStatistics();
}
