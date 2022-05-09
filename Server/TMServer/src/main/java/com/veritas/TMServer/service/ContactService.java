package com.veritas.TMServer.service;

import com.veritas.TMServer.model.ContactEntity;
import com.veritas.TMServer.model.UserEntity;
import com.veritas.TMServer.persistence.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactService {
    @Autowired
    private ContactRepository repository;

    public List<ContactEntity> create(final ContactEntity entity){
        validate(entity);

        repository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());
        return retrieve(entity.getUuid());
    }

    public List<ContactEntity> update(final ContactEntity entity){
        validate(entity);

        final Optional<ContactEntity> original = repository.findById(entity.getId().toString());

        original.ifPresent(contact -> {
            contact.setChecked(entity.isChecked());
            repository.save(contact);
        });
        return retrieve(entity.getUuid());
    }

    public List<ContactEntity> delete(final ContactEntity entity){
        validate(entity);

        try{
            repository.delete(entity);
        }catch (Exception e){
            log.error("error deleting entity {} {}", entity.getId(), e);
            throw new RuntimeException("error deleting entity " + entity.getId());
        }
        return retrieve(entity.getUuid());
    }

    private void validate(final ContactEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

        if(entity.getUuid() == null) {
            log.warn("Uknown user.");
            throw new RuntimeException("Uknown user.");
        }
    }

    public List<ContactEntity> retrieve(final String uuid){
        return repository.findByUuid(uuid);
    }
    public List<ContactEntity> contactList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        return repository.findAll(pageRequest).getContent();
    }
    public List<ContactEntity> testList() { return repository.findAll(); }
    public List<ContactEntity> searchList(String uuid, String date, String date2) { return repository.findSearchList(uuid, date, date2);}
    public List<ContactEntity> findFirstContactList(String uuid, String date) { return repository.findFirstContactList(uuid, date); }
    public List<ContactEntity> findContinuousContactList(String uuid, String date, String time) { return repository.findContinuousContactList(uuid, date, time); }
    public Long count() { return repository.count(); }
}
