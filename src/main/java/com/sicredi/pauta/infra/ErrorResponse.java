package com.sicredi.pauta.infra;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String message;
    private String error;
    private LocalDateTime timestamp;
    private String path;

    public ErrorResponse(String message, String error, String path) {
        this.message = message;
        this.error = error;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
