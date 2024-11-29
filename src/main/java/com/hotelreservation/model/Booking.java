package com.hotelreservation.model;

import java.time.LocalDate;

/**
 * Represents a booking made by a guest in the hotel.
 * Contains information such as booking ID, guest details, room details,
 * booking dates, payment status, and special preferences.
 */
public class Booking {

    private String bookingId;
    private String guestId;
    private String roomId;
    private String voucherNumber;
    private LocalDate inDate;
    private LocalDate outDate;
    private String specialPreference;
    private String paymentStatus;
    private String bookingStatus;
    private Guest guest;

    /**
     * Constructs a Booking object with the specified details.
     *
     * @param bookingId        the unique ID for this booking
     * @param guestId          the unique ID of the guest making the booking
     * @param roomId           the ID of the room being booked
     * @param voucherNumber    the voucher number for the booking (if applicable)
     * @param inDate           the check-in date for the booking
     * @param outDate          the check-out date for the booking
     * @param specialPreference special preferences or requests made by the guest
     * @param paymentStatus    the current payment status of the booking
     * @param bookingStatus    the current status of the booking (e.g., confirmed, canceled)
     */
    public Booking(String bookingId, String guestId, String roomId, String voucherNumber, LocalDate inDate, LocalDate outDate, String specialPreference, String paymentStatus, String bookingStatus) {
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.roomId = roomId;
        this.voucherNumber = voucherNumber;
        this.inDate = inDate;
        this.outDate = outDate;
        this.specialPreference = specialPreference;
        this.paymentStatus = paymentStatus;
        this.bookingStatus = bookingStatus;
    }

    // Getters and setters with one line each

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getGuestId() { return guestId; }
    public void setGuestId(String guestId) { this.guestId = guestId; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getVoucherNumber() { return voucherNumber; }
    public void setVoucherNumber(String voucherNumber) { this.voucherNumber = voucherNumber; }

    public LocalDate getInDate() { return inDate; }
    public void setInDate(LocalDate inDate) { this.inDate = inDate; }

    public LocalDate getOutDate() { return outDate; }
    public void setOutDate(LocalDate outDate) { this.outDate = outDate; }

    public String getSpecialPreference() { return specialPreference; }
    public void setSpecialPreference(String specialPreference) { this.specialPreference = specialPreference; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public Guest getGuest() { return guest; }
    public void setGuest(Guest guest) { this.guest = guest; }
}
