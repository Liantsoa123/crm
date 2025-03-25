package site.easy.to.build.crm.service.customer;

import org.w3c.dom.stylesheets.LinkStyle;
import site.easy.to.build.crm.dto.CustomerCsvDTO;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.User;

import java.util.List;

public interface CustomerCsvDTOService {

    Customer convertToCustomer(CustomerCsvDTO customerCsvDTO , User user);

    List<Customer> convertToCustomers(List<CustomerCsvDTO> customerCsvDTOs, User user);
}
