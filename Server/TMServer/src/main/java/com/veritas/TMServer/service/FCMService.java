package com.veritas.TMServer.service;

import com.veritas.TMServer.model.FCMEntity;
import com.veritas.TMServer.persistence.FCMRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FCMService {
    @Autowired
    private FCMRepository repository;

    public List<FCMEntity> create(final FCMEntity entity){
        validate(entity);

        repository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());
        return retrieve(entity.getUuid());
    }

    public List<FCMEntity> delete(final FCMEntity entity){
        validate(entity);

        try{
            repository.delete(entity);
        }catch (Exception e){
            log.error("error deleting entity {} {}", entity.getId(), e);
            throw new RuntimeException("error deleting entity " + entity.getId());
        }
        return retrieve(entity.getUuid());
    }

    private void validate(final FCMEntity entity){
        if(entity == null){
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

        if(entity.getUuid() == null){
            log.warn("Uknown user.");
            throw new RuntimeException("Uknown user.");
        }
    }
    public List<FCMEntity> retrieve(final String uuid){
        return repository.findFCMList(uuid);
    }
}
