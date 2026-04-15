package com.sb2.dto;

import java.util.UUID;

public record TaskResponse(
        UUID id,
        String fileName,
        String status,
        String extractedData,
        String error
) {}
