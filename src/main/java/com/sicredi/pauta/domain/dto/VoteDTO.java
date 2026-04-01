package com.sicredi.pauta.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para registro de voto")
public record VoteDTO(
    @Schema(description = "ID da seção onde o voto será registrado", example = "1")
    @NotNull(message = "ID da seção é obrigatório")
    Long sectionId,

    @Schema(description = "ID do usuário que está votando", example = "1")
    @NotNull(message = "ID do usuário é obrigatório")
    Long userId,

    @Schema(description = "Voto do usuário (true = a favor, false = contra)", example = "true")
    @NotNull(message = "Voto é obrigatório")
    Boolean vote
) {}
