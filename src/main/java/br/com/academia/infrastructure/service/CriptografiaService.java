package br.com.academia.infrastructure.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CriptografiaService {

    public static String criptografarSenha(String senha) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
        String hashString = bytesToHex(hash);
        return hashString;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02X", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String gerarTokenNumerico() {
        String NUMERIC_CHARS = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder tokenBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(NUMERIC_CHARS.length());
            tokenBuilder.append(NUMERIC_CHARS.charAt(index));
        }
        return tokenBuilder.toString();
    }

}
