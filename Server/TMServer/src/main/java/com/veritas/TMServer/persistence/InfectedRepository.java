package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.InfectedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InfectedRepository extends JpaRepository<InfectedEntity, String> {     // 감염 정보 Table 사용 Query 문
    List<InfectedEntity> findByUuid(String uuid);
    List<InfectedEntity> findById(Long id);

    @Query(value = "SELECT COUNT(*) FROM INFECTED WHERE MANAGER_CHECK = 0", nativeQuery = true)
    long countAllManagerCheckFalse();

    @Query(value = "SELECT * FROM INFECTED WHERE MANAGER_CHECK = 0", nativeQuery = true)
    List<InfectedEntity> findAllManagerCheckFalse();

    @Transactional
    @Modifying
    @Query(value = "UPDATE INFECTED SET MANAGER_CHECK = '1' WHERE ID = :id", nativeQuery = true)
    int updateToTrueManagerCheck(@Param("id") String id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE INFECTED SET MANAGER_CHECK = '0' WHERE ID = :id", nativeQuery = true)
    int updateToFalseManagerCheck(@Param("id") String id);
}
