package ru.rabetsky.crackhash.manager.service;

import org.springframework.stereotype.Component;
import ru.rabetsky.crackhash.manager.model.CrackHashInfo;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CrackHashStorage {

    private final ConcurrentHashMap<UUID, CrackHashInfo> cache = new ConcurrentHashMap<>();

    public void put(UUID uuid, CrackHashInfo crackHashInfo) {
        cache.put(uuid, crackHashInfo);
        crackHashInfo.startCountdown();
    }

    public CrackHashInfo get(UUID uuid) {
        return cache.get(uuid);
    }

    public CrackHashInfo remove(UUID uuid) {
        return cache.remove(uuid);
    }

}
