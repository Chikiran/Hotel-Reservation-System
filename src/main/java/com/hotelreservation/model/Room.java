package com.hotelreservation.model;

/**
 * Represents a room in the hotel, including details such as room ID, type, price, and availability.
 */
public class Room {

    private String roomId;
    private String roomType;
    private double roomPrice;
    private boolean availability;

    /**
     * Constructs a Room object with the specified details.
     *
     * @param roomId        the unique ID for the room
     * @param roomType      the type of the room (e.g., single, double, suite)
     * @param roomPrice     the price of the room per night
     * @param availability  indicates whether the room is available for booking
     */
    public Room(String roomId, String roomType, double roomPrice, boolean availability) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.availability = availability;
    }

    // Getters and setters

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public double getRoomPrice() { return roomPrice; }
    public void setRoomPrice(double roomPrice) { this.roomPrice = roomPrice; }

    public boolean isAvailability() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }
}
