import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GermanToEnglish extends Translator {
    public static String translateFromGermanToEnglish(String germanWord) {
        String query = "SELECT english FROM translations WHERE german = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, germanWord);
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

