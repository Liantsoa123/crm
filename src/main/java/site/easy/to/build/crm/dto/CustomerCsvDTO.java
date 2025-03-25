package site.easy.to.build.crm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerCsvDTO {

    @JsonProperty("customer_name")
    @NotBlank(message = "Customer_name is required")
    private String name;

    @JsonProperty("customer_email")
    @Email(message = "Customer_email is not valid")
    @NotBlank(message = "Customer_email is required")
    private String email;

}
