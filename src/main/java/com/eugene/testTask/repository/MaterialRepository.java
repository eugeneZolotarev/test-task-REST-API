package com.eugene.testTask.repository;

import com.eugene.testTask.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    boolean existsByName(String name);
}
