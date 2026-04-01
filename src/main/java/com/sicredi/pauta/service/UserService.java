package com.sicredi.pauta.service;

import com.sicredi.pauta.domain.dto.UserDTO;
import com.sicredi.pauta.domain.dto.UserResponseDTO;
import com.sicredi.pauta.domain.interfaces.UserLoginRequest;
import com.sicredi.pauta.domain.model.Users;
import com.sicredi.pauta.exception.AuthenticationException;
import com.sicredi.pauta.infra.mapper.UserMapper;
import com.sicredi.pauta.repository.UserRepository;
import com.sicredi.pauta.utils.BcryptUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserDTO userDTO) {
        log.info("Criando um novo usuário com o email: {}", userDTO.email());

        if (userRepository.findByEmail(userDTO.email()) != null) {
            log.warn("Email já cadastrado: {}", userDTO.email());
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (userRepository.findByCpf(userDTO.cpf()) != null) {
            log.warn("CPF já cadastrado: {}", userDTO.cpf());
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        Users user = userMapper.toEntity(userDTO);
        user.setPassword(BcryptUtils.encryptPassword(user.getPassword()));

        Users saved = userRepository.save(user);
        log.info("Usuário criado com sucesso. ID: {}, Email: {}", saved.getId(), saved.getEmail());
        return userMapper.toResponseDTO(saved);
    }

    public void authenticate(String email, String password) {
        log.debug("Tentativa de autenticação para o email: {}", email);

        Users user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("Email inexistente: {}", email);
            throw new AuthenticationException("Credenciais inválidas!");
        }

        if (!BcryptUtils.comparePasswords(password, user.getPassword())) {
            log.warn("Senha incorreta para o email: {}", email);
            throw new AuthenticationException("Credenciais inválidas!");
        }

        log.info("Autenticação realizada com sucesso para o email: {}", email);
    }

    public UserResponseDTO findUser(String email) {
        log.debug("Buscando usuário pelo email: {}", email);
        Users user = userRepository.findByEmail(email);
        if (user != null) {
            return userMapper.toResponseDTO(user);
        }
        return null;
    }

    public UserResponseDTO login(UserLoginRequest userBody) {
        log.info("Tentativa de login para o email: {}", userBody.email());
        authenticate(userBody.email(), userBody.password());
        UserResponseDTO user = findUser(userBody.email());
        log.info("Login realizado com sucesso para o email: {}", userBody.email());
        return user;
    }
}
