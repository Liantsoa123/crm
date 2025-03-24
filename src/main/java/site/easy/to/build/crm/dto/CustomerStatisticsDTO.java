package site.easy.to.build.crm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerStatisticsDTO {
    private String customerName;
    private double ticketCount;
    private double leadCount;
    private Integer customerId;
    private double totalBudget;
}