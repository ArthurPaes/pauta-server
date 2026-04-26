package com.sicredi.pauta.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Dados de uma pauta com contagem de votos e usuários habilitados")
public record SectionResponseDTO(
    Long id,
    String name,
    String description,
    Integer expiration,
    LocalDateTime startAt,
    Long totalVotes,
    Long votesTrue,
    Long votesFalse,
    Boolean hasVoted,
    Boolean isExpired,
    Long ownerId,
    List<Long> allowedUsers
) {}
