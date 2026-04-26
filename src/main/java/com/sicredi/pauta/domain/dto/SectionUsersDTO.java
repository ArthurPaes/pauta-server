package com.sicredi.pauta.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Lista de usuários habilitados para votar na pauta")
public record SectionUsersDTO(
    @NotNull(message = "Lista de usuários é obrigatória")
    List<Long> userIds
) {}
