package com.sicredi.pauta.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "VoteStatus",
    description = "Status de um voto",
    example = "ABLE_TO_VOTE"
)
public enum VoteStatus {
    ABLE_TO_VOTE,
    UNABLE_TO_VOTE
}
