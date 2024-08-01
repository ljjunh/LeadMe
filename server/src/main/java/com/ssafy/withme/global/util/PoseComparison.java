package com.ssafy.withme.global.util;

import com.ssafy.withme.global.response.Frame;
import com.ssafy.withme.global.response.Keypoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 유클리드 정규화 및 코사인 유사도 측정을 진행하는 클래스
 */
public class PoseComparison {
    // L2 정규화 학습
    public static List<Keypoint> l2Normalize(List<Keypoint> pose) {
        double sumOfSquare = 0;

        // 입부터 진행
        for(int i = 7; i < pose.size(); i++) {
            Keypoint kp = pose.get(i);
            // 신뢰도 0.3 미만은 0으로 대체
            if(kp.getVisibility() < 0.3) {
                kp.setX(0);
                kp.setY(0);
                kp.setZ(0);
            }

            sumOfSquare += kp.getX() * kp.getX() + kp.getY() * kp.getY() + kp.getZ() * kp.getZ();
        }
        double l2Norm = Math.sqrt(sumOfSquare);

        List<Keypoint> normalized = new ArrayList<Keypoint>();

        // 입부터 포함시킨 정규화 값
        for(int i = 7; i < pose.size(); i++) {
            Keypoint kp = pose.get(i);
            normalized.add(new Keypoint(
                    kp.getX() / l2Norm,
                    kp.getY() / l2Norm,
                    kp.getZ()/ l2Norm
            ));
        }

        return normalized;
    }

    // 코사인 유사도 계산 함수
    public static double cosinSimilarity(List<Keypoint> pose1, List<Keypoint> pose2) {

        double dotProduct = 0.0;
        for(int i = 0; i < pose1.size(); i++) {
            Keypoint kp1 = pose1.get(i);
            Keypoint kp2 = pose2.get(i);
            dotProduct += kp1.getX() * kp2.getX() + kp1.getY() * kp2.getY() + kp1.getZ() * kp2.getZ();
        }

        // 정규화를 사전에 수행했기 때문에 분모인 두 벡터의 크기는 1*1이다.
        return (dotProduct);
    }

    // 평균 점수 게산
    public static double calculatePoseScore(List<Frame> userVideoFrames, List<Frame> challengeFrames) {
        double totalScore = 0.0;
        int totalFrameCount = challengeFrames.size();
        int userVideoFrameCount = userVideoFrames.size();

        // 기존 로직 유저영상의 길이를 기준으로 챌린지도 함께 돌린다?

        // 유저 영상 프레임 수가 챌린지 프레임 수보다 적거나 같다면
        if(totalFrameCount >= userVideoFrameCount) {
            // 유저의 영상의 길이를 기준으로 한다.
            for(int i = 0; i < userVideoFrameCount; i++) {

                Frame userFrame = userVideoFrames.get(i);
                Frame challengeFrame = challengeFrames.get(i);

                if(userFrame == null) continue;

                // 정규화과정을 거친 관절 keypoints를 구함
                List<Keypoint> userL2Keypoints = l2Normalize(userFrame.getKeypoints());
                List<Keypoint> userL2ChallengeKeypoints = l2Normalize(challengeFrame.getKeypoints());

                // 코사인 유사도 측정 진행
                totalScore += cosinSimilarity(userL2Keypoints, userL2ChallengeKeypoints);

            }


        } else { // 유저 영상 프레임 수가 챌린지 프레임 수보다 크다면
            // 챌린지 영상의 길이를 기준으로 한다.
            for(int i = 0; i < totalFrameCount; i++) {
                Frame userFrame = userVideoFrames.get(i);
                Frame challengeFrame = challengeFrames.get(i);

                if(userFrame == null) continue;

                // 정규화과정을 거친 관절 keypoints를 구함
                List<Keypoint> userL2Keypoints = l2Normalize(userFrame.getKeypoints());
                List<Keypoint> userL2ChallengeKeypoints = l2Normalize(challengeFrame.getKeypoints());

                // 코사인 유사도 측정 진행
                totalScore += cosinSimilarity(userL2Keypoints, userL2ChallengeKeypoints);
            }
        }

        // 원본 프레임 기준으로 평균을 나눔
        totalScore = totalScore / totalFrameCount;
        return totalScore;
    }
}
