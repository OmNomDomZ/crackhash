package ru.rabetsky.crackhash.manager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rabetsky.crackhash.manager.service.ManagerService;
import ru.rabetsky.crackhash.model.CrackHashWorkerResponse;

@Slf4j
@RestController
@RequestMapping("/internal/api/manager/hash/crack")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PatchMapping("/request")
    public ResponseEntity<Void> handleWorkerResponse(@RequestBody CrackHashWorkerResponse crackHashWorkerResponse) {
        log.info("PATCH /internal/api/manager/hash/crack/request \n requestId={}, partNumber={}",
                crackHashWorkerResponse.getRequestId(), crackHashWorkerResponse.getPartNumber());
        managerService.handleWorkerResponse(crackHashWorkerResponse);

        return ResponseEntity.ok().build();
    }

}
