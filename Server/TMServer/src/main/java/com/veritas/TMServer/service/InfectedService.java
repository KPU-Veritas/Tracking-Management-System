package com.veritas.TMServer.service;

import com.veritas.TMServer.model.InfectedEntity;
import com.veritas.TMServer.persistence.InfectedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class InfectedService {
    @Autowired
    private InfectedRepository repository;

    public List<InfectedEntity> create(final InfectedEntity entity){
        validate(entity);

        repository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());
        return repository.findByUuid(entity.getUuid());
    }

    public List<InfectedEntity> delete(final InfectedEntity entity){
        validate(entity);

        try{
            repository.delete(entity);
        }catch (Exception e){
            log.error("error deleting entity {} {}", entity.getId(), e);
            throw new RuntimeException("error deleting entity " + entity.getId());
        }
        return retrieve(entity.getUuid());
    }
    private void validate(final InfectedEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

        if(entity.getUuid() == null) {
            log.warn("Uknown user.");
            throw new RuntimeException("Uknown user.");
        }
    }

    public List<InfectedEntity> retrieve(final String uuid){
        return repository.findByUuid(uuid);
    }
    public List<InfectedEntity> infectedList() {
        return repository.findAll();
    }

    public List<InfectedEntity> nonCheckedInfectedList() { return repository.findAllManagerCheckFalse(); }      //현재 사용되지않음
    public long managerNotice() { return repository.countAllManagerCheckFalse(); }

    public void updateCheck(String id, boolean managerCheck) {
        if(managerCheck) repository.updateToTrueManagerCheck(id);
        else repository.updateToFalseManagerCheck(id);
    }

}
