package ru.rabetsky.crackhash.manager.service;

import org.springframework.stereotype.Component;
import ru.rabetsky.crackhash.manager.model.CrackHashInfo;
import ru.rabetsky.crackhash.manager.model.Status;

import java.util.concurrent.ConcurrentHashMap;

@Component

public class CrackHashStorage {
    private ConcurrentHashMap<CrackHashInfo, Status> cache = new ConcurrentHashMap<>();

    public void add(CrackHashInfo crackHashInfo) {

    }

}
