package ru.rabetsky.crackhash.manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.rabetsky.crackhash.manager.model.CrackHashRequest;
import ru.rabetsky.crackhash.manager.model.CrackHashResponse;
import ru.rabetsky.crackhash.manager.service.ManagerService;

import java.util.UUID;

@RestController
@RequestMapping("api/hash")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/crack")
    public CrackHashResponse crack(CrackHashRequest crackHashRequest) {
        return managerService.handleRequest(crackHashRequest);
    }

    @GetMapping("/status/{requestId}")
    public void getStatus(@PathVariable("requestId") UUID requestId) {

    }
}
