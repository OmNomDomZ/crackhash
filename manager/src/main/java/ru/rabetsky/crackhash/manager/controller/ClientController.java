package ru.rabetsky.crackhash.manager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rabetsky.crackhash.manager.model.CrackHashRequest;
import ru.rabetsky.crackhash.manager.model.CrackHashResponse;
import ru.rabetsky.crackhash.manager.model.StatusResponse;
import ru.rabetsky.crackhash.manager.service.ClientService;
import ru.rabetsky.crackhash.manager.service.StatusService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/hash")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final StatusService statusService;

    @PostMapping("/crack")
    public ResponseEntity<CrackHashResponse> crack(CrackHashRequest crackHashRequest) {
        log.info("POST /api/hash/crack \n hash={}, maxLength={}",
                crackHashRequest.getHash(), crackHashRequest.getMaxLength());
        return ResponseEntity.ok(clientService.handleRequest(crackHashRequest));
    }

    @GetMapping("/status/{requestId}")
    public ResponseEntity<StatusResponse> getStatus(@PathVariable("requestId") UUID requestId) {
        log.info("GET /api/hash/status/{}", requestId);
        return ResponseEntity.ok(statusService.getStatus(requestId));
    }
}
