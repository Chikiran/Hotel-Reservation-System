package com.hotelreservation.viewmodel;

import com.hotelreservation.model.Booking;
import com.hotelreservation.service.BookingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ViewModel class for managing bookings on the main view.
 * Provides functionality to load and search bookings based on a search term.
 */
public class MainViewModel {

    private BookingService bookingService;
    private ObservableList<Booking> bookings;
    private List<Booking> allBookings;

    /**
     * Constructor initializes the MainViewModel with a BookingService instance and an observable list for bookings.
     */
    public MainViewModel() {
        bookingService = new BookingService();
        bookings = FXCollections.observableArrayList();
    }

    /**
     * Returns the observable list of bookings to be displayed in the UI.
     *
     * @return an observable list of bookings
     */
    public ObservableList<Booking> getBookings() {
        return bookings;
    }

    /**
     * Loads all bookings from the booking service and updates the bookings list.
     */
    public void loadBookings() {
        allBookings = bookingService.getAllBookings();
        bookings.setAll(allBookings);
    }

    /**
     * Filters the bookings based on the search term. The search is case-insensitive and checks various booking attributes.
     *
     * @param searchTerm the search term to filter bookings
     */
    public void searchBookings(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            bookings.setAll(allBookings);
        } else {
            List<Booking> filteredBookings = allBookings.stream()
                    .filter(booking ->
                            booking.getBookingId().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                    booking.getGuest().getFullName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                    booking.getRoomId().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                    booking.getVoucherNumber().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                    booking.getInDate().toString().contains(searchTerm) ||
                                    booking.getOutDate().toString().contains(searchTerm)
                    )
                    .collect(Collectors.toList());
            bookings.setAll(filteredBookings);
        }
    }
}
