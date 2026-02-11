package ru.rabetsky.crackhash.worker.service;

import lombok.RequiredArgsConstructor;
import org.paukov.combinatorics3.Generator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.rabetsky.crackhash.model.CrackHashManagerRequest;
import ru.rabetsky.crackhash.model.CrackHashWorkerResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrackHashService {

    private final ManagerClient managerClient;

    @Async
    public void processTask(CrackHashManagerRequest request) {
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

            Generator.combination(alphabet.toArray(new String[0]))
                    .multi(len)
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

        managerClient.sendResponseToManager(response);
    }

    private long getCountCombinationsWithRep(int n, int k) {
        return binomial(n + k - 1, k);
    }

    private long binomial(int n, int k) {
        if (k > n) {
            return 0;
        }
        if (k == 0 || k == n) {
            return 1;
        }
        if (k > n - k) {
            k = n - k;
        }
        long result = 1;
        for (int i = 0; i < k; i++) {
            result = result * (n - i) / (i + 1);
        }
        return result;
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
}
