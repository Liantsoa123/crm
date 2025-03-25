package site.easy.to.build.crm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@JsonPropertyOrder({"customer_email", "customer_name"})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerCsvDTO {

    @JsonProperty("customer_email")
    @Email(message = "Customer_email is not valid")
    @NotBlank(message = "Customer_email is required")
    private String email;

    @JsonProperty("customer_name")
    @NotBlank(message = "Customer_name is required")
    private String name;
}
