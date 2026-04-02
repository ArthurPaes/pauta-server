package com.sicredi.pauta.controller;

import com.sicredi.pauta.domain.dto.SectionDTO;
import com.sicredi.pauta.domain.interfaces.SectionWithVotesCount;
import com.sicredi.pauta.domain.model.Sections;
import com.sicredi.pauta.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/section")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Endpoints para gerenciamento de pautas")
public class SectionController {

    private final SectionService sectionService;

    @GetMapping
    @Operation(summary = "Listar pautas", description = "Retorna todas as pautas com contagem de votos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pautas retornadas com sucesso")
    })
    public List<SectionWithVotesCount> getAllSections(@RequestParam Long userId) {
        return sectionService.getAllSectionsWithVotes(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar pauta", description = "Cadastra uma nova pauta no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Sections createSection(@Valid @RequestBody SectionDTO sectionBody) {
        return sectionService.createSection(sectionBody);
    }
}
