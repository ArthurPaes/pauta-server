package com.sicredi.pauta.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BcryptUtilsTest {

    @Test
    void encryptPassword_deveRetornarHash() {
        String hash = BcryptUtils.encryptPassword("senha123");

        assertNotNull(hash);
        assertNotEquals("senha123", hash);
        assertTrue(hash.startsWith("$2a$"));
    }

    @Test
    void comparePasswords_comSenhaCorreta_deveRetornarTrue() {
        String hash = BcryptUtils.encryptPassword("senha123");

        assertTrue(BcryptUtils.comparePasswords("senha123", hash));
    }

    @Test
    void comparePasswords_comSenhaIncorreta_deveRetornarFalse() {
        String hash = BcryptUtils.encryptPassword("senha123");

        assertFalse(BcryptUtils.comparePasswords("senhaerrada", hash));
    }

    @Test
    void encryptPassword_mesmoInput_deveGerarHashesDiferentes() {
        String hash1 = BcryptUtils.encryptPassword("senha123");
        String hash2 = BcryptUtils.encryptPassword("senha123");

        assertNotEquals(hash1, hash2);
        assertTrue(BcryptUtils.comparePasswords("senha123", hash1));
        assertTrue(BcryptUtils.comparePasswords("senha123", hash2));
    }
}
