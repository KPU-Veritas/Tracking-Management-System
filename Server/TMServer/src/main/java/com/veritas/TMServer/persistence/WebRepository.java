package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.WebEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WebRepository extends JpaRepository<WebEntity, String> {
    WebEntity findByEmail(String email);
    Boolean existsByEmail(String email);
    WebEntity findByEmailAndPassword(String email, String password);

    @Query(value = "SELECT WARNING_LEVEL FROM WEB_ENTITY", nativeQuery = true)
    int getLevel();

    @Transactional
    @Modifying
    @Query(value = "UPDATE  WEB_ENTITY SET WARNING_LEVEL = :level", nativeQuery = true)
    void setLevel(@Param("level") int warningLevel);
}
