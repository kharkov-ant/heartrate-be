package com.heartrate;

import java.util.List;

import lombok.Data;

@Data
public class InMemorySettings {
    
    private final int batchSize;
    private final List<String> messageBuffer;

}
