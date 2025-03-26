package site.easy.to.build.crm.controller.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.dto.CustomerInfoDTO;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.service.customerInfoDTO.CustomerInfoDTOServiceImpl;

@RestController
@RequestMapping("/api/import")
@AllArgsConstructor
public class ImportDuplicate {

    private final CustomerInfoDTOServiceImpl customerInfoDTOService;

    @PostMapping("/duplicate")
    public ResponseEntity<CustomerInfoDTO> insertDuplicate (@RequestBody CustomerInfoDTO customerInfoDTO){
        System.out.println("Welcome to the import duplicate API");
        CustomerInfoDTO customerInfoDTOGenerated = customerInfoDTOService.changeValueDuplicate(customerInfoDTO);
        customerInfoDTOService.save(customerInfoDTOGenerated);
        return  new ResponseEntity<>(customerInfoDTOGenerated, org.springframework.http.HttpStatus.CREATED);
    }
}
