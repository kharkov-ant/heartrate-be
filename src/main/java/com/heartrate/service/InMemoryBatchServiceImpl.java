package com.heartrate.service;

import org.springframework.stereotype.Service;

import com.heartrate.InMemorySettings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class InMemoryBatchServiceImpl implements BatchService {

    private final InMemorySettings inMemorySettings;

    @Override
    public void processMessage(String message) {
        inMemorySettings.getMessageBuffer().add(message);

        if (inMemorySettings.getMessageBuffer().size() >= inMemorySettings.getBatchSize()) {
            persistBatch();
        }
    }

    private void persistBatch() {
        // Logic to persist the batch to the database
        // ...
        log.info("Persist message {} ", inMemorySettings.getMessageBuffer());

        // Clear the buffer after persisting
        inMemorySettings.getMessageBuffer().clear();
    }
}
