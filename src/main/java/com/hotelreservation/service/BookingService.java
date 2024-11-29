package com.hotelreservation.service;

import com.hotelreservation.model.Booking;
import com.hotelreservation.model.Guest;
import com.hotelreservation.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for managing bookings.
 * Provides functionality to create, retrieve, update bookings and associate them with guests.
 */
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private GuestService guestService;

    /**
     * Constructs a BookingService with a new GuestService instance.
     */
    public BookingService() {
        this.guestService = new GuestService();
    }

    /**
     * Retrieves all bookings from the database.
     *
     * @return a list of all bookings
     */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Bookings";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getString("bookingId"),
                        rs.getString("guestId"),
                        rs.getString("roomId"),
                        rs.getString("voucherNumber"),
                        rs.getDate("inDate").toLocalDate(),
                        rs.getDate("outDate").toLocalDate(),
                        rs.getString("specialPreference"),
                        rs.getString("paymentStatus"),
                        rs.getString("bookingStatus")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            logger.error("Error fetching all bookings", e);
        }

        return bookings;
    }

    /**
     * Creates a new booking and associates it with a guest.
     *
     * @param booking the booking to create
     * @param guest   the guest associated with the booking
     * @return true if the booking was created successfully, false otherwise
     */
    public boolean createBooking(Booking booking, Guest guest) {
        // Create or update the guest
        if (guestService.getGuestById(guest.getGuestId()) == null) {
            if (!guestService.createGuest(guest)) {
                return false;
            }
        }

        String sql = "INSERT INTO Bookings (bookingId, guestId, roomId, voucherNumber, inDate, outDate, specialPreference, paymentStatus, bookingStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, booking.getBookingId());
            pstmt.setString(2, guest.getGuestId());
            pstmt.setString(3, booking.getRoomId());
            pstmt.setString(4, booking.getVoucherNumber());
            pstmt.setDate(5, Date.valueOf(booking.getInDate()));
            pstmt.setDate(6, Date.valueOf(booking.getOutDate()));
            pstmt.setString(7, booking.getSpecialPreference());
            pstmt.setString(8, booking.getPaymentStatus());
            pstmt.setString(9, booking.getBookingStatus());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error creating booking", e);
            return false;
        }
    }

    /**
     * Retrieves a booking along with its associated guest information by booking ID.
     *
     * @param bookingId the ID of the booking
     * @return the booking with guest details, or null if not found
     */
    public Booking getBookingWithGuestInfo(String bookingId) {
        String sql = "SELECT b.*, g.firstName, g.lastName, g.middleName, g.contactNumber " +
                "FROM Bookings b " +
                "JOIN Guests g ON b.guestId = g.guestId " +
                "WHERE b.bookingId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookingId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Booking booking = new Booking(
                        rs.getString("bookingId"),
                        rs.getString("guestId"),
                        rs.getString("roomId"),
                        rs.getString("voucherNumber"),
                        rs.getDate("inDate").toLocalDate(),
                        rs.getDate("outDate").toLocalDate(),
                        rs.getString("specialPreference"),
                        rs.getString("paymentStatus"),
                        rs.getString("bookingStatus")
                );

                Guest guest = new Guest(
                        rs.getString("guestId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("middleName"),
                        null, // No need to retrieve the password since it's not part of the return data
                        rs.getString("contactNumber")
                );

                booking.setGuest(guest);
                return booking;
            }
        } catch (SQLException e) {
            logger.error("Error fetching booking with guest info", e);
        }

        return null;
    }

    /**
     * Updates the details of an existing booking.
     *
     * @param booking the booking with updated details
     * @return true if the update was successful, false otherwise
     */
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE Bookings SET roomId = ?, voucherNumber = ?, inDate = ?, outDate = ?, specialPreference = ?, paymentStatus = ?, bookingStatus = ? WHERE bookingId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, booking.getRoomId());
            pstmt.setString(2, booking.getVoucherNumber());
            pstmt.setDate(3, Date.valueOf(booking.getInDate()));
            pstmt.setDate(4, Date.valueOf(booking.getOutDate()));
            pstmt.setString(5, booking.getSpecialPreference());
            pstmt.setString(6, booking.getPaymentStatus());
            pstmt.setString(7, booking.getBookingStatus());
            pstmt.setString(8, booking.getBookingId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error updating booking", e);
            return false;
        }
    }
}
