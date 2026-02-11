package ru.rabetsky.crackhash.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rabetsky.crackhash.model.CrackHashWorkerResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerClient {

    @Value("${manager.url}")
    private String managerUrl;

    private final RestTemplate restTemplate;

    public void sendResponseToManager(CrackHashWorkerResponse response) {
        String url = managerUrl + "/internal/api/manager/hash/crack/request";;

        log.info("Sending result to manager: requestId={}, found {} words",
                response.getRequestId(),
                response.getAnswers().getWords().size());

        try {
            restTemplate.patchForObject(url, response, Void.class);
            log.info("Result sent successfully");
        } catch (Exception e) {
            log.error("Failed to send result to manager: {}", e.getMessage(), e);
        }
    }
}
