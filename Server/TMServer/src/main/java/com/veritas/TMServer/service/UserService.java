package com.veritas.TMServer.service;

import com.veritas.TMServer.model.UserEntity;
import com.veritas.TMServer.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if(userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invaild arguments");
        }
        final String email= userEntity.getEmail();
        if(userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByEmail(email);

        if(originalUser != null && encoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }
        return null;
    }

    public UserEntity findByUuid(String uuid) { return userRepository.findByUuid(uuid); }
    public List<UserEntity> userList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        return userRepository.findAll(pageRequest).getContent();
    }
    public List<UserEntity> searchList(String search) { return userRepository.findSearchList(search); }
    public void updateFcmToken(String uuid, String fcmToken){ userRepository.updateFcmToken(uuid, fcmToken); }
    public void updateRisk(String uuid, float risk) { userRepository.updateRisk(uuid, risk); }
    public void updateContactDegree(String uuid, int contactDegree) { userRepository.updateContactDegree(uuid, contactDegree); }
    public float findRiskByUuid(String uuid) { return userRepository.findRiskByUuid(uuid); }
    public Integer  findContactDegreeByUuid(String uuid) { return userRepository.findContactDegreeByUuid(uuid); }
    public List<String> findFcmTokenList() { return userRepository.findFcmTokenList(); }
    public List<UserEntity> findOverRisk(float risk) { return userRepository.findOverRisk(risk); }
    public void reset() { userRepository.resetRisk(); userRepository.resetContactDegree(); }
    public Long count() { return userRepository.count(); }
    public void updateEmail(String uuid, String value) { userRepository.updateEmail(uuid, value); }
    public void updateUserName(String uuid, String value) { userRepository.updateUserName(uuid, value); }
    public void updateSimpleAddress(String uuid, String value) { userRepository.updateSimpleAddress(uuid, value); }
    public void updateDetailAddress(String uuid, String value) { userRepository.updateDetailAddress(uuid, value); }
    public void updatePhoneNumber(String uuid, String value) { userRepository.updatePhoneNumber(uuid, value); }
    public void updatePassWord(String uuid, String value) { userRepository.updatePassWord(uuid, value); }

}
