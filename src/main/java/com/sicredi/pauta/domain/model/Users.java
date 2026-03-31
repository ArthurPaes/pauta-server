package com.sicredi.pauta.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Schema(
    name = "Users",
    description = "Entidade de usuário do sistema",
    example = """
        {
            "id": 1,
            "name": "João Silva",
            "email": "joao.silva@example.com",
            "cpf": "12345678909",
            "password": "$2a$10$encryptedPasswordHash"
        }
        """
)
            
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 11)   
    private String cpf;

    @Column(nullable = false)
    private String password;

}
