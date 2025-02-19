package consulting.reason.tax_forms_api.dto;

import consulting.reason.tax_forms_api.enums.TaxFormStatus;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TaxFormHistoryDto {

    private Integer id;
    private Integer taxFormId;
    private ZonedDateTime createdAt;
    private TaxFormStatus type;
}