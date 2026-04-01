package com.sicredi.pauta.repository;

import com.sicredi.pauta.domain.model.Votes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotesRepository extends JpaRepository<Votes, Long> {

    // find an existing vote by user + section — used to prevent double voting
    Optional<Votes> findByUserIdAndSectionId(Long userId, Long sectionId);
}
