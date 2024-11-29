package com.hotelreservation.service;

import com.hotelreservation.model.Staff;
import com.hotelreservation.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for authenticating staff login.
 * Provides functionality to authenticate staff by validating credentials.
 */
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    /**
     * Authenticates staff based on their staff ID and password.
     *
     * @param staffId the staff's ID
     * @param password the staff's password
     * @return a Staff object if authentication is successful, null otherwise
     */
    public Staff authenticateStaff(String staffId, String password) {
        String sql = "SELECT * FROM Staffs WHERE staffId = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, staffId);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Staff(
                        rs.getString("staffId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("middleName"),
                        rs.getString("password"),
                        rs.getString("position")
                );
            }
        } catch (SQLException e) {
            logger.error("Error authenticating staff", e);
        }

        return null;
    }
}
