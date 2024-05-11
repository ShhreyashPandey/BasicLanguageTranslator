import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudOperations {

    // Method to add a new translation
    public void addTranslation(String englishWord, String germanWord) {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO translations (english, german) VALUES (?, ?)")) {
            preparedStatement.setString(1, englishWord);
            preparedStatement.setString(2, germanWord);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to update an existing translation
    public void updateTranslation (int id, String englishWord, String germanWord) {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE translations SET english = ?, german = ? WHERE id = ?")) {
            preparedStatement.setString(1, englishWord);
            preparedStatement.setString(2, germanWord);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to delete a translation by its ID
    public void deleteTranslation(int id) {
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM translations WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}