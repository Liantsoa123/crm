package site.easy.to.build.crm.service.customerInfoDTO;

import site.easy.to.build.crm.dto.CustomerInfoDTO;

public interface CustomerInfoDTOService {

    CustomerInfoDTO changeValueDuplicate(CustomerInfoDTO customerInfoDTO);

    void save(CustomerInfoDTO customerInfoDTO) ;

}
