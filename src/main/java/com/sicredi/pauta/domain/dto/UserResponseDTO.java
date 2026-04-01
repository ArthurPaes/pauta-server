package com.sicredi.pauta.domain.dto;

public record UserResponseDTO(
    Long id,
    String name,
    String cpf,
    String email
) {}
