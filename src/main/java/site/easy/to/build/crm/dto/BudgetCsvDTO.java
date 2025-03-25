package site.easy.to.build.crm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonPropertyOrder({"customer_email", "budget"})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BudgetCsvDTO {

    @NotBlank
    @Email(message = "Customer email is not valid")
    @JsonProperty("customer_email")
    private String customerEmail;

    @JsonProperty("budget")
    @DecimalMin(value = "0.0", message = "Budget must be greater than 0")
    private Double budget;

}
