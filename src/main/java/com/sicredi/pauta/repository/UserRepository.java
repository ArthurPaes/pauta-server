package com.sicredi.pauta.repository;

import com.sicredi.pauta.domain.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    // find a user by their email address (used for login)
    Users findByEmail(String email);

    // find a user by their CPF (used to check uniqueness on registration)
    Users findByCpf(String cpf);
}
