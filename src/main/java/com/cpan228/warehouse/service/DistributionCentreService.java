package com.cpan228.warehouse.service;

import com.cpan228.warehouse.config.DistributionCentreProperties;
import com.cpan228.warehouse.dto.DistributionCentre;
import com.cpan228.warehouse.model.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionCentreService {
    private final RestTemplate restTemplate;
    private final DistributionCentreProperties properties;

    public List<DistributionCentre> getAllDistributionCentres() {
        try {
            ResponseEntity<List<DistributionCentre>> response = restTemplate.exchange(
                getDistributionCentreUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DistributionCentre>>() {}
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.error("Error fetching distribution centres: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    public List<DistributionCentre> findCentresWithItem(String name, Item.Brand brand) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("name", name);
            requestBody.put("brand", brand);
            requestBody.put("requestingLatitude", properties.getLatitude());
            requestBody.put("requestingLongitude", properties.getLongitude());

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody);

            ResponseEntity<List<DistributionCentre>> response = restTemplate.exchange(
                getDistributionCentreUrl() + "/request-item",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<DistributionCentre>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.error("Error searching for items: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    public Optional<Item> requestItem(Long centreId, Item.Brand brand, String name, int quantity) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("brand", brand);
            requestBody.put("name", name);
            requestBody.put("quantity", quantity);
            requestBody.put("requestingLatitude", properties.getLatitude());
            requestBody.put("requestingLongitude", properties.getLongitude());
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody);
            
            ResponseEntity<Item> response = restTemplate.postForEntity(
                getDistributionCentreUrl() + "/" + centreId + "/request-item",
                requestEntity,
                Item.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            log.error("Authentication failed when requesting item from Distribution Centre");
        } catch (Exception e) {
            log.error("Error requesting item from Distribution Centre: {}", e.getMessage());
            log.debug("Detailed error:", e);
        }
        return Optional.empty();
    }

    public DistributionCentre findNearestCentreWithItem(String name, Item.Brand brand) {
        List<DistributionCentre> centres = findCentresWithItem(name, brand);
        if (centres.isEmpty()) {
            throw new RuntimeException("No distribution centres found with the requested item");
        }
        return centres.get(0); // The list is already sorted by distance
    }

    private String getDistributionCentreUrl() {
        return properties.getUrl() + "/api/distribution-centres";
    }
}