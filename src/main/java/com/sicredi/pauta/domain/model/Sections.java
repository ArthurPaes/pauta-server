package com.sicredi.pauta.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sections")
@Schema(
    name = "Sections",
    description = "Entidade de pauta para votação",
    example = """
        {
            "id": 1,
            "name": "Pauta Importante",
            "description": "Esta é uma descrição detalhada da pauta que será votada pelos associados",
            "expiration": 10,
            "start_at": "2024-01-15T10:00:00"
        }
        """
)
public class Sections {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Integer expiration;
    
    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "owner_id")
    private Long ownerId;

    @ElementCollection
    @CollectionTable(name = "section_users", joinColumns = @JoinColumn(name = "section_id"))
    @Column(name = "user_id")
    private Set<Long> allowedUserIds = new HashSet<>();
}
