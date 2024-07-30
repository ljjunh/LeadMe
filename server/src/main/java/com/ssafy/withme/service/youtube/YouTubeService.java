package com.ssafy.withme.service.youtube;


import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.ssafy.withme.controller.youtube.request.YouTubeSearchRequest;
import com.ssafy.withme.controller.youtube.response.YouTubeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    @Value("${youtube.api.keys}")
    private List<String> apiKey;

    // api 호출이 동시에 여러개가 들어와서 순환이 안되는 경우를 방지
    private AtomicInteger curentKeyIndex = new AtomicInteger(0);

    /**
     * 유튜브 API 를 활용하여 영상 정보를 받아온다.
     * @param youTubeSearchRequest
     * @return
     * @throws IOException
     */
    public List<YouTubeSearchResponse> searchYouTube(YouTubeSearchRequest youTubeSearchRequest) throws IOException {
        List<YouTubeSearchResponse> youTubeSearchResponses = new ArrayList<>();

        // api 키를 가지고 시도한 횟수
        int attempts = 0;
        // 전체 api 키의 개수를 초과하지 않는다.
        while(attempts < apiKey.size()) {

            String apiKey = getNextApiKey();
            try {
                youTubeSearchResponses = callYoutubeApi(youTubeSearchRequest, apiKey);
                break;
            } catch(Exception e) {
                e.printStackTrace();
            }

            // 아토믹하게 1씩 증가시킨다.
            curentKeyIndex.incrementAndGet();
            attempts++;
        }

        return youTubeSearchResponses;
    }

    public List<YouTubeSearchResponse> callYoutubeApi(YouTubeSearchRequest youTubeSearchRequest, String apiKey) throws IOException {
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        YouTube youTube = new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                jsonFactory,
                request -> {})
                .build();

        YouTube.Search.List search = youTube.search().list(Collections.singletonList("id, snippet"));

        // 검색 조건 세팅
        search.setKey(apiKey);
        search.setPart(Collections.singletonList(youTubeSearchRequest.getPart()));
        search.setQ(youTubeSearchRequest.getQ());
        search.setType(Collections.singletonList(youTubeSearchRequest.getType()));
        search.setVideoDuration(youTubeSearchRequest.getVideoDuration());
        search.setMaxResults(youTubeSearchRequest.getMaxResults());
        search.setPageToken(youTubeSearchRequest.getPageToken());
        search.setVideoEmbeddable(youTubeSearchRequest.isVideoEmbeddable());
        search.setVideoSyndicated(youTubeSearchRequest.isVideoSyndicated());

        // 검색 조건을 실행시켜 결과값 반환받음
        SearchListResponse searchResponse = search.execute();
        String nextPageToken = searchResponse.getNextPageToken();
        java.util.List<SearchResult> searchResultList = searchResponse.getItems();

        // YouTubeSearchResponse 반환값 감싸는 과정
        List<YouTubeSearchResponse> searchResponseList = searchResultList.stream()
                .map(searchResult -> YouTubeSearchResponse.builder()
                        .videoId(searchResult.getId().getVideoId())
                        .nextPageToken(nextPageToken)
                        .snippet(searchResult.getSnippet())
                        .build())
                .collect(Collectors.toList());

        return searchResponseList;
    }

    public String getNextApiKey() {
        return apiKey.get( curentKeyIndex.get() % apiKey.size() );
    }


}
