package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.InfectedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfectedRepository extends JpaRepository<InfectedEntity, String> {
    List<InfectedEntity> findByUuid(String uuid);
}
