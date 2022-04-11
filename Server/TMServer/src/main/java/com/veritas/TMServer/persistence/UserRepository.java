package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.InfectedEntity;
import com.veritas.TMServer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
    UserEntity findByUuid(String uuid);
    Boolean existsByEmail(String email);
    UserEntity findByEmailAndPassword(String email, String password);

    @Query(value = "SELECT * FROM USER_ENTITY WHERE USERNAME = :username", nativeQuery = true)
    List<UserEntity> findSearchList(@Param("username") String username);

    @Query(value = "SELECT RISK FROM USER_ENTITY WHERE UUID = :uuid", nativeQuery = true)
    float findRiskByUuid(@Param("uuid") String uuid);

    // 임시 모든 FCM 토큰 리스트 받아오기
    @Query(value = "SELECT FCM_TOKEN FROM USER_ENTITY", nativeQuery = true)
    List<String> findFcmTokenList();

    @Query(value = "SELECT * FROM USER_ENTITY WHERE RISK >= :risk", nativeQuery = true)
    List<UserEntity> findOverRisk(@Param("risk") float risk);


    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET FCM_TOKEN = :fcmToken WHERE UUID = :uuid", nativeQuery = true)
    int updateFcmToken(@Param("uuid") String uuid, @Param("fcmToken") String fcmToken);


    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET RISK = :risk WHERE UUID = :uuid", nativeQuery = true)
    int updateRisk(@Param("uuid") String uuid, @Param("risk") float risk);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET CONTACT_DEGREE = :contactdegree WHERE UUID = :uuid", nativeQuery = true)
    int updateContactDegree(@Param("uuid") String uuid, @Param("contactdegree") int contactDegree);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET CONTACT_DEGREE = 0 WHERE CONTACT_DEGREE > 0", nativeQuery = true)
    int resetContactDegree();

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET RISK = 0 WHERE RISK > 0", nativeQuery = true)
    int resetRisk();


}
