package com.veritas.TMServer.controller;

import com.veritas.TMServer.dto.InfectedDTO;
import com.veritas.TMServer.dto.ResponseDTO;
import com.veritas.TMServer.model.InfectedEntity;
import com.veritas.TMServer.model.UserEntity;
import com.veritas.TMServer.service.InfectedService;
import com.veritas.TMServer.service.RiskService;
import com.veritas.TMServer.service.UserService;
import com.veritas.TMServer.service.WebService;
import jdk.management.jfr.RecordingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/infected")
public class InfectedController {   // 감염 정보 컨트롤러
    @Autowired
    private InfectedService service;
    @Autowired
    private WebService webService;
    @Autowired
    private UserService userService;
    @Autowired
    private RiskService riskService;

    @PostMapping("/addinfected")
    public ResponseEntity<?> createInfected(    // 감염 정보를 등록할 때 사용하는 url
            @AuthenticationPrincipal String uuid,
            @RequestBody InfectedDTO dto){
        try{
            InfectedEntity entity = InfectedDTO.infectedEntity(dto);
            entity.setId(null);
            entity.setUuid(uuid);
            List<InfectedEntity> entities = service.create(entity);

            riskService.firstCalculation(entities.get(0));

            userService.updateRisk(uuid, 0);

            long risk = webService.getLevel();
            List<UserEntity> riskOverList = userService.findOverRisk(risk);

            UserEntity userEntity = userService.findByUuid(dto.getUuid());
            if(riskOverList.indexOf(userEntity) >= 0) riskOverList.remove(riskOverList.indexOf(userEntity));

            for(int i = 0; i < riskOverList.size(); i++) {
                riskService.notificate(riskOverList.get(i), "You are the " + riskOverList.get(i).getContactDegree() + " contact with COVID-19.",
                        "Your risk is " + riskOverList.get(i).getRisk() + "%.");
            }       //설정된 위험도를 초과한 접촉자들에게 알림을 보냄

            List<InfectedDTO> dtos = entities.stream().map(InfectedDTO::new).collect(Collectors.toList());
            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().data(dtos).build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/deleteinfected")
    public ResponseEntity<?> deleteInfected(    // 감염 정보를 지울 때 사용하는 url
            @AuthenticationPrincipal String uuid,
            @RequestBody InfectedDTO dto
    ){
        try{
            InfectedEntity entity = InfectedDTO.infectedEntity(dto);
            entity.setUuid(uuid);
            List<InfectedEntity> entities = service.delete(entity);
            List<InfectedDTO> dtos = entities.stream().map(InfectedDTO::new).collect(Collectors.toList());
            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().data(dtos).build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<InfectedDTO> response = ResponseDTO.<InfectedDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
