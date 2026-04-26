package com.sicredi.pauta.controller;

import com.sicredi.pauta.domain.dto.SectionDTO;
import com.sicredi.pauta.domain.dto.SectionResponseDTO;
import com.sicredi.pauta.domain.dto.SectionUsersDTO;
import com.sicredi.pauta.domain.dto.UpdateSectionDTO;
import com.sicredi.pauta.domain.model.Sections;
import com.sicredi.pauta.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @ApiResponse(responseCode = "200", description = "Pautas retornadas com sucesso")
    public List<SectionResponseDTO> getAllSections(@RequestParam Long userId) {
        return sectionService.getAllSectionsWithVotes(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar pauta", description = "Cadastra uma nova pauta no sistema")
    @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public Sections createSection(@Valid @RequestBody SectionDTO sectionBody) {
        return sectionService.createSection(sectionBody);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pauta", description = "Atualiza os dados de uma pauta existente")
    @ApiResponse(responseCode = "200", description = "Pauta atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    public Sections updateSection(@PathVariable Long id, @Valid @RequestBody UpdateSectionDTO body) {
        return sectionService.updateSection(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir pauta", description = "Remove uma pauta do sistema")
    @ApiResponse(responseCode = "204", description = "Pauta excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    public void deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
    }

    @PutMapping("/{id}/users")
    @Operation(summary = "Atualizar usuários habilitados", description = "Define quais usuários podem votar na pauta")
    @ApiResponse(responseCode = "200", description = "Usuários atualizados com sucesso")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    public Sections updateSectionUsers(@PathVariable Long id, @Valid @RequestBody SectionUsersDTO body) {
        return sectionService.updateSectionUsers(id, body);
    }
}
