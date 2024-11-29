package com.hotelreservation.service;

import com.hotelreservation.model.Guest;
import com.hotelreservation.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for managing guest information.
 * Provides functionality to create, retrieve, update, and delete guests.
 */
public class GuestService {
    private static final Logger logger = LoggerFactory.getLogger(GuestService.class);

    /**
     * Retrieves a guest by their guest ID.
     *
     * @param guestId the ID of the guest
     * @return the guest with the specified ID, or null if not found
     */
    public Guest getGuestById(String guestId) {
        String sql = "SELECT * FROM Guests WHERE guestId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, guestId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Guest(
                        rs.getString("guestId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("middleName"),
                        rs.getString("password"),
                        rs.getString("contactNumber")
                );
            }
        } catch (SQLException e) {
            logger.error("Error fetching guest by ID", e);
        }

        return null;
    }

    /**
     * Creates a new guest in the database.
     *
     * @param guest the guest to create
     * @return true if the guest was created successfully, false otherwise
     */
    public boolean createGuest(Guest guest) {
        String sql = "INSERT INTO Guests (guestId, firstName, lastName, middleName, password, contactNumber) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, guest.getGuestId());
            pstmt.setString(2, guest.getFirstName());
            pstmt.setString(3, guest.getLastName());
            pstmt.setString(4, guest.getMiddleName());
            pstmt.setString(5, guest.getPassword());
            pstmt.setString(6, guest.getContactNumber());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error creating guest", e);
            return false;
        }
    }

    /**
     * Retrieves all guests from the database.
     *
     * @return a list of all guests
     */
    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM Guests";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getString("guestId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("middleName"),
                        rs.getString("password"),
                        rs.getString("contactNumber")
                );
                guests.add(guest);
            }
        } catch (SQLException e) {
            logger.error("Error fetching all guests", e);
        }

        return guests;
    }

    /**
     * Updates the details of an existing guest.
     *
     * @param guest the guest with updated details
     * @return true if the update was successful, false otherwise
     */
    public boolean updateGuest(Guest guest) {
        String sql = "UPDATE Guests SET firstName = ?, lastName = ?, middleName = ?, password = ?, contactNumber = ? WHERE guestId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getLastName());
            pstmt.setString(3, guest.getMiddleName());
            pstmt.setString(4, guest.getPassword());
            pstmt.setString(5, guest.getContactNumber());
            pstmt.setString(6, guest.getGuestId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating guest", e);
            return false;
        }
    }
}
