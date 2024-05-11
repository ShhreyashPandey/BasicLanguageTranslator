import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnglishToGerman extends Translator {
    public static String translateFromEnglishToGerman(String englishWord) {
        String query = "SELECT german FROM translations WHERE english = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, englishWord);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                return "Translation not found";
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Translation failed";
        }
    }
}

