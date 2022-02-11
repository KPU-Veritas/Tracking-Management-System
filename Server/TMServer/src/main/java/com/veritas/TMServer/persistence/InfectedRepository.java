package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.InfectedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InfectedRepository extends JpaRepository<InfectedEntity, String> {
    List<InfectedEntity> findByUuid(String uuid);

    @Query(value = "SELECT COUNT(*) FROM INFECTED WHERE MANAGER_CHECK = 0", nativeQuery = true)
    long countAllManagerCheckFalse();

    @Query(value = "SELECT * FROM INFECTED WHERE MANAGER_CHECK = 0", nativeQuery = true)
    List<InfectedEntity> findAllManagerCheckFalse();

    @Query(value = "UPDATE INFECTED SET MANAGER_CHECK = '1' WHERE ID = ?1", nativeQuery = true)
    InfectedEntity updateToTrueManagerCheck(String id);

    @Query(value = "UPDATE INFECTED SET MANAGER_CHECK = '0' WHERE ID = ?1", nativeQuery = true)
    InfectedEntity updateToFalseManagerCheck(String id);
}
