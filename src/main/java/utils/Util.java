package utils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class Util {

    private static final SecureRandom random = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static <T> T getRandomElement(List<T> array) {
        Random random = new Random();
        int randomIndex = random.nextInt(array.size());
        return array.get(randomIndex);
    };

    public static String generateHexString(int byteLength) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[byteLength];
        random.nextBytes(bytes);

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b)); // "%02X" gera letras maiúsculas
        }

        return sb.toString();
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
