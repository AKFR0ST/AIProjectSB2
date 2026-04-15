package com.sb2.dto;

import java.util.UUID;

public record CreateTaskResponse(
        UUID id,
        String status
) {}
