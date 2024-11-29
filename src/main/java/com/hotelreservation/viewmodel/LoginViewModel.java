package com.hotelreservation.viewmodel;

import com.hotelreservation.model.Staff;
import com.hotelreservation.service.LoginService;

/**
 * ViewModel class for handling login authentication for staff members.
 * Provides a method to authenticate staff by their ID and password.
 */
public class LoginViewModel {

    private LoginService loginService;

    /**
     * Constructor initializes the LoginViewModel with a LoginService instance.
     */
    public LoginViewModel() {
        loginService = new LoginService();
    }

    /**
     * Authenticates the staff member using their staff ID and password.
     *
     * @param staffId the staff member's ID
     * @param password the staff member's password
     * @return the authenticated Staff object if credentials are valid, null otherwise
     */
    public Staff authenticate(String staffId, String password) {
        return loginService.authenticateStaff(staffId, password);
    }
}
