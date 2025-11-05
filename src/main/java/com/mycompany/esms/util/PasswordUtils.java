package com.mycompany.esms.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtils {

    private static final int BCRYPT_LOG_ROUNDS = 10; // strength

    private PasswordUtils() {}

    public static String hashPassword(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(BCRYPT_LOG_ROUNDS));
    }

    public static boolean matches(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}


