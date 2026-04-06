package ru.rabetsky.crackhash.worker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rabetsky.crackhash.worker.service.CrackHashService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClientController {

    private final CrackHashService crackHashService;

    @GetMapping("/md5hash/{input}")
    public ResponseEntity<String> md5Hash(@PathVariable String input) {
        return ResponseEntity.ok(crackHashService.getMd5Hash(input));
    }
}
