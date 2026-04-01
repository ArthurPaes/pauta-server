package com.sicredi.pauta.service;

import com.sicredi.pauta.domain.dto.SectionDTO;
import com.sicredi.pauta.domain.interfaces.SectionWithVotesCount;
import com.sicredi.pauta.domain.model.Sections;
import com.sicredi.pauta.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public List<SectionWithVotesCount> getAllSectionsWithVotes(Long userId) {
        log.debug("Buscando todas as seções com contagem de votos para o usuário: {}", userId);
        List<SectionWithVotesCount> sections = sectionRepository.findAllWithVotesCount(userId);
        log.debug("Encontradas {} seções para o usuário: {}", sections.size(), userId);
        return sections;
    }

    public Sections createSection(SectionDTO sectionDTO) {
        log.debug("Criando nova seção: {}", sectionDTO.name());

        Sections section = new Sections();
        section.setName(sectionDTO.name());
        section.setDescription(sectionDTO.description());
        section.setExpiration(sectionDTO.expiration());
        section.setStartAt(LocalDateTime.now());

        Sections saved = sectionRepository.save(section);
        log.debug("Seção salva com sucesso. ID: {}, Nome: {}", saved.getId(), saved.getName());
        return saved;
    }
}
