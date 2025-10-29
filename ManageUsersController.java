package bd.edu.seu.onlinequiz.controllers;

import bd.edu.seu.onlinequiz.DBConnection;
import bd.edu.seu.onlinequiz.HelloApplication;
import bd.edu.seu.onlinequiz.model.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageUsersController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        usernameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        roleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));

        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "Teacher", "Student"));
        userTable.setItems(userList);

        loadUsers();
    }
    ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<User, Number> idCol;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TableColumn<User, String> roleCol;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Label statusLabel;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> usernameCol;

    @FXML
    private TextField usernameField;


    private void loadUsers() {
        userList.clear();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                userList.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
            userTable.setItems(userList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddUser(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            statusLabel.setText("Fill all fields!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();

            statusLabel.setText("User added successfully!");
            loadUsers();

        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteUser(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a user to delete!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();

            statusLabel.setText("User deleted!");
            loadUsers();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backEvent(ActionEvent event) {
        HelloApplication.changeScene("admin-dashboard");
    }
}


