package com.example.trackingmanagementserver.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.trackingmanagementserver.model.ContactEntity;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, String>{
	List<ContactEntity> findByUuid(String uuid);
}
