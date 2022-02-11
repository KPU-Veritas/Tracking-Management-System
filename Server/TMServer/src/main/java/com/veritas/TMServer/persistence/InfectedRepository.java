package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.InfectedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InfectedRepository extends JpaRepository<InfectedEntity, String> {
    List<InfectedEntity> findByUuid(String uuid);

    @Query(value = "SELECT COUNT(*) FROM INFECTED WHERE MANAGER_CHECK = 0", nativeQuery = true)
    long countAllManagerCheckFalse();

    @Query(value = "SELECT * FROM INFECTED WHERE MANAGER_CHECK = 0", nativeQuery = true)
    List<InfectedEntity> findAllManagerCheckFalse();

    @Modifying
    @Query(value = "update infected set manager_check = '1' where id = :id;", nativeQuery = true)
    void updateToTrueManagerCheck(@Param("id") String id);

    @Modifying
    @Query(value = "update infected set manager_check = '0' where id = :id;", nativeQuery = true)
    default void updateToFalseManagerCheck(@Param("id") String id) {

    }
}
