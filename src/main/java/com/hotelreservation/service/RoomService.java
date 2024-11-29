package com.hotelreservation.service;

import com.hotelreservation.model.Room;
import com.hotelreservation.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for managing room-related operations.
 * Provides functionality to get available rooms, add, update, or delete rooms, and fetch room types.
 */
public class RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    /**
     * Retrieves a list of all distinct room types available in the hotel.
     *
     * @return a list of room types
     */
    public List<String> getAllRoomTypes() {
        List<String> roomTypes = new ArrayList<>();
        String sql = "SELECT DISTINCT roomType FROM Rooms";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                roomTypes.add(rs.getString("roomType"));
            }
        } catch (SQLException e) {
            logger.error("Error fetching room types", e);
        }

        return roomTypes;
    }

    /**
     * Retrieves a list of available rooms based on the specified room type and date range.
     * A room is considered available if it matches the room type and is not booked during the provided date range.
     *
     * @param roomType the type of room to look for
     * @param checkInDate the check-in date for the reservation
     * @param checkOutDate the check-out date for the reservation
     * @return a list of available rooms
     */
    public List<Room> getAvailableRooms(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Room> availableRooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms r WHERE r.roomType = ? AND r.availability = true " +
                "AND r.roomId NOT IN (SELECT b.roomId FROM Bookings b WHERE " +
                "(b.inDate <= ? AND b.outDate >= ?) OR " +
                "(b.inDate >= ? AND b.inDate < ?))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, roomType);
            pstmt.setDate(2, Date.valueOf(checkOutDate));
            pstmt.setDate(3, Date.valueOf(checkInDate));
            pstmt.setDate(4, Date.valueOf(checkInDate));
            pstmt.setDate(5, Date.valueOf(checkOutDate));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Room room = new Room(
                        rs.getString("roomId"),
                        rs.getString("roomType"),
                        rs.getDouble("roomPrice"),
                        rs.getBoolean("availability")
                );
                availableRooms.add(room);
            }
        } catch (SQLException e) {
            logger.error("Error fetching available rooms", e);
        }

        return availableRooms;
    }

    /**
     * Adds a new room to the database.
     *
     * @param room the room to be added
     * @return true if the room was successfully added, false otherwise
     */
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO Rooms (roomType, roomPrice, availability) " +
                "VALUES (?, ?, ?) RETURNING roomId";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, room.getRoomType());
            pstmt.setDouble(2, room.getRoomPrice());
            pstmt.setBoolean(3, room.isAvailability());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String generatedId = rs.getString("roomId");
                room.setRoomId(generatedId);
                logger.info("Generated roomId: {}", generatedId);
            }

            return true;
        } catch (SQLException e) {
            logger.error("SQL error when adding room: {}", e.getMessage());
            return false;
        }
    }


    /**
     * Updates the details of an existing room.
     *
     * @param room the room with updated details
     * @return true if the room was successfully updated, false otherwise
     */
    public boolean updateRoom(Room room) {
        String sql = "UPDATE Rooms SET roomType = ?, roomPrice = ?, availability = ? WHERE roomId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, room.getRoomType());
            pstmt.setDouble(2, room.getRoomPrice());
            pstmt.setBoolean(3, room.isAvailability());
            pstmt.setString(4, room.getRoomId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating room", e);
            return false;
        }
    }

    /**
     * Deletes a room from the database by its roomId.
     *
     * @param roomId the ID of the room to delete
     * @return true if the room was successfully deleted, false otherwise
     */
    public boolean deleteRoom(String roomId) {
        String sql = "DELETE FROM Rooms WHERE roomId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, roomId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error deleting room", e);
            return false;
        }
    }

    /**
     * Retrieves a list of all rooms in the hotel.
     *
     * @return a list of all rooms
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room room = new Room(
                        rs.getString("roomId"),
                        rs.getString("roomType"),
                        rs.getDouble("roomPrice"),
                        rs.getBoolean("availability")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            logger.error("Error fetching all rooms", e);
        }

        return rooms;
    }
}
