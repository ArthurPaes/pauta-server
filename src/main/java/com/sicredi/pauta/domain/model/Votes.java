package com.sicredi.pauta.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "votes")
@Schema(
    name = "Votes",
    description = "Entidade de voto em uma pauta",
    example = """
        {
            "id": 1,
            "sectionId": 1,
            "userId": 1,
            "vote": true,
            "status": "ABLE_TO_VOTE"
        }
        """
)
public class Votes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_id", nullable = false)
    private Long sectionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Boolean vote;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteStatus status;
}
