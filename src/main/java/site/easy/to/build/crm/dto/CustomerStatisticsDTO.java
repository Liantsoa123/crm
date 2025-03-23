package site.easy.to.build.crm.dto;

import lombok.Data;

@Data
public class CustomerStatisticsDTO {
    private String customerName;
    private Long ticketCount;
    private Long leadCount;
    private Integer customerId;
}