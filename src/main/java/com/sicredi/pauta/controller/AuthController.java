package com.sicredi.pauta.controller;

import com.sicredi.pauta.domain.dto.UserResponseDTO;
import com.sicredi.pauta.domain.interfaces.UserLoginRequest;
import com.sicredi.pauta.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Autenticar usuário", description = "Realiza a autenticação de um usuário no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public UserResponseDTO login(@Valid @RequestBody UserLoginRequest userBody) {
        return userService.login(userBody);
    }
}
