package com.his.main.repositories.mongo;

import com.his.main.entities.mongo.ReportPayload;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReportPayloadRepo  extends MongoRepository<ReportPayload, String> {
    Optional<ReportPayload> findByJobKey(String jobId);
}
