package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.ContactEntity;
import com.veritas.TMServer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, String> {   // 접촉 기록 Table 사용 Query 문
    List<ContactEntity> findByUuid(String uuid);

    @Query(value = "SELECT * FROM CONTACT WHERE UUID = :uuid AND DATE BETWEEN :date AND :date2 ORDER BY DATE ASC, FIRST_TIME ASC", nativeQuery = true)
    List<ContactEntity> findSearchList(@Param("uuid") String uuid, @Param("date") String date, @Param("date2") String date2);

    @Query(value = "SELECT * FROM CONTACT WHERE UUID = :uuid AND DATE >= :date ORDER BY DATE ASC, FIRST_TIME ASC", nativeQuery = true)
    List<ContactEntity> findFirstContactList(@Param("uuid") String uuid, @Param("date") String date);

    @Query(value = "SELECT * FROM CONTACT WHERE (UUID = :uuid AND DATE = :date AND LAST_TIME >= :time) OR UUID = :uuid AND DATE > :date ORDER BY DATE ASC, FIRST_TIME ASC", nativeQuery = true)
    List<ContactEntity> findContinuousContactList(@Param("uuid") String uuid, @Param("date") String date, @Param("time") String time);


}
