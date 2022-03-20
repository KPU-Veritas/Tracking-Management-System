package com.veritas.TMServer.service;

import com.veritas.TMServer.model.ContactEntity;
import com.veritas.TMServer.model.DeviceEntity;
import com.veritas.TMServer.model.UserEntity;
import com.veritas.TMServer.persistence.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public DeviceEntity create(final DeviceEntity deviceEntity) {
        if(deviceEntity == null || deviceEntity.getPlace() == null) {
            throw new RuntimeException("Invaild arguments");
        }
        final String place= deviceEntity.getPlace();
        if(deviceRepository.existsByPlace(place)) {
            log.warn("Email already exists {}", place);
            throw new RuntimeException("Email already exists");
        }

        return deviceRepository.save(deviceEntity);
    }

    public List<DeviceEntity> deviceList() { return deviceRepository.findAll(); }
    public void delete(String id) { deviceRepository.delete(id);}

}
