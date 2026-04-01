package com.sicredi.pauta.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

@Schema(description = "Dados para criação de usuário")
public record UserDTO(
    @Schema(description = "Nome completo do usuário", example = "João Silva")
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    String name,

    @Schema(description = "CPF do usuário (apenas números)", example = "12345678909")
    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    String cpf,

    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "senha123")
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
    String password,

    @Schema(description = "Email do usuário", example = "joao@example.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    String email
) {}
