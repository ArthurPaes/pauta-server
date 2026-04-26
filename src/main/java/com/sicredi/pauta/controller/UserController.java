package com.sicredi.pauta.controller;

import com.sicredi.pauta.domain.dto.UserDTO;
import com.sicredi.pauta.domain.dto.UserResponseDTO;
import com.sicredi.pauta.service.UserService;
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
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna todos os usuários exceto o informado")
    @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso")
    public List<UserResponseDTO> getAllUsers(@RequestParam(required = false) Long excludeId) {
        return userService.getAllUsers(excludeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário no sistema")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário já existente")
    public UserResponseDTO createUser(@Valid @RequestBody UserDTO userBody) {
        return userService.createUser(userBody);
    }
}
