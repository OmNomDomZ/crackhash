package ru.rabetsky.crackhash.worker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rabetsky.crackhash.model.CrackHashManagerRequest;
import ru.rabetsky.crackhash.worker.service.CrackHashService;

@RestController
@RequestMapping("/internal/api/worker/hash/crack")
@RequiredArgsConstructor
@Slf4j
public class WorkerController {

    private final CrackHashService crackHashService;

    @PostMapping("/task")
    public ResponseEntity<Void> processTask(
            @RequestBody CrackHashManagerRequest request) {

        log.info("POST /internal/api/worker/hash/crack/task \n requestId={}",
                request.getRequestId());
        crackHashService.processTask(request);
        return ResponseEntity.ok().build();
    }
}
