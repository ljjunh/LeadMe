package com.ssafy.withme.domain.report;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "reports")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Report {

    @Id
    private String uuid;

    private Long userChallengeId;

    private Double totalScore;

    private double[] scoreHistory;

    @Builder
    private Report(String uuid, Double totalScore, double[] scoreHistory) {
        this.uuid = uuid;
        this.totalScore = totalScore;
        this.scoreHistory = scoreHistory;
    }
}
