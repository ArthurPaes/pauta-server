package com.sicredi.pauta.repository;

import com.sicredi.pauta.domain.interfaces.SectionWithVotesCount;
import com.sicredi.pauta.domain.model.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends JpaRepository<Sections, Long> {

    @Query(value =
            // select all columns from sections plus the computed columns below
            "SELECT s.*, " +

            // count all votes cast for this section
            "(SELECT COUNT(v.id) FROM votes v WHERE v.section_id = s.id) as totalVotes, " +

            // count only votes where vote = true (Sim)
            "(SELECT COUNT(v.id) FROM votes v WHERE v.section_id = s.id AND v.vote = true) as votesTrue, " +

            // count only votes where vote = false (Não)
            "(SELECT COUNT(v.id) FROM votes v WHERE v.section_id = s.id AND v.vote = false) as votesFalse, " +

            // returns true if the given user has already voted on this section
            "(CASE WHEN EXISTS (SELECT 1 FROM votes v WHERE v.section_id = s.id AND v.user_id = :user_id) THEN true ELSE false END) as hasVoted, " +

            // returns true if the current time is past start_at + expiration minutes (session closed)
            "(CASE WHEN NOW() > (s.start_at + (s.expiration * INTERVAL '1 minute')) THEN true ELSE false END) as isExpired " +

            "FROM sections s", nativeQuery = true)
    List<SectionWithVotesCount> findAllWithVotesCount(@Param("user_id") Long user_id);
}
