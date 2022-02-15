package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.InfectedEntity;
import com.veritas.TMServer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
    Boolean existsByEmail(String email);
    UserEntity findByEmailAndPassword(String email, String password);

    @Query(value = "SELECT * FROM USER_ENTITY WHERE USERNAME = :username", nativeQuery = true)
    List<UserEntity> findSearchList(@Param("username") String username);
}
