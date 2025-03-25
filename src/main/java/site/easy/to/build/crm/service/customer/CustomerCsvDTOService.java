package site.easy.to.build.crm.service.customer;

import site.easy.to.build.crm.dto.CustomerCsvDTO;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.User;

public interface CustomerCsvDTOService {

    Customer convertToCustomer(CustomerCsvDTO customerCsvDTO , User user);

}
