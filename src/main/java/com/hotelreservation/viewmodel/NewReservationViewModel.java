package com.hotelreservation.viewmodel;

import com.hotelreservation.model.Booking;
import com.hotelreservation.model.Guest;
import com.hotelreservation.model.Room;
import com.hotelreservation.service.BookingService;
import com.hotelreservation.service.RoomService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * ViewModel class for creating new reservations.
 * Provides functionality to search for available rooms and create new bookings.
 */
public class NewReservationViewModel {

    private RoomService roomService;
    private BookingService bookingService;
    private ObservableList<String> roomTypes;
    private ObservableList<Room> availableRooms;

    /**
     * Constructor initializes the NewReservationViewModel with services for room and booking management,
     * and initializes lists for available rooms and room types.
     */
    public NewReservationViewModel() {
        roomService = new RoomService();
        bookingService = new BookingService();
        roomTypes = FXCollections.observableArrayList();
        availableRooms = FXCollections.observableArrayList();
        loadRoomTypes();
    }

    /**
     * Loads all available room types from the room service and updates the roomTypes list.
     */
    private void loadRoomTypes() {
        List<String> types = roomService.getAllRoomTypes();
        roomTypes.setAll(types);
    }

    /**
     * Returns the observable list of room types available for the new reservation.
     *
     * @return an observable list of room types
     */
    public ObservableList<String> getRoomTypes() {
        return roomTypes;
    }

    /**
     * Returns the observable list of available rooms, filtered based on the search criteria.
     *
     * @return an observable list of available rooms
     */
    public ObservableList<Room> getAvailableRooms() {
        return availableRooms;
    }

    /**
     * Searches for available rooms based on the specified room type, check-in date, and check-out date.
     *
     * @param roomType the type of room to search for
     * @param checkInDate the desired check-in date
     * @param checkOutDate the desired check-out date
     */
    public void searchAvailableRooms(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Room> rooms = roomService.getAvailableRooms(roomType, checkInDate, checkOutDate);
        availableRooms.setAll(rooms);
    }

    /**
     * Creates a new booking for a guest with the specified room, dates, voucher number, and special preferences.
     *
     * @param room the room to be booked
     * @param guest the guest making the reservation
     * @param checkInDate the check-in date for the booking
     * @param checkOutDate the check-out date for the booking
     * @param voucherNumber the voucher number for the booking
     * @param specialPreference any special preferences the guest has
     * @return the created Booking object, or null if the booking creation failed
     */
    public Booking createBooking(Room room, Guest guest, LocalDate checkInDate, LocalDate checkOutDate, String voucherNumber, String specialPreference) {
        String bookingId = UUID.randomUUID().toString();
        Booking booking = new Booking(bookingId, guest.getGuestId(), room.getRoomId(), voucherNumber,
                checkInDate, checkOutDate, specialPreference, "Pending", "Confirmed");

        if (bookingService.createBooking(booking, guest)) {
            return booking;
        }
        return null;
    }
}
