package consulting.reason.tax_forms_api.controller;

import consulting.reason.tax_forms_api.dto.TaxFormDto;
import consulting.reason.tax_forms_api.dto.request.TaxFormDetailsRequest;
import consulting.reason.tax_forms_api.exception.TaxFormNotFoundException;
import consulting.reason.tax_forms_api.service.TaxFormService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoints.FORMS)
public class TaxFormController {
    private final TaxFormService taxFormService;

    public TaxFormController(TaxFormService taxFormService) {
        this.taxFormService = taxFormService;
    }

    @GetMapping
    public List<TaxFormDto> findAllByYear(@RequestParam Integer year) {
        return taxFormService.findAllByYear(year);
    }

    @GetMapping("/{id}")
    public TaxFormDto findById(@PathVariable Integer id) {
        return taxFormService.findById(id)
                .orElseThrow(() -> new TaxFormNotFoundException(id));
    }

    @PatchMapping("/{id}")
    public TaxFormDto save(@PathVariable Integer id, @Valid @RequestBody TaxFormDetailsRequest taxFormDetailsRequest) {
        return taxFormService.save(id, taxFormDetailsRequest)
                .orElseThrow(() -> new TaxFormNotFoundException(id));
    }
    @PatchMapping("/{id}/submit")
    public ResponseEntity<TaxFormDto> submit(@PathVariable Integer id) {
        return taxFormService.submit(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<TaxFormDto> returned(@PathVariable Integer id) {
        return taxFormService.returned(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<TaxFormDto> accept(@PathVariable Integer id) {
        return taxFormService.accept(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
