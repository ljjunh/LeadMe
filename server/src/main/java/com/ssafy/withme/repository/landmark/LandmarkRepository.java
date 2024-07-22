package com.ssafy.withme.repository.landmark;

import com.ssafy.withme.domain.landmark.Landmark;
import com.ssafy.withme.global.error.ErrorCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface LandmarkRepository extends MongoRepository<Landmark, String> {

    Landmark findByYoutubeId(String youtubeId);
}
