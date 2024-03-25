package consulting.reason.tax_forms_api.service;

import consulting.reason.tax_forms_api.dto.TaxFormDetailsDto;
import consulting.reason.tax_forms_api.dto.TaxFormDto;
import consulting.reason.tax_forms_api.dto.request.TaxFormDetailsRequest;
import consulting.reason.tax_forms_api.entity.TaxForm;
import consulting.reason.tax_forms_api.entity.TaxFormHistory;
import consulting.reason.tax_forms_api.enums.TaxFormStatus;
import consulting.reason.tax_forms_api.repository.TaxFormHistoryRepository;
import consulting.reason.tax_forms_api.repository.TaxFormRepository;
import consulting.reason.tax_forms_api.util.TaxFormStatusUtils;
import jakarta.validation.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaxFormServiceImpl implements TaxFormService {
    private final TaxFormRepository taxFormRepository;
    private final TaxFormHistoryRepository taxFormHistoryRepository;
    private final ModelMapper modelMapper;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public TaxFormServiceImpl(TaxFormRepository taxFormRepository,
                              ModelMapper modelMapper,TaxFormHistoryRepository taxFormHistoryRepository) {
        this.taxFormRepository = taxFormRepository;
        this.taxFormHistoryRepository = taxFormHistoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaxFormDto> findAllByYear(Integer year) {
        return taxFormRepository.findAllByFormYear(year).stream()
                .map(taxForm -> modelMapper.map(taxForm, TaxFormDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaxFormDto> findById(Integer id) {
        return taxFormRepository.findById(id)
                .map(taxForm -> modelMapper.map(taxForm, TaxFormDto.class));
    }

    @Override
    @Transactional
    public Optional<TaxFormDto> save(Integer id, TaxFormDetailsRequest taxFormDetailsRequest) {

        Set<ConstraintViolation<TaxFormDetailsRequest>> violations = validator.validate(taxFormDetailsRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return taxFormRepository.findById(id)
                .map(taxForm -> {
                    TaxFormStatusUtils.save(taxForm);
                    taxForm.setDetails(modelMapper.map(taxFormDetailsRequest, TaxFormDetailsDto.class));

                    taxFormRepository.save(taxForm);

                    return modelMapper.map(taxForm, TaxFormDto.class);
                });
    }

    @Override
    public Optional<TaxFormDto> submit(Integer id) {
        TaxForm taxForm = taxFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TaxForm not found"));

        TaxFormStatusUtils.submit(taxForm);
        taxFormRepository.save(taxForm);

        TaxFormHistory taxFormHistory = new TaxFormHistory();
        taxFormHistory.setTaxForm(taxForm);
        taxFormHistory.setType(TaxFormStatus.SUBMITTED);
        taxFormHistory.setCreatedAt(ZonedDateTime.now());
        taxFormHistoryRepository.save(taxFormHistory);
        return Optional.of(modelMapper.map(taxForm, TaxFormDto.class));
    }

    @Override
    public Optional<TaxFormDto> returned(Integer id) {
        TaxForm taxForm = taxFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TaxForm not found"));

        TaxFormStatusUtils.returnForm(taxForm);
        taxFormRepository.save(taxForm);

        TaxFormHistory taxFormHistory = new TaxFormHistory();
        taxFormHistory.setTaxForm(taxForm);
        taxFormHistory.setType(TaxFormStatus.RETURNED);
        taxFormHistory.setCreatedAt(ZonedDateTime.now());
        taxFormHistoryRepository.save(taxFormHistory);
        return Optional.of(modelMapper.map(taxForm, TaxFormDto.class));
    }

    @Override
    public Optional<TaxFormDto> accept(Integer id) {
        TaxForm taxForm = taxFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TaxForm not found"));

        TaxFormStatusUtils.accept(taxForm);
        taxFormRepository.save(taxForm);

        TaxFormHistory taxFormHistory = new TaxFormHistory();
        taxFormHistory.setTaxForm(taxForm);
        taxFormHistory.setType(TaxFormStatus.ACCEPTED);
        taxFormHistory.setCreatedAt(ZonedDateTime.now());
        taxFormHistoryRepository.save(taxFormHistory);
        return Optional.of(modelMapper.map(taxForm, TaxFormDto.class));
    }
}
