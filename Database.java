import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/language_translator";
    // Encrypted credentials
    private static final String encryptedUsername = "X9G8wx/CFhljUXh/pAn+jA==";
    private static final String encryptedPassword = "e1e0DCky+0nwHujL2pbOwg==";

    // Encryption key (16 characters for AES-128)
    private static final String key = "KeyForTranslator";

    // Fixed IV for encryption and decryption
    private static final byte[] fixedIV = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF,
            (byte) 0xFE, (byte) 0xDC, (byte) 0xBA, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };

    // Decrypt sensitive information
    private static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(fixedIV);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            System.err.println("Error decrypting credentials: " + ex.getMessage());
        }
        return null;
    }

    // Get decrypted username
    private static String getUsername() {
        return decrypt(encryptedUsername);
    }

    // Get decrypted password
    private static String getPassword() {
        return decrypt(encryptedPassword);
    }

    // Establish database connection
    public static Connection getConnection() throws SQLException {
        String username = getUsername();
        String password = getPassword();
        return DriverManager.getConnection(URL, username, password);
    }
}
