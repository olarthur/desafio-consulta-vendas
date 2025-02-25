package com.devsuperior.dsmeta.services;

import com.devsuperior.dsmeta.dto.ReportDTO;
import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.ReportProjection;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repository;

    LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

    public SaleMinDTO findById(Long id) {
        Optional<Sale> result = repository.findById(id);
        Sale entity = result.get();
        return new SaleMinDTO(entity);
    }

    public Page<ReportDTO> findBySale(String minDate, String maxDate, String name, Pageable pageable) {

        if (minDate.isBlank() && maxDate.isBlank()) {
            minDate = today.minusYears(1L).toString();
            maxDate = today.toString();
        }

        Page<ReportProjection> reportProjections = repository.searchReport
                (LocalDate.parse(minDate), LocalDate.parse(maxDate), name, pageable);

        return reportProjections.map(ReportDTO::new);
    }
}
