package com.veritas.TMServer.persistence;


import com.veritas.TMServer.model.DeviceEntity;
import com.veritas.TMServer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, String> {
   Boolean existsByPlace(String place);

   @Transactional
   @Modifying
   @Query(value = "DELETE FROM DEVICE_ENTITY WHERE ID = :id", nativeQuery = true)
   void delete(@Param("id") String id);


}
