package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.FCMEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FCMRepository extends JpaRepository<FCMEntity, String> {   // FCM 알림 기록 Table 사용 Query 문
    @Query(value = "SELECT * FROM FCMTABLE WHERE UUID = :uuid ORDER BY DATE ASC, TIME ASC", nativeQuery = true)
    List<FCMEntity> findFCMList(@Param("uuid") String uuid);

    @Query(value = "DELETE FROM FCMTABLE WHERE ID = :id", nativeQuery = true)
    void delete(@Param("id") String id);
}
