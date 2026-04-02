package com.sicredi.pauta.service;

import com.sicredi.pauta.domain.dto.UserDTO;
import com.sicredi.pauta.domain.dto.UserResponseDTO;
import com.sicredi.pauta.domain.interfaces.UserLoginRequest;
import com.sicredi.pauta.domain.model.Users;
import com.sicredi.pauta.exception.AuthenticationException;
import com.sicredi.pauta.repository.UserRepository;
import com.sicredi.pauta.utils.BcryptUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private Users testUser;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setId(1L);
        testUser.setName("João Silva");
        testUser.setCpf("52998224725");
        testUser.setEmail("joao@test.com");
        testUser.setPassword(BcryptUtils.encryptPassword("senha123"));
    }

    // --- createUser ---

    @Test
    void createUser_comDadosValidos_deveCriarUsuario() {
        UserDTO dto = new UserDTO("João Silva", "529.982.247-25", "senha123", "joao@test.com");
        when(userRepository.findByEmail("joao@test.com")).thenReturn(null);
        when(userRepository.findByCpf("52998224725")).thenReturn(null);
        when(userRepository.save(any(Users.class))).thenReturn(testUser);

        UserResponseDTO result = userService.createUser(dto);

        assertEquals("João Silva", result.name());
        assertEquals("joao@test.com", result.email());
        assertEquals("52998224725", result.cpf());
        verify(userRepository).save(any(Users.class));
    }

    @Test
    void createUser_comEmailDuplicado_deveLancarExcecao() {
        UserDTO dto = new UserDTO("João Silva", "529.982.247-25", "senha123", "joao@test.com");
        when(userRepository.findByEmail("joao@test.com")).thenReturn(testUser);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(dto));
        assertEquals("Email já cadastrado", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_comCpfDuplicado_deveLancarExcecao() {
        UserDTO dto = new UserDTO("João Silva", "529.982.247-25", "senha123", "joao@test.com");
        when(userRepository.findByEmail("joao@test.com")).thenReturn(null);
        when(userRepository.findByCpf("52998224725")).thenReturn(testUser);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(dto));
        assertEquals("CPF já cadastrado", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_comCpfInvalido_deveLancarExcecao() {
        UserDTO dto = new UserDTO("João Silva", "111.111.111-11", "senha123", "joao@test.com");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(dto));
        assertEquals("CPF inválido", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_comCpfFormatoInvalido_deveLancarExcecao() {
        UserDTO dto = new UserDTO("João Silva", "12345", "senha123", "joao@test.com");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(dto));
        assertEquals("CPF inválido", ex.getMessage());
    }

    @Test
    void createUser_deveRemoverMascaraDoCpf() {
        UserDTO dto = new UserDTO("João Silva", "529.982.247-25", "senha123", "joao@test.com");
        when(userRepository.findByEmail("joao@test.com")).thenReturn(null);
        when(userRepository.findByCpf("52998224725")).thenReturn(null);
        when(userRepository.save(any(Users.class))).thenReturn(testUser);

        userService.createUser(dto);

        verify(userRepository).findByCpf("52998224725");
    }

    // --- authenticate ---

    @Test
    void authenticate_comCredenciaisValidas_deveAutenticar() {
        when(userRepository.findByEmail("joao@test.com")).thenReturn(testUser);

        assertDoesNotThrow(() -> userService.authenticate("joao@test.com", "senha123"));
    }

    @Test
    void authenticate_comEmailInexistente_deveLancarExcecao() {
        when(userRepository.findByEmail("naoexiste@test.com")).thenReturn(null);

        AuthenticationException ex = assertThrows(AuthenticationException.class,
                () -> userService.authenticate("naoexiste@test.com", "senha123"));
        assertEquals("Credenciais inválidas!", ex.getMessage());
    }

    @Test
    void authenticate_comSenhaIncorreta_deveLancarExcecao() {
        when(userRepository.findByEmail("joao@test.com")).thenReturn(testUser);

        AuthenticationException ex = assertThrows(AuthenticationException.class,
                () -> userService.authenticate("joao@test.com", "senhaerrada"));
        assertEquals("Credenciais inválidas!", ex.getMessage());
    }

    // --- findUser ---

    @Test
    void findUser_comEmailExistente_deveRetornarUsuario() {
        when(userRepository.findByEmail("joao@test.com")).thenReturn(testUser);

        UserResponseDTO result = userService.findUser("joao@test.com");

        assertNotNull(result);
        assertEquals("joao@test.com", result.email());
    }

    @Test
    void findUser_comEmailInexistente_deveRetornarNull() {
        when(userRepository.findByEmail("naoexiste@test.com")).thenReturn(null);

        UserResponseDTO result = userService.findUser("naoexiste@test.com");

        assertNull(result);
    }

    // --- login ---

    @Test
    void login_comCredenciaisValidas_deveRetornarUsuario() {
        when(userRepository.findByEmail("joao@test.com")).thenReturn(testUser);

        UserLoginRequest request = new UserLoginRequest("joao@test.com", "senha123");
        UserResponseDTO result = userService.login(request);

        assertNotNull(result);
        assertEquals("joao@test.com", result.email());
    }

    @Test
    void login_comCredenciaisInvalidas_deveLancarExcecao() {
        when(userRepository.findByEmail("joao@test.com")).thenReturn(testUser);

        UserLoginRequest request = new UserLoginRequest("joao@test.com", "senhaerrada");

        assertThrows(AuthenticationException.class, () -> userService.login(request));
    }
}
