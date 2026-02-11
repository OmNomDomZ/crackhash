package ru.rabetsky.crackhash.manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rabetsky.crackhash.manager.model.CrackHashInfo;
import ru.rabetsky.crackhash.manager.model.CrackHashRequest;
import ru.rabetsky.crackhash.manager.model.CrackHashResponse;
import ru.rabetsky.crackhash.manager.model.Status;
import ru.rabetsky.crackhash.model.CrackHashManagerRequest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final WorkerClient workerClient;
    private final CrackHashStorage crackHashStorage;

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";

    @Value("${worker.count}")
    private int workerCount;

    @Value("${manager.timeout}")
    private Long timeout;

    public CrackHashResponse handleRequest(CrackHashRequest crackHashRequest) {
        UUID requestId = UUID.randomUUID();
        log.info("Received crack request: hash={}, maxLength={}, requestId={}",
                crackHashRequest.getHash(), crackHashRequest.getMaxLength(), requestId);

        List<String> alphabetList = Arrays.asList(ALPHABET.split(""));
        CrackHashManagerRequest.Alphabet alphabetObject = new CrackHashManagerRequest.Alphabet();
        alphabetObject.getSymbols().addAll(alphabetList);

        CrackHashInfo crackHashInfo = CrackHashInfo.builder()
                .timeout(timeout)
                .status(Status.IN_PROGRESS)
                .completedWorkerCount(0)
                .totalWorkerCount(workerCount)
                .build();
        crackHashStorage.put(requestId, crackHashInfo);

        for (int partNumber = 0; partNumber < workerCount; partNumber++) {

            CrackHashManagerRequest request = new CrackHashManagerRequest();
            request.setRequestId(requestId.toString());
            request.setHash(crackHashRequest.getHash());
            request.setMaxLength(crackHashRequest.getMaxLength());
            request.setPartNumber(partNumber);
            request.setPartCount(workerCount);
            request.setAlphabet(alphabetObject);

            workerClient.sendTaskToWorker(request);
        }

        log.info("Dispatched {} tasks for requestId={}", workerCount, requestId);
        return new CrackHashResponse(requestId);
    }
}
