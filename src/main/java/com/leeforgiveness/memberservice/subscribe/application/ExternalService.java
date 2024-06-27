package com.leeforgiveness.memberservice.subscribe.application;

import static com.leeforgiveness.memberservice.common.ServerPath.INFLUENCER_SUMMARISE;

import com.leeforgiveness.memberservice.subscribe.dto.InfluencerSummaryDto;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class ExternalService {

    public List<InfluencerSummaryDto> getInfluencerSummarise(String authorization,
        List<String> influencerUuids) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(INFLUENCER_SUMMARISE.getUrl())
                .queryParam("influencerUuids", influencerUuids)
                .toUriString();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", authorization)
                .GET()
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            String responseBody = response.body();
            JSONArray jsonArray = new JSONArray(responseBody);

            List<InfluencerSummaryDto> influencerSummaries = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                InfluencerSummaryDto influencerSummary = InfluencerSummaryDto.fromJson(jsonObject);
                influencerSummaries.add(influencerSummary);
            }
            log.info("Influencer summaries: {}", influencerSummaries);
            return influencerSummaries;
        } catch (InterruptedException | IOException | RuntimeException e) {
            log.info("getInfluencerSummarise error: {}", e.getMessage());
            return null;
        }
    }
}
