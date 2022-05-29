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
public interface UserRepository extends JpaRepository<UserEntity, String> { // 사용자 정보 Table 사용 Query 문
    UserEntity findByEmail(String email);
    UserEntity findByUuid(String uuid);
    Boolean existsByEmail(String email);
    UserEntity findByEmailAndPassword(String email, String password);

    @Query(value = "SELECT * FROM USER_ENTITY WHERE USERNAME = :search OR EMAIL = :search OR PHONE_NUMBER = :search", nativeQuery = true)
    List<UserEntity> findSearchList(@Param("search") String search);

    @Query(value = "SELECT RISK FROM USER_ENTITY WHERE UUID = :uuid", nativeQuery = true)
    float findRiskByUuid(@Param("uuid") String uuid);

    @Query(value = "SELECT CONTACT_DEGREE FROM USER_ENTITY WHERE UUID = :uuid", nativeQuery = true)
    int findContactDegreeByUuid(@Param("uuid") String uuid);

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

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET EMAIL = :value WHERE UUID = :uuid", nativeQuery = true)
    int updateEmail(@Param("uuid") String uuid, @Param("value") String value);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET USERNAME = :value WHERE UUID = :uuid", nativeQuery = true)
    int updateUserName(@Param("uuid") String uuid, @Param("value") String value);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET PASSWORD = :value WHERE UUID = :uuid", nativeQuery = true)
    int updatePassWord(@Param("uuid") String uuid, @Param("value") String value);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET PHONE_NUMBER = :value WHERE UUID = :uuid", nativeQuery = true)
    int updatePhoneNumber(@Param("uuid") String uuid, @Param("value") String value);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET SIMPLE_ADDRESS = :value WHERE UUID = :uuid", nativeQuery = true)
    int updateSimpleAddress(@Param("uuid") String uuid, @Param("value") String value);

    @Transactional
    @Modifying
    @Query(value = "UPDATE USER_ENTITY SET DETAIL_ADDRESS = :value WHERE UUID = :uuid", nativeQuery = true)
    int updateDetailAddress(@Param("uuid") String uuid, @Param("value") String value);


}
