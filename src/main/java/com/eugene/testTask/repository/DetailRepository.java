package com.eugene.testTask.repository;

import com.eugene.testTask.model.Detail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailRepository extends JpaRepository<Detail, Long> {
    @EntityGraph(attributePaths = "material")
    Optional<Detail> getDetailById(Long id);

    @Override
    @EntityGraph(attributePaths = "material")
    Page<Detail> findAll(Pageable pageable);

    boolean existsByDecimalNumber(String decimalNumber);

    boolean existsByDecimalNumberAndIdNot(String decimalNumber, Long id);
}
