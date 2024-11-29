package com.hotelreservation.view;

import com.hotelreservation.App;
import com.hotelreservation.model.Staff;
import com.hotelreservation.viewmodel.LoginViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Controller class for managing the login view.
 * Handles user input for staff login, authenticates credentials, and navigates to the main view upon successful login.
 */
public class LoginViewController {

    private static final Logger logger = LoggerFactory.getLogger(LoginViewController.class);

    @FXML
    private TextField staffIdField;

    @FXML
    private PasswordField passwordField;

    private LoginViewModel loginViewModel;

    /**
     * Initializes the LoginViewController with the LoginViewModel.
     */
    public LoginViewController() {
        loginViewModel = new LoginViewModel();
    }

    /**
     * Handles the login process. Authenticates the staff using the provided staff ID and password.
     * If authentication is successful, navigates to the main view. If authentication fails, shows an error alert.
     */
    @FXML
    private void handleLogin() {
        String staffId = staffIdField.getText();
        String password = passwordField.getText();

        logger.info("Login attempt for staff ID: {}", staffId);

        Staff authenticatedStaff = loginViewModel.authenticate(staffId, password);

        if (authenticatedStaff != null) {
            logger.info("Login successful for staff ID: {}", staffId);
            try {
                App.showMainView(authenticatedStaff);
            } catch (IOException e) {
                logger.error("Failed to load main view", e);
                App.showErrorAlert("Error", "Failed to load main view.");
            }
        } else {
            logger.warn("Login failed for staff ID: {}", staffId);
            App.showErrorAlert("Login Failed", "Invalid staff ID or password.");
        }
    }
}
