package com.sicredi.pauta.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class BcryptUtils {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encryptPassword(String password) {
        log.debug("Criptografando senha");
        return encoder.encode(password);
    }

    public static boolean comparePasswords(String password, String hashedPassword) {
        log.debug("Comparando senhas");
        return encoder.matches(password, hashedPassword);
    }
}
