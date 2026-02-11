package ru.rabetsky.crackhash.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rabetsky.crackhash.manager.model.CrackHashInfo;
import ru.rabetsky.crackhash.model.CrackHashWorkerResponse;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {

    private final CrackHashStorage crackHashStorage;

    public void handleWorkerResponse(CrackHashWorkerResponse response) {
        log.info("Received worker response: requestId={}, partNumber={}, words={}",
                response.getRequestId(), response.getPartNumber(),
                response.getAnswers() != null ? response.getAnswers().getWords() : "[]");

        CrackHashInfo crackHashInfo = crackHashStorage.get(UUID.fromString(response.getRequestId()));
        crackHashInfo.workerFinished(response.getAnswers());

        log.info("Request {} progress: {}/{} workers done, status={}",
                response.getRequestId(), crackHashInfo.getCompletedWorkerCount(),
                crackHashInfo.getTotalWorkerCount(), crackHashInfo.getStatus());
    }
}
