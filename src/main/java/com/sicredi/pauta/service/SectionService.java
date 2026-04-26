package com.sicredi.pauta.service;

import com.sicredi.pauta.domain.dto.SectionDTO;
import com.sicredi.pauta.domain.dto.SectionResponseDTO;
import com.sicredi.pauta.domain.dto.SectionUsersDTO;
import com.sicredi.pauta.domain.dto.UpdateSectionDTO;
import com.sicredi.pauta.domain.model.Sections;
import com.sicredi.pauta.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public List<SectionResponseDTO> getAllSectionsWithVotes(Long userId) {
        var sections = sectionRepository.findAllWithVotesCount(userId);

        Map<Long, List<Long>> allowedMap = new HashMap<>();
        for (Object[] row : sectionRepository.findAllSectionUsers()) {
            Long sectionId = ((Number) row[0]).longValue();
            Long allowedUserId = ((Number) row[1]).longValue();
            allowedMap.computeIfAbsent(sectionId, k -> new ArrayList<>()).add(allowedUserId);
        }

        return sections.stream().map(s -> new SectionResponseDTO(
            s.getId(),
            s.getName(),
            s.getDescription(),
            s.getExpiration(),
            s.getStart_at(),
            s.getTotalVotes(),
            s.getVotesTrue(),
            s.getVotesFalse(),
            s.getHasVoted(),
            s.getIsExpired(),
            s.getOwnerId(),
            allowedMap.getOrDefault(s.getId(), List.of())
        )).toList();
    }

    public Sections createSection(SectionDTO sectionDTO) {
        Sections section = new Sections();
        section.setName(sectionDTO.name());
        section.setDescription(sectionDTO.description());
        section.setExpiration(sectionDTO.expiration());
        section.setStartAt(LocalDateTime.now());
        section.setOwnerId(sectionDTO.userId());
        return sectionRepository.save(section);
    }

    public Sections updateSection(Long id, UpdateSectionDTO dto) {
        Sections section = sectionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));
        if (dto.name() != null) section.setName(dto.name());
        if (dto.description() != null) section.setDescription(dto.description());
        if (dto.expiration() != null) section.setExpiration(dto.expiration());
        return sectionRepository.save(section);
    }

    public void deleteSection(Long id) {
        if (!sectionRepository.existsById(id)) {
            throw new IllegalArgumentException("Pauta não encontrada");
        }
        sectionRepository.deleteById(id);
    }

    public Sections updateSectionUsers(Long id, SectionUsersDTO dto) {
        Sections section = sectionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));
        section.setAllowedUserIds(new HashSet<>(dto.userIds()));
        return sectionRepository.save(section);
    }
}
