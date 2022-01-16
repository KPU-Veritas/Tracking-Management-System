package com.example.trackingmanagementserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.trackingmanagementserver.model.ContactEntity;
import com.example.trackingmanagementserver.persistence.ContactRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContactService {
	@Autowired
	private ContactRepository repository;
	
	public List<ContactEntity> create(final ContactEntity entity){
		validate(entity);
		
		repository.save(entity);
		
		log.info("Entity Id : {} is saved.", entity.getId());
		
		return repository.findByUuid(entity.getUuid());
	}
	
	public List<ContactEntity> retrieve(final String uuid){
		return repository.findByUuid(uuid);
	}
	
	private void validate(final ContactEntity entity) {
		// Validation
				if(entity == null) {
					log.warn("Entity cannot be null");
					throw new RuntimeException("Entity cannot be null");
				}
				
				if(entity.getUuid() == null) {
					log.warn("Unkown user.");
					throw new RuntimeException("Unkown user.");
				}
	}
}
