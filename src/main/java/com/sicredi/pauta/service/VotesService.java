package com.sicredi.pauta.service;

import com.sicredi.pauta.domain.dto.VoteDTO;
import com.sicredi.pauta.domain.model.Sections;
import com.sicredi.pauta.domain.model.VoteStatus;
import com.sicredi.pauta.domain.model.Votes;
import com.sicredi.pauta.repository.SectionRepository;
import com.sicredi.pauta.repository.VotesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotesService {

    private final VotesRepository votesRepository;
    private final SectionRepository sectionRepository;

    public Votes createVote(VoteDTO voteDTO) {
        log.info("Processando voto. Usuário: {}, Seção: {}, Voto: {}",
                voteDTO.userId(), voteDTO.sectionId(), voteDTO.vote());

        validateSection(voteDTO.sectionId());

        if (!isValidCPF()) {
            log.warn("CPF inválido para usuário: {}", voteDTO.userId());
            Votes rejectedVote = new Votes();
            rejectedVote.setUserId(voteDTO.userId());
            rejectedVote.setSectionId(voteDTO.sectionId());
            rejectedVote.setVote(voteDTO.vote());
            rejectedVote.setStatus(VoteStatus.UNABLE_TO_VOTE);
            return rejectedVote;
        }

        votesRepository.findByUserIdAndSectionId(voteDTO.userId(), voteDTO.sectionId())
                .ifPresent(v -> {
                    log.warn("Voto duplicado. Usuário: {}, Seção: {}", voteDTO.userId(), voteDTO.sectionId());
                    throw new IllegalArgumentException("Esse usuário já votou nesta seção.");
                });

        Votes vote = new Votes();
        vote.setUserId(voteDTO.userId());
        vote.setSectionId(voteDTO.sectionId());
        vote.setVote(voteDTO.vote());
        vote.setStatus(VoteStatus.ABLE_TO_VOTE);

        Votes saved = votesRepository.save(vote);
        log.info("Voto registrado com sucesso. ID: {}, Usuário: {}, Seção: {}",
                saved.getId(), saved.getUserId(), saved.getSectionId());
        return saved;
    }

    private void validateSection(Long sectionId) {
        Sections section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> {
                    log.warn("Seção inexistente: {}", sectionId);
                    return new IllegalArgumentException("Seção não encontrada");
                });

        LocalDateTime expiresAt = section.getStartAt().plusMinutes(section.getExpiration());
        if (LocalDateTime.now().isAfter(expiresAt)) {
            log.warn("Seção expirada: {} (expirou em: {})", sectionId, expiresAt);
            throw new IllegalArgumentException("Seção expirada");
        }

        log.debug("Seção válida: {} (expira em: {})", sectionId, expiresAt);
    }

    // retorna falso 30% das vezes para simular CPFs inválidos
    private boolean isValidCPF() {
        return new Random().nextDouble() > 0.3;
    }
}
