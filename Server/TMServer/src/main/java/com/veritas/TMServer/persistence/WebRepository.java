package com.veritas.TMServer.persistence;

import com.veritas.TMServer.model.WebEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebRepository extends JpaRepository<WebEntity, String> {
    WebEntity findByEmail(String email);
    Boolean existsByEmail(String email);
    WebEntity findByEmailAndPassword(String email, String password);
}
