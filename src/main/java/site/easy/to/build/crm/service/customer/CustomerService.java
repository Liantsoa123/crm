package site.easy.to.build.crm.service.customer;

import org.checkerframework.checker.units.qual.C;
import site.easy.to.build.crm.dto.CustomerStatisticsDTO;
import site.easy.to.build.crm.entity.Customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListResourceBundle;

public interface CustomerService {

    public Customer findByCustomerId(int customerId);

    public List<Customer> findByUserId(int userId);

    public Customer findByEmail(String email);

    public List<Customer> findAll();

    public Customer save(Customer customer);

    public void delete(Customer customer);

    public List<Customer> getRecentCustomers(int userId, int limit);

    long countByUserId(int userId);

    List<CustomerStatisticsDTO> getCustomerStatistics();

    double getTotalExpensesTicketByCustomerId(int customerId);

    double getTotalExpensesLeadByCustomerId(int customerId);

    Double getTotalBudgetByCustomerId(int customerId);

    List<Customer> saveAll(List<Customer> customers);
}
