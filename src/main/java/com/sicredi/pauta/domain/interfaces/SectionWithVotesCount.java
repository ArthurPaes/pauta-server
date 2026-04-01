package com.sicredi.pauta.domain.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(
    name = "SectionWithVotesCount",
    description = "Dados de uma pauta com contagem de votos",
    example = """
        {
            "id": 1,
            "name": "Pauta Importante",
            "description": "Esta é uma descrição detalhada da pauta que será votada pelos associados",
            "expiration": 10,
            "start_at": "2024-01-15T10:00:00",
            "totalVotes": 5,
            "votesTrue": 3,
            "votesFalse": 2,
            "hasVoted": true,
            "isExpired": false
        }
        """
)
public interface SectionWithVotesCount {

    @Schema(description = "ID único da pauta", example = "1")
    Long getId();

    @Schema(description = "Nome da pauta", example = "Pauta Importante")
    String getName();

    @Schema(description = "Descrição detalhada da pauta", example = "Esta é uma descrição detalhada da pauta que será votada pelos associados")
    String getDescription();

    @Schema(description = "Tempo de expiração em minutos", example = "10")
    Integer getExpiration();

    @Schema(description = "Data e hora de início da pauta", example = "2024-01-15T10:00:00")
    LocalDateTime getStart_at();

    @Schema(description = "Total de votos na pauta", example = "5")
    Long getTotalVotes();

    @Schema(description = "Número de votos a favor", example = "3")
    Long getVotesTrue();

    @Schema(description = "Número de votos contra", example = "2")
    Long getVotesFalse();

    @Schema(description = "Se o usuário já votou nesta pauta", example = "true")
    Boolean getHasVoted();

    @Schema(description = "Se a pauta está expirada", example = "false")
    Boolean getIsExpired();
}
