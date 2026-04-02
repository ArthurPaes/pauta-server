package com.sicredi.pauta.service;

import com.sicredi.pauta.domain.dto.SectionDTO;
import com.sicredi.pauta.domain.interfaces.SectionWithVotesCount;
import com.sicredi.pauta.domain.model.Sections;
import com.sicredi.pauta.repository.SectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private SectionService sectionService;

    @Test
    void getAllSectionsWithVotes_deveRetornarListaDePautas() {
        SectionWithVotesCount mockSection = mock(SectionWithVotesCount.class);
        when(sectionRepository.findAllWithVotesCount(1L)).thenReturn(List.of(mockSection));

        List<SectionWithVotesCount> result = sectionService.getAllSectionsWithVotes(1L);

        assertEquals(1, result.size());
        verify(sectionRepository).findAllWithVotesCount(1L);
    }

    @Test
    void getAllSectionsWithVotes_semPautas_deveRetornarListaVazia() {
        when(sectionRepository.findAllWithVotesCount(1L)).thenReturn(List.of());

        List<SectionWithVotesCount> result = sectionService.getAllSectionsWithVotes(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void createSection_deveSalvarPauta() {
        SectionDTO dto = new SectionDTO("Pauta Teste", "Descrição da pauta teste", 10);

        Sections saved = new Sections();
        saved.setId(1L);
        saved.setName("Pauta Teste");
        saved.setDescription("Descrição da pauta teste");
        saved.setExpiration(10);

        when(sectionRepository.save(any(Sections.class))).thenReturn(saved);

        Sections result = sectionService.createSection(dto);

        assertEquals("Pauta Teste", result.getName());
        assertEquals(10, result.getExpiration());
        verify(sectionRepository).save(any(Sections.class));
    }

    @Test
    void createSection_deveDefinirStartAt() {
        SectionDTO dto = new SectionDTO("Pauta Teste", "Descrição da pauta teste", 5);

        when(sectionRepository.save(any(Sections.class))).thenAnswer(invocation -> {
            Sections s = invocation.getArgument(0);
            assertNotNull(s.getStartAt());
            s.setId(1L);
            return s;
        });

        sectionService.createSection(dto);

        verify(sectionRepository).save(any(Sections.class));
    }
}
