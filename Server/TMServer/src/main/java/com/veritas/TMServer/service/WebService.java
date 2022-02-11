package com.veritas.TMServer.service;

import com.veritas.TMServer.model.UserEntity;
import com.veritas.TMServer.model.WebEntity;
import com.veritas.TMServer.persistence.WebRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WebService {
    @Autowired
    private WebRepository webRepository;

    public WebEntity create(final WebEntity webEntity) {
        if(webEntity == null || webEntity.getEmail() == null) {
            throw new RuntimeException("Invaild arguments");
        }
        final String email= webEntity.getEmail();
        if(webRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }

        return webRepository.save(webEntity);
    }


    public WebEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final WebEntity originalUser = webRepository.findByEmail(email);

        if(originalUser != null && encoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }
        return null;
    }

    public long countManager() { return webRepository.count(); }

}
