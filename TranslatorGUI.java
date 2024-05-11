import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TranslatorGUI extends JFrame implements ActionListener {
    private JLabel inputLabel, outputLabel, directionLabel;
    private JTextField inputTextField, outputTextField;
    private JButton translateButton, addButton, updateButton, deleteButton;
    private JComboBox<String> directionComboBox;
    private CrudOperations translationDAO;

    public TranslatorGUI() throws SQLException, AuthenticationException {
        // Display login form
        LoginForm loginForm = new LoginForm(this);
        if (loginForm.authenticate()) {
            // Proceed to initialize the main application if authentication is successful
            setTitle("Language Translator");
            setSize(800, 600);
            //setExtendedState(JFrame.MAXIMIZED_BOTH);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridLayout(5, 2));

            // Initialize components
            inputLabel = new JLabel("Input:");
            inputTextField = new JTextField();
            outputLabel = new JLabel("Translation:");
            outputTextField = new JTextField();
            outputTextField.setEditable(false);
            directionLabel = new JLabel("Translation Direction:");
            directionComboBox = new JComboBox<>(new String[]{"English to German", "German to English"});
            translateButton = new JButton("Translate");
            addButton = new JButton("Add Translation");
            updateButton = new JButton("Update Translation");
            deleteButton = new JButton("Delete Translation");

            // Add action listeners
            translateButton.addActionListener(this);
            addButton.addActionListener(this);
            updateButton.addActionListener(this);
            deleteButton.addActionListener(this);

            // Add components to the frame
            add(inputLabel);
            add(inputTextField);
            add(outputLabel);
            add(outputTextField);
            add(directionLabel);
            add(directionComboBox);
            add(translateButton);
            add(addButton);
            add(updateButton);
            add(deleteButton);

            // Instantiate CRUD operations
            translationDAO = new CrudOperations();

            // Make the frame visible
            setVisible(true);
        } else {
            // Exit the application if authentication fails
            System.exit(0);
        }
    }


    private int getSelectedTranslationId() {
        return Integer.parseInt(JOptionPane.showInputDialog("Enter ID of translation:"));
    }

    private String getUpdatedenglishWord() {
        return JOptionPane.showInputDialog("Enter updated English word:");
    }

    private String getUpdatedgermanWord() {
        return JOptionPane.showInputDialog("Enter updated German word:");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == translateButton) {
            String inputText = inputTextField.getText();
            String translation = translateText(inputText);

            if (!translation.equals("Translation failed")) {
                outputTextField.setText(translation);
            } else {
                // Display error message in output text field
                outputTextField.setText("Translation failed. Please check your input and try again.");
            }
        } else if (e.getSource() == addButton) {
            String englishWord = JOptionPane.showInputDialog("Enter English word:");
            String germanWord = JOptionPane.showInputDialog("Enter German word:");
            translationDAO.addTranslation(englishWord, germanWord);
        } else if (e.getSource() == updateButton) {
            int id = getSelectedTranslationId();
            String updatedenglishWord = getUpdatedenglishWord();
            String updatedgermanWord = getUpdatedgermanWord();
            translationDAO.updateTranslation(id, updatedenglishWord, updatedgermanWord);
        } else if (e.getSource() == deleteButton) {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID of translation to delete:"));
            translationDAO.deleteTranslation(id);
        }
    }

    private String translateText(String text) {
        String selectedDirection = (String) directionComboBox.getSelectedItem();
        String sourceLanguage = selectedDirection.equals("German to English") ? "german" : "english";
        String targetLanguage = selectedDirection.equals("German to English") ? "english" : "german";

        String[] words = text.split("\\s+");
        StringBuilder translatedSentence = new StringBuilder();

        try {
            Connection connection = Database.getConnection();

            for (String word : words) {
                String translation = translateWord(word, sourceLanguage, targetLanguage, connection);
                translatedSentence.append(translation).append(" ");
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Translation failed";
        }

        return translatedSentence.toString().trim();
    }

    private String translateWord(String word, String sourceLanguage, String targetLanguage, Connection connection) throws SQLException {
        String query;
        if (sourceLanguage.equals("english")) {
            query = "SELECT " + targetLanguage + " FROM translations WHERE english = ?";
        } else {
            query = "SELECT " + targetLanguage + " FROM translations WHERE german = ?";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, word);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        } else {
            return "Translation not found for word:  " + word;
        }
    }


    public static void main(String[] args) throws SQLException, AuthenticationException {
        new TranslatorGUI();
    }
}