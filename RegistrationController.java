
package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.HelloApplication;
import bd.edu.seu.onlinequiz.model.Registration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleComboBox.setItems(FXCollections.observableArrayList("Admin","Teacher","Student"));
        imageUpload.setOnMouseClicked(event -> handleImageUpload());
    }

    @FXML
    private TextField contactField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private ToggleGroup genderGroup;

    @FXML
    private ImageView imageUpload;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField rePasswordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea textAreaField;

    @FXML
    private Label uploadLabel;

    private File selectedImageFile;
    ObservableList<Registration> list = FXCollections.observableArrayList();

    @FXML
    void backEvent(ActionEvent event) {
        HelloApplication.changeScene("welcome");
    }

    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            if (file.length() > 512000) { // > 500KB
                statusLabel.setText("Image too large. Must be ≤ 500 KB.");
                return;
            }

            selectedImageFile = file;
            Image image = new Image(file.toURI().toString());
            imageUpload.setImage(image);
            uploadLabel.setText("Uploaded: " + file.getName());
            statusLabel.setText("");
        }
    }

    @FXML
    void submitEvent(ActionEvent event) {
        String first = firstNameField.getText();
        String last = lastNameField.getText();
        String email = emailField.getText();
        String contact = contactField.getText();
        String password = passwordField.getText();
        String rePassword = rePasswordField.getText();
        String bio = textAreaField.getText();
        String role = roleComboBox.getValue();
        String gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();

        if (first.isEmpty() || last.isEmpty() || email.isEmpty() || contact.isEmpty() || password.isEmpty() || rePassword.isEmpty() || bio.isEmpty() || role == null || gender == null) {
            statusLabel.setText("Please fill all required fields.");
            return;
        }

        if (!password.equals(rePassword)) {
            statusLabel.setText("Passwords do not match.");
            return;
        }

        // Save to DB logic here (optional, depending on your system)
        statusLabel.setText("Registration Successful.");
    }
}
