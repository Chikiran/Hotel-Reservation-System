package com.hotelreservation.viewmodel;

import com.hotelreservation.model.Booking;
import com.hotelreservation.service.BookingService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel class for managing the details of a booking.
 * Provides methods to retrieve and update booking details such as special preferences, payment status, and booking status.
 */
public class BookingDetailsViewModel {

    private Booking booking;
    private BookingService bookingService;

    private StringProperty bookingId;
    private StringProperty roomNumber;
    private StringProperty guestName;
    private StringProperty checkInDate;
    private StringProperty checkOutDate;
    private StringProperty specialPreference;
    private StringProperty paymentStatus;
    private StringProperty bookingStatus;

    /**
     * Constructor initializes the BookingDetailsViewModel with a specific booking and prepares the necessary properties.
     *
     * @param booking the booking for which details will be managed
     */
    public BookingDetailsViewModel(Booking booking) {
        this.booking = booking;
        this.bookingService = new BookingService();
        initializeProperties();
    }

    /**
     * Initializes the properties for binding to the UI.
     * This includes booking details such as booking ID, room number, guest name, dates, and preferences.
     */
    private void initializeProperties() {
        bookingId = new SimpleStringProperty(booking.getBookingId());
        roomNumber = new SimpleStringProperty(booking.getRoomId());
        guestName = new SimpleStringProperty(booking.getGuest().getFullName());
        checkInDate = new SimpleStringProperty(booking.getInDate().toString());
        checkOutDate = new SimpleStringProperty(booking.getOutDate().toString());
        specialPreference = new SimpleStringProperty(booking.getSpecialPreference());
        paymentStatus = new SimpleStringProperty(booking.getPaymentStatus());
        bookingStatus = new SimpleStringProperty(booking.getBookingStatus());
    }

    /**
     * Saves any changes made to the booking, such as changes to special preferences.
     * Calls the BookingService to update the booking in the system.
     *
     * @return true if the changes were saved successfully, false otherwise
     */
    public boolean saveChanges() {
        booking.setSpecialPreference(specialPreference.get());
        return bookingService.updateBooking(booking);
    }

    // Getter methods for properties

    public StringProperty bookingIdProperty() {
        return bookingId;
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
    }

    public StringProperty guestNameProperty() {
        return guestName;
    }

    public StringProperty checkInDateProperty() {
        return checkInDate;
    }

    public StringProperty checkOutDateProperty() {
        return checkOutDate;
    }

    public StringProperty specialPreferenceProperty() {
        return specialPreference;
    }

    public StringProperty paymentStatusProperty() {
        return paymentStatus;
    }

    public StringProperty bookingStatusProperty() {
        return bookingStatus;
    }
}
