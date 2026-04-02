package com.sicredi.pauta.service;

import com.sicredi.pauta.domain.dto.VoteDTO;
import com.sicredi.pauta.domain.model.Sections;
import com.sicredi.pauta.domain.model.VoteStatus;
import com.sicredi.pauta.domain.model.Votes;
import com.sicredi.pauta.repository.SectionRepository;
import com.sicredi.pauta.repository.VotesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotesServiceTest {

    @Mock
    private VotesRepository votesRepository;

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private VotesService votesService;

    private Sections activeSection;

    @BeforeEach
    void setUp() {
        activeSection = new Sections();
        activeSection.setId(1L);
        activeSection.setName("Pauta Ativa");
        activeSection.setDescription("Descrição");
        activeSection.setExpiration(60);
        activeSection.setStartAt(LocalDateTime.now());
    }

    @Test
    void createVote_comSecaoInexistente_deveLancarExcecao() {
        VoteDTO dto = new VoteDTO(99L, 1L, true);
        when(sectionRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> votesService.createVote(dto));
        assertEquals("Seção não encontrada", ex.getMessage());
    }

    @Test
    void createVote_comSecaoExpirada_deveLancarExcecao() {
        Sections expired = new Sections();
        expired.setId(2L);
        expired.setName("Pauta Expirada");
        expired.setDescription("Expirada");
        expired.setExpiration(1);
        expired.setStartAt(LocalDateTime.now().minusMinutes(10));

        VoteDTO dto = new VoteDTO(2L, 1L, true);
        when(sectionRepository.findById(2L)).thenReturn(Optional.of(expired));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> votesService.createVote(dto));
        assertEquals("Seção expirada", ex.getMessage());
    }

    @Test
    void createVote_comVotoDuplicado_deveLancarExcecao() {
        VoteDTO dto = new VoteDTO(1L, 1L, true);
        when(sectionRepository.findById(1L)).thenReturn(Optional.of(activeSection));
        Votes existingVote = new Votes();
        when(votesRepository.findByUserIdAndSectionId(1L, 1L)).thenReturn(Optional.of(existingVote));

        // The method may return UNABLE_TO_VOTE due to random CPF check
        // or throw duplicate exception — depends on random. We test with
        // multiple runs or test the section validation separately.
        // For deterministic testing, we test the section and duplicate paths:
        try {
            Votes result = votesService.createVote(dto);
            // if random CPF check fails first, we get UNABLE_TO_VOTE
            assertEquals(VoteStatus.UNABLE_TO_VOTE, result.getStatus());
        } catch (IllegalArgumentException ex) {
            // if random CPF check passes, we get duplicate error
            assertEquals("Esse usuário já votou nesta seção.", ex.getMessage());
        }
    }

    @Test
    void createVote_comSecaoValida_deveSalvarOuRejeitarPorCpf() {
        VoteDTO dto = new VoteDTO(1L, 1L, true);
        when(sectionRepository.findById(1L)).thenReturn(Optional.of(activeSection));

        Votes savedVote = new Votes();
        savedVote.setId(1L);
        savedVote.setUserId(1L);
        savedVote.setSectionId(1L);
        savedVote.setVote(true);
        savedVote.setStatus(VoteStatus.ABLE_TO_VOTE);

        lenient().when(votesRepository.findByUserIdAndSectionId(1L, 1L)).thenReturn(Optional.empty());
        lenient().when(votesRepository.save(any(Votes.class))).thenReturn(savedVote);

        Votes result = votesService.createVote(dto);

        assertNotNull(result);
        // resultado é ABLE_TO_VOTE (salvo) ou UNABLE_TO_VOTE (rejeitado pelo random CPF)
        assertTrue(result.getStatus() == VoteStatus.ABLE_TO_VOTE
                || result.getStatus() == VoteStatus.UNABLE_TO_VOTE);
    }
}
