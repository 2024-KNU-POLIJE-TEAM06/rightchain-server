package com.example.rightchain.wallet.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlockSDKApi {

    @Value("${spring.chain.api-token}")
    private String API_TOKEN;

    private final String blockchainUrl = "https://testnet-api.blocksdk.com/v3/eth";

    public String createWallet(String walletName) {
        String url = blockchainUrl + "/address/?api_token=" + API_TOKEN;
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", walletName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            log.info("error " + response);
        }

        Map payload = (Map) Objects.requireNonNull(response.getBody()).get("payload");
        log.info("Wallet created successfully with address: {}", payload.get("address"));

        return (String) payload.get("address");
    }

    /**
     * 지갑 주소를 사용해 지갑 정보를 조회하는 메서드
     * @param address 지갑 주소
     * @return 지갑의 기본 정보 (예: 잔액, 이름 등)
     */
    public Map<String, Object> readWallet(String address) {
        String url = blockchainUrl + "/address/" + address + "/info?api_token=" + API_TOKEN;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK || !response.hasBody()) {
            log.error("Failed to retrieve wallet information for address: {}", address);
            throw new IllegalStateException("Failed to retrieve wallet information");
        }

        Map<String, Object> walletInfo = (Map<String, Object>) response.getBody().get("payload");
        log.info("Retrieved wallet info for address {}: {}", address, walletInfo);

        return walletInfo;
    }

    /**
     * transaction transmission
     * we have to change data to hex value
     *
    public String sendTransaction(String walletAddress, String toAddress, String hexValue) throws Exception {
        String url = blockchainUrl + "/transaction/send?api_token=" + API_TOKEN;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // 전송할 트랜잭션 데이터 구성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("from", walletAddress);
        requestBody.put("to", toAddress);
        requestBody.put("hex", hexValue);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 트랜잭션 전송
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        if (response.getStatusCode() != HttpStatus.OK || !response.hasBody()) {
            throw new IllegalStateException("Failed to send transaction: " + response);
        }

        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> payload = (Map<String, Object>) responseBody.get("payload");
        return (String) payload.get("hash"); // 트랜잭션 해시 반환
    }
    */

}
