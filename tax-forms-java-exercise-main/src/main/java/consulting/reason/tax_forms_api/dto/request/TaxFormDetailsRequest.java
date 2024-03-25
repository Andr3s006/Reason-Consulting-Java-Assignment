package consulting.reason.tax_forms_api.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class TaxFormDetailsRequest {

    @NotNull(message = "Assessed value is required")
    @Min(value = 0, message = "Assessed value must be greater than or equal to 0")
    @Max(value = 100000, message = "Assessed value must be less than or equal to 100000")
    private Integer assessedValue;

    @NotNull(message = "Appraised value is required")
    @Min(value = 0, message = "Appraised value must be greater than or equal to 0")
    @Max(value = 100000, message = "Appraised value must be less than or equal to 100000")
    private Long appraisedValue;

    @NotNull(message = "Ratio is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Ratio must be greater than 0")
    @DecimalMax(value = "1.0", inclusive = false, message = "Ratio must be less than 1")
    private Double ratio;

    @Size(max = 500, message = "Comments must be less than or equal to 500 characters")
    private String comments;
}
