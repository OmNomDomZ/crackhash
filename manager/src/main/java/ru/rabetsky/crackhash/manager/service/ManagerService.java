package ru.rabetsky.crackhash.manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rabetsky.crackhash.manager.model.CrackHashRequest;
import ru.rabetsky.crackhash.manager.model.CrackHashResponse;
import ru.rabetsky.crackhash.model.CrackHashManagerRequest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final WorkerClient workerClient;
    private final CrackHashStorage crackHashStorage;

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";

    @Value("${worker.count}")
    private int workerCount;

    public CrackHashResponse handleRequest(CrackHashRequest crackHashRequest) {
        UUID requestId = UUID.randomUUID();

        List<String> alphabetList = Arrays.asList(ALPHABET.split(""));
        CrackHashManagerRequest.Alphabet alphabetObject = new CrackHashManagerRequest.Alphabet();
        alphabetObject.getSymbols().addAll(alphabetList);

//        crackHashStorage.add();

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

        return new CrackHashResponse(requestId);
    }
}
