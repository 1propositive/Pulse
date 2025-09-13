package com.example.luto.cielo.repo;

import com.example.luto.cielo.repo.model.ReconciliationEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReconciliationEntryRepository extends JpaRepository<ReconciliationEntry, String> {
}
