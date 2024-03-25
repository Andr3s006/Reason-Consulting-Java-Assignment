package consulting.reason.tax_forms_api.entity;

import consulting.reason.tax_forms_api.enums.TaxFormStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tax_form_history")
@Entity
public class TaxFormHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tax_form_id", nullable = false)
    private TaxForm taxForm;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaxFormStatus type;

}
