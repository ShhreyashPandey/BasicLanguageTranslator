import java.sql.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// Translator class implementing the Translation interface
public abstract class Translator implements Translation {
    private static final String key = "KeyForTranslator"; // 16 characters for AES-128
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
        return decrypt("X9G8wx/CFhljUXh/pAn+jA==");
    }

    // Get decrypted password
    private static String getPassword() {
        return decrypt("e1e0DCky+0nwHujL2pbOwg==");
    }


    @Override
    public String translate(String text, String direction) throws TranslationException {
        String sourceLanguage, targetLanguage;
        if (direction.equals("English to German")) {
            sourceLanguage = "english";
            targetLanguage = "german";
        } else {
            sourceLanguage = "german";
            targetLanguage = "english";
        }

        String[] words = text.split("\\s+");
        StringBuilder translatedSentence = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/language_translator", getUsername(), getPassword());

            for (String word : words) {
                String translation = translateWord(word, sourceLanguage, targetLanguage, connection);
                translatedSentence.append(translation).append(" ");
            }

            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            throw new TranslationException("Translation failed: " + ex.getMessage());
        }

        return translatedSentence.toString().trim();
    }

    private String translateWord(String word, String sourceLanguage, String targetLanguage, Connection connection) throws SQLException {
        String query = "SELECT " + targetLanguage + " FROM translations WHERE " + sourceLanguage + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, word);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            // Encrypt the translation before returning
            return resultSet.getString(1);
        } else {
            return "Translation not found for word: " + word;
        }
    }

}
