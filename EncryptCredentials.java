/*import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptCredentials {
    private static final String key = "KeyForTranslator"; // 16 characters for AES-128
    // Specify your IV here
    private static final byte[] fixedIV = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF,
            (byte) 0xFE, (byte) 0xDC, (byte) 0xBA, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };

    public static void main(String[] args) {
        String username = "root"; // Replace with your actual username
        String password = "shreya123"; // Replace with your actual password

        // Encrypt username
        String encryptedUsername = encrypt(username);
        System.out.println("Encrypted username: " + encryptedUsername);

        // Encrypt password
        String encryptedPassword = encrypt(password);
        System.out.println("Encrypted password: " + encryptedPassword);
    }

    // Encrypt sensitive information
    public static String encrypt(String value) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(fixedIV);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}*/