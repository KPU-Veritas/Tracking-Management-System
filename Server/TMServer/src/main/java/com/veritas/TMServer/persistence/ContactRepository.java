package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.ContactEntity;
import com.veritas.TMServer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, String> {
    List<ContactEntity> findByUuid(String uuid);

    @Query(value = "SELECT * FROM CONTACT WHERE UUID = :uuid AND DATE = :date", nativeQuery = true)
    List<ContactEntity> findSearchList(@Param("uuid") String uuid, @Param("date") String date);
}
