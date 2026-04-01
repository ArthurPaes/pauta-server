package com.sicredi.pauta.infra.mapper;

import com.sicredi.pauta.domain.dto.UserDTO;
import com.sicredi.pauta.domain.dto.UserResponseDTO;
import com.sicredi.pauta.domain.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserMapper {

    public Users toEntity(UserDTO userDTO) {
        log.trace("Convertendo UserDTO para Users: {}", userDTO.email());
        Users user = new Users();
        user.setName(userDTO.name());
        user.setCpf(userDTO.cpf());
        user.setPassword(userDTO.password());
        user.setEmail(userDTO.email());
        return user;
    }

    public UserResponseDTO toResponseDTO(Users user) {
        log.trace("Convertendo Users para UserResponseDTO: {}", user.getEmail());
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail()
        );
    }
}
