package ru.rabetsky.crackhash.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.paukov.combinatorics3.Generator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.rabetsky.crackhash.model.CrackHashManagerRequest;
import ru.rabetsky.crackhash.model.CrackHashWorkerResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrackHashService {

    private final ManagerClient managerClient;

    @Async
    public void processTask(CrackHashManagerRequest request) {
        log.info("Starting task: requestId={}, part {}/{}, hash={}, maxLength={}",
                request.getRequestId(), request.getPartNumber(), request.getPartCount(),
                request.getHash(), request.getMaxLength());

        String hash = request.getHash();
        List<String> alphabet = request.getAlphabet().getSymbols();
        int maxLength = request.getMaxLength();

        long total = 0;
        for (int i = 1; i <= maxLength; i++) {
            total += getCountCombinationsWithRep(alphabet.size(), i);
        }

        long partNumber = request.getPartNumber();
        long partCount = request.getPartCount();

        long combinationsForPart = total / partCount;
        long remainder = total % partCount;
        long start = partNumber * combinationsForPart + Math.min(partNumber, remainder);
        long size = combinationsForPart + (partNumber < remainder ? 1 : 0);
        long end = start + size;

        List<String> found = new ArrayList<>();
        long globalIdx = 0;

        for (int len = 1; len <= maxLength; len++) {
            long count = getCountCombinationsWithRep(alphabet.size(), len);

            if (globalIdx + count <= start) {
                globalIdx += count;
                continue;
            }

            if (globalIdx >= end) {
                break;
            }

            long skip = Math.max(0, start - globalIdx);
            long take = Math.min(count - skip, end - globalIdx - skip);

            Generator.permutation(alphabet.toArray(new String[0]))
                    .withRepetitions(len)
                    .stream()
                    .skip(skip)
                    .limit(take)
                    .forEach(combo -> {
                        String word = String.join("", combo);
                        if (md5Hash(word).equalsIgnoreCase(hash)) {
                            found.add(word);
                        }
                    });

            globalIdx += count;
        }

        CrackHashWorkerResponse response = new CrackHashWorkerResponse();
        response.setRequestId(request.getRequestId());
        response.setPartNumber((int) partNumber);
        CrackHashWorkerResponse.Answers answers = new CrackHashWorkerResponse.Answers();
        answers.getWords().addAll(found);
        response.setAnswers(answers);

        log.info("Task finished: requestId={}, part {}/{}, found {} words: {}",
                request.getRequestId(), request.getPartNumber(), request.getPartCount(),
                found.size(), found);

        managerClient.sendResponseToManager(response);
    }

    private long getCountCombinationsWithRep(int n, int k) {
        return (long) Math.pow(n, k);
    }

    private String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMd5Hash(String input) {
        return md5Hash(input);
    }
}
