package com.sicredi.pauta.controller;

import com.sicredi.pauta.domain.dto.VoteDTO;
import com.sicredi.pauta.domain.model.Votes;
import com.sicredi.pauta.service.VotesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
@Tag(name = "Votos", description = "Endpoints para registro de votos")
public class VotesController {

    private final VotesService votesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar voto", description = "Registra o voto de um usuário em uma pauta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos, pauta encerrada ou voto duplicado")
    })
    public Votes createVote(@Valid @RequestBody VoteDTO voteBody) {
        return votesService.createVote(voteBody);
    }
}
