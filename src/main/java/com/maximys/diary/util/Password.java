package com.maximys.diary.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Password {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] byteData = md.digest();

            // Преобразуем байты в шестнадцатеричную строку
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Не удалось хешировать пароль", e);
        }
    }
}

