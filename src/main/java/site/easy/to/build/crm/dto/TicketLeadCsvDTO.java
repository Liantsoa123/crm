package site.easy.to.build.crm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonPropertyOrder({"customer_email", "subject_or_name", "type", "status", "expense"})
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TicketLeadCsvDTO {
    @JsonProperty("customer_email")
    @Email(message = "Customer_email is not valid")
    @NotBlank(message = "Customer_email is required")
    private String email;

    @JsonProperty("subject_or_name")
    @NotBlank(message = "Subject_or_name is required")
    private String subjectOrName;

    @JsonProperty("type")
    @NotBlank(message = "Type is required")
    @Pattern(regexp = "^(lead|ticket)$", message = "Invalid type")
    private String type;

    @JsonProperty("status")
    @NotBlank(message = "Status is required")
    private String status;

    @JsonProperty("expense")
    @Positive
    private Double expense;
}
