package lt.shgg.database;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public static String passwordHash(String password){
        try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] pswd = md.digest();
        BigInteger bi = new BigInteger(1, pswd);
        return bi.toString(16);
        } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
        }
    }
}
