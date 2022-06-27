package com.hse.knopkabackend.services;

import com.hse.knopkabackend.DTO.BatchDTO;
import com.hse.knopkabackend.DTO.BatchResponseDTO;
import com.hse.knopkabackend.configs.ClickHouseKnopkaConfig;
import com.hse.knopkabackend.models.knopka.Knopka;
import com.hse.knopkabackend.models.knopkauser.KnopkaUser;
import com.hse.knopkabackend.repositories.entityforclick.EntityForClickRepository;
import com.hse.knopkabackend.repositories.knopka.KnopkaRepository;
import com.hse.knopkabackend.repositories.knopkauser.KnopkaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class EntityForClickService {
    private final EntityForClickRepository entityForClickRepository;
    private final KnopkaUserRepository knopkaUserRepository;
    private final KnopkaRepository knopkaRepository;
    private final KnopkaService knopkaService;

    @Autowired
    public EntityForClickService(EntityForClickRepository entityForClickRepository, KnopkaUserRepository knopkaUserRepository, KnopkaService knopkaService, KnopkaRepository knopkaRepository) {
        this.entityForClickRepository = entityForClickRepository;
        this.knopkaUserRepository = knopkaUserRepository;
        this.knopkaService = knopkaService;
        this.knopkaRepository = knopkaRepository;
    }


    public Long getClicks(Long knopkaId, String token) {
        KnopkaUser knopkaUserById = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(() -> {
            throw new IllegalStateException("Invalid id");
        });
        if (!Objects.equals(token, knopkaUserById.getToken())) throw new IllegalStateException("Invalid token");
        return entityForClickRepository.getNumberOfClicks(knopkaId);
    }

    public ResponseEntity<String> registerNewBatch(String token, BatchDTO batchDTO) {
        KnopkaUser knopkaUserById = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(() -> {
            throw new IllegalStateException("Invalid token");
        });
        Knopka knopka = knopkaRepository.findById(batchDTO.getClickedKnopkaId()).orElseThrow(
                () -> new IllegalStateException("knopka with id: " + batchDTO.getClickedKnopkaId() + " doesn't exist")
        );
        if (Objects.equals(token, knopkaUserById.getToken())) {

            if (isAlreadyWritten(batchDTO)) {
                System.out.println("Bebra");
                return new ResponseEntity<>(batchDTO.getTime()+"/"+batchDTO.getAuthorId(), HttpStatus.OK);
            }


            StringBuilder queryBuffer = new StringBuilder("INSERT INTO entityforbatch (author_id, clicked_knopka_id, pushes, region, time_of_click) values ");
            queryBuffer.append("('")
                    .append(batchDTO.getAuthorId())
                    .append("', '")
                    .append(batchDTO.getClickedKnopkaId())
                    .append("', '")
                    .append(batchDTO.getPushes())
                    .append("', '")
                    .append(batchDTO.getRegion())
                    .append("', '")
                    .append(batchDTO.getTime())
                    .append("')");
            System.out.println(queryBuffer);
            try {
                Connection conn = ClickHouseKnopkaConfig.getConnection();
                Statement statement = conn.createStatement();
                statement.executeQuery(queryBuffer.toString());
                conn.close();
                knopkaService.updatePushesCount(batchDTO.getClickedKnopkaId(), batchDTO.getPushes(), token);
            } catch (SQLException e) {
                throw new IllegalStateException("ClickHouse connection problems", e);
            }

            System.out.println("Registered batch with time: " + batchDTO.getTime());
        } else {
            throw new IllegalStateException("Token is invalid");
        }
        return new ResponseEntity<>(batchDTO.getTime()+"/"+batchDTO.getAuthorId(), HttpStatus.OK);
    }

    boolean isAlreadyWritten(BatchDTO batchDTO) {
        return entityForClickRepository.getBatchByTime(batchDTO.getTime(), batchDTO.getClickedKnopkaId(), batchDTO.getAuthorId()) != 0;

    }

    public List<Long> getTopByRegion(String token, String region) {
        KnopkaUser knopkaUserById = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(() -> {
            throw new IllegalStateException("Invalid token");
        });
        return entityForClickRepository.getTopByRegion(region);
    }

    public Set<String> getValidRegions(String token) {
        KnopkaUser knopkaUserById = knopkaUserRepository.findKnopkaUserByToken(token).orElseThrow(() -> {
            throw new IllegalStateException("Invalid token");
        });
        return entityForClickRepository.getRegions();
    }
}
