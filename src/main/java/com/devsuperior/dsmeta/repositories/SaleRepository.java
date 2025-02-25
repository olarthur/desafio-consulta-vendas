package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.ReportProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(nativeQuery = true, value = "SELECT TB_SELLER.id, date, amount, TB_SELLER.name " +
            "FROM TB_SALES " +
            "INNER JOIN TB_SELLER ON TB_SELLER.id = TB_SALES.seller_id " +
            "WHERE date BETWEEN :minDate AND :maxDate " +
            "AND UPPER(TB_SELLER.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<ReportProjection> searchReport(
            LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);

    @Query(value = "SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(obj.seller.name, " +
            "SUM(obj.amount)) " +
            "FROM Sale obj " +
            "WHERE obj.date BETWEEN :minDate AND :maxDate " +
            "GROUP BY obj.seller.name")
    List<SaleSummaryDTO> searchSummary(LocalDate minDate, LocalDate maxDate);
}
