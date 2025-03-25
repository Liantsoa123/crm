package site.easy.to.build.crm.service.customer;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.dto.CustomerCsvDTO;
import site.easy.to.build.crm.dto.CustomerStatisticsDTO;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.util.EmailTokenUtils;

import javax.swing.text.IconView;

@Service
@AllArgsConstructor
public class CustomerCsvDTOServiceImpl  implements CustomerCsvDTOService{

  private  final PasswordEncoder passwordEncoder ;

  @Override
  public Customer convertToCustomer(CustomerCsvDTO customerCsvDTO, User admin ) {
    CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
    customerLoginInfo.setEmail(customerCsvDTO.getEmail());
    customerLoginInfo.setToken(EmailTokenUtils.generateToken());
    customerLoginInfo.setPasswordSet(true);
    String password = passwordEncoder.encode(customerCsvDTO.getEmail());
    customerLoginInfo.setPassword(password);

    Customer customer = new Customer();
    customer.setEmail(customerCsvDTO.getEmail());
    customer.setName(customerCsvDTO.getName());
    customer.setCustomerLoginInfo(customerLoginInfo);
    customer.setCountry("Madagascar");
    customer.setUser(admin);
    return customer;
  }
}
