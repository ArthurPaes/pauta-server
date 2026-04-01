package com.sicredi.pauta.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de pauta")
public record SectionDTO(
    @Schema(description = "Nome da pauta", example = "Pauta Importante")
    @NotBlank(message = "Nome da seção é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    String name,

    @Schema(description = "Descrição detalhada da pauta", example = "Esta é uma descrição detalhada da pauta que será votada pelos associados")
    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "Descrição deve ter entre 10 e 1000 caracteres")
    String description,

    @Schema(description = "Tempo de expiração em minutos", example = "10")
    @NotNull(message = "Tempo de expiração é obrigatório")
    @Min(value = 1, message = "Tempo de expiração deve ser pelo menos 1 minuto")
    Integer expiration
) {}
