package site.easy.to.build.crm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerStatisticsDTO {
    private String customerName;
    private Long ticketCount;
    private Long leadCount;
    private Integer customerId;
    private BigDecimal totalBudget;
}