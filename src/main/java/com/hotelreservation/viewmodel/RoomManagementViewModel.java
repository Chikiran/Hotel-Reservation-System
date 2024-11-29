
package com.hotelreservation.viewmodel;

import com.hotelreservation.model.Room;
import com.hotelreservation.service.RoomService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RoomManagementViewModel {

    private static final Logger logger = LoggerFactory.getLogger(RoomManagementViewModel.class);

    private RoomService roomService;
    private ObservableList<Room> rooms;
    private ObservableList<String> roomTypes;

    public RoomManagementViewModel() {
        roomService = new RoomService();
        rooms = FXCollections.observableArrayList();
        roomTypes = FXCollections.observableArrayList("Standard", "Deluxe", "Suite");
    }

    public ObservableList<Room> getRooms() {
        return rooms;
    }

    public ObservableList<String> getRoomTypes() {
        return roomTypes;
    }

    public void loadRooms() {
        logger.info("Loading rooms");
        List<Room> loadedRooms = roomService.getAllRooms();
        rooms.setAll(loadedRooms);
    }

    public boolean addRoom(String roomNumber, String roomType, double roomPrice, boolean availability) {
        logger.info("Adding room with auto-generated ID");

        Room newRoom = new Room(null, roomType, roomPrice, availability);
        if (roomService.addRoom(newRoom)) {
            loadRooms();
            return true;
        }
        return false;
    }


    public boolean updateRoom(Room room, String roomNumber, String roomType, double roomPrice, boolean availability) {
        logger.info("Updating room: {}", room.getRoomId());
        room.setRoomId(roomNumber);
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        room.setAvailability(availability);
        if (roomService.updateRoom(room)) {
            loadRooms(); // Reload to reflect changes
            return true;
        }
        return false;
    }

    public boolean deleteRoom(Room room) {
        logger.info("Deleting room: {}", room.getRoomId());
        if (roomService.deleteRoom(room.getRoomId())) {
            rooms.remove(room);
            return true;
        }
        return false;
    }
}
