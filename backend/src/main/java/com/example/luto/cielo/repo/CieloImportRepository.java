package com.example.luto.cielo.repo;

import com.example.luto.cielo.repo.model.CieloImport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CieloImportRepository extends JpaRepository<CieloImport, String> {
  boolean existsByTenantIdAndFileName(String tenantId, String fileName);
}
