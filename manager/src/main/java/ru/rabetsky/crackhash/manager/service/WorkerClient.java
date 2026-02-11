package ru.rabetsky.crackhash.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rabetsky.crackhash.manager.config.WorkerConfig;
import ru.rabetsky.crackhash.model.CrackHashManagerRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkerClient {

    private final RestTemplate restTemplate;
    private final WorkerConfig workerConfig;

    public void sendTaskToWorker(CrackHashManagerRequest crackHashManagerRequest) {

        int partNumber = crackHashManagerRequest.getPartNumber();
        String workerUrl = workerConfig.getUrls().get(partNumber %  workerConfig.getUrls().size());

        String url = workerUrl + "/internal/api/worker/hash/crack/task";

        log.info("Sending request to worker: requestId={}, part number={}",
                crackHashManagerRequest.getRequestId(),
                crackHashManagerRequest.getPartNumber());

        try {
            restTemplate.postForEntity(url, crackHashManagerRequest, Void.class);
            log.info("Request successfully sent");
        } catch (Exception e) {
            log.error("Failed to send request to worker: {}", e.getMessage(), e);
        }
    }
}
