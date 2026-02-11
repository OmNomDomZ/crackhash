package ru.rabetsky.crackhash.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rabetsky.crackhash.manager.model.CrackHashInfo;
import ru.rabetsky.crackhash.manager.model.StatusResponse;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusService {
    private final CrackHashStorage crackHashStorage;

    public StatusResponse getStatus(UUID requestId) {
        CrackHashInfo crackHashInfo = crackHashStorage.get(requestId);
        log.info("Status request: requestId={}, status={}, data={}", requestId,
                crackHashInfo.getStatus(), crackHashInfo.getData());
        return StatusResponse.builder()
                .data(crackHashInfo.getData())
                .status(crackHashInfo.getStatus())
                .build();
    }
}
