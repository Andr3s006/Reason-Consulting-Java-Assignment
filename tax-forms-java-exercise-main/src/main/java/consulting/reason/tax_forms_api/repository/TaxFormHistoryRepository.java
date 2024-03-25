package consulting.reason.tax_forms_api.repository;

import consulting.reason.tax_forms_api.entity.TaxForm;
import consulting.reason.tax_forms_api.entity.TaxFormHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxFormHistoryRepository extends JpaRepository<TaxFormHistory, Integer> {
    List<TaxFormHistory> findAllByTaxForm(TaxForm taxForm);
}
