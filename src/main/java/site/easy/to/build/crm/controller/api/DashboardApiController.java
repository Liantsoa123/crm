package site.easy.to.build.crm.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.dto.CustomerStatisticsDTO;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customer-statistics")
    public ResponseEntity<List<CustomerStatisticsDTO>> getCustomerStatistics() {
        return ResponseEntity.ok(customerService.getCustomerStatistics());
    }
}