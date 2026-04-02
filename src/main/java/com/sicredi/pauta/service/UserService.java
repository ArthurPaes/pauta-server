package com.sicredi.pauta.service;

import com.sicredi.pauta.domain.dto.UserDTO;
import com.sicredi.pauta.domain.dto.UserResponseDTO;
import com.sicredi.pauta.domain.interfaces.UserLoginRequest;
import com.sicredi.pauta.domain.model.Users;
import com.sicredi.pauta.exception.AuthenticationException;
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

    public UserResponseDTO createUser(UserDTO userDTO) {
        log.info("Criando um novo usuário com o email: {}", userDTO.email());

        String cpf = userDTO.cpf().replaceAll("\\D", "");

        if (userRepository.findByEmail(userDTO.email()) != null) {
            log.warn("Email já cadastrado: {}", userDTO.email());
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (userRepository.findByCpf(cpf) != null) {
            log.warn("CPF já cadastrado: {}", cpf);
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        Users user = new Users();
        user.setName(userDTO.name());
        user.setCpf(cpf);
        user.setEmail(userDTO.email());
        user.setPassword(BcryptUtils.encryptPassword(userDTO.password()));

        Users saved = userRepository.save(user);
        log.info("Usuário criado com sucesso. ID: {}, Email: {}", saved.getId(), saved.getEmail());
        return new UserResponseDTO(saved.getId(), saved.getName(), saved.getCpf(), saved.getEmail());
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
        return user != null ? new UserResponseDTO(user.getId(), user.getName(), user.getCpf(), user.getEmail()) : null;
    }

    public UserResponseDTO login(UserLoginRequest userBody) {
        log.info("Tentativa de login para o email: {}", userBody.email());
        authenticate(userBody.email(), userBody.password());
        UserResponseDTO user = findUser(userBody.email());
        log.info("Login realizado com sucesso para o email: {}", userBody.email());
        return user;
    }

}
