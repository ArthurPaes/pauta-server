package com.sicredi.pauta.repository;

import com.sicredi.pauta.domain.interfaces.SectionWithVotesCount;
import com.sicredi.pauta.domain.model.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends JpaRepository<Sections, Long> {

    @Query(value =
            "SELECT s.id, s.name, s.description, s.expiration, s.start_at, s.owner_id as ownerId, " +
            "(SELECT COUNT(v.id) FROM votes v WHERE v.section_id = s.id) as totalVotes, " +
            "(SELECT COUNT(v.id) FROM votes v WHERE v.section_id = s.id AND v.vote = true) as votesTrue, " +
            "(SELECT COUNT(v.id) FROM votes v WHERE v.section_id = s.id AND v.vote = false) as votesFalse, " +
            "(CASE WHEN EXISTS (SELECT 1 FROM votes v WHERE v.section_id = s.id AND v.user_id = :userId) THEN true ELSE false END) as hasVoted, " +
            "(CASE WHEN NOW() > (s.start_at + (s.expiration * INTERVAL '1 minute')) THEN true ELSE false END) as isExpired " +
            "FROM sections s", nativeQuery = true)
    List<SectionWithVotesCount> findAllWithVotesCount(@Param("userId") Long userId);

    @Query(value = "SELECT section_id, user_id FROM section_users", nativeQuery = true)
    List<Object[]> findAllSectionUsers();
}
