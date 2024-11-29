package com.hotelreservation.view;

import com.hotelreservation.App;
import com.hotelreservation.model.Room;
import com.hotelreservation.model.Staff;
import com.hotelreservation.viewmodel.RoomManagementViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for managing room details.
 * Handles the addition, updating, and deletion of rooms, as well as displaying room data in a table.
 */
public class RoomManagementViewController {

    private static final Logger logger = LoggerFactory.getLogger(RoomManagementViewController.class);

    @FXML
    private TableView<Room> roomsTable;

    @FXML
    private TableColumn<Room, String> roomNumberColumn;

    @FXML
    private TableColumn<Room, String> roomTypeColumn;

    @FXML
    private TableColumn<Room, Double> roomPriceColumn;

    @FXML
    private TableColumn<Room, Boolean> availabilityColumn;

    @FXML
    private TextField roomNumberField;

    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private TextField roomPriceField;

    @FXML
    private CheckBox availabilityCheckBox;

    private RoomManagementViewModel viewModel;
    private Staff currentStaff;

    /**
     * Initializes the RoomManagementViewController, setting up the table columns, room types,
     * and event listeners to populate fields when a room is selected.
     */
    public RoomManagementViewController() {
        viewModel = new RoomManagementViewModel();
    }

    /**
     * Initializes the table, combo box, and listener for room selection. Also loads room data.
     */
    @FXML
    private void initialize() {
        logger.info("Initializing RoomManagementViewController");

        roomNumberField.setEditable(false);
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        roomPriceColumn.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));

        roomsTable.setItems(viewModel.getRooms());
        roomTypeComboBox.setItems(viewModel.getRoomTypes());

        roomsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });

        viewModel.loadRooms();
    }

    /**
     * Populates the form fields with the selected room's details.
     *
     * @param room the room whose details will be populated into the form
     */
    private void populateFields(Room room) {
        roomNumberField.setText(room.getRoomId());
        roomTypeComboBox.setValue(room.getRoomType());
        roomPriceField.setText(String.valueOf(room.getRoomPrice()));
        availabilityCheckBox.setSelected(room.isAvailability());
    }

    /**
     * Handles adding a new room to the system, creating a new room with the entered details.
     * Displays success or error messages accordingly.
     */
    @FXML
    private void handleAddRoom() {
        logger.info("Adding new room");

        String roomType = roomTypeComboBox.getValue();
        double roomPrice = Double.parseDouble(roomPriceField.getText());
        boolean availability = availabilityCheckBox.isSelected();

        // Room number is auto-generated
        if (viewModel.addRoom(null, roomType, roomPrice, availability)) {
            clearFields();
            App.showInfoAlert("Success", "Room added successfully.");
        } else {
            App.showErrorAlert("Error", "Failed to add room.");
        }
        viewModel.loadRooms();
    }

    /**
     * Handles updating the details of the selected room in the table.
     * Displays success or error messages accordingly.
     */
    @FXML
    private void handleUpdateRoom() {
        logger.info("Updating room");
        Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            App.showErrorAlert("Error", "Please select a room to update.");
            return;
        }

        String roomNumber = roomNumberField.getText();
        String roomType = roomTypeComboBox.getValue();
        double roomPrice = Double.parseDouble(roomPriceField.getText());
        boolean availability = availabilityCheckBox.isSelected();

        if (viewModel.updateRoom(selectedRoom, roomNumber, roomType, roomPrice, availability)) {
            clearFields();
            App.showInfoAlert("Success", "Room updated successfully.");
        } else {
            App.showErrorAlert("Error", "Failed to update room.");
        }
    }

    /**
     * Handles the deletion of the selected room from the system.
     * Displays success or error messages accordingly.
     */
    @FXML
    private void handleDeleteRoom() {
        logger.info("Deleting room");
        Room selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            App.showErrorAlert("Error", "Please select a room to delete.");
            return;
        }

        if (viewModel.deleteRoom(selectedRoom)) {
            clearFields();
            App.showInfoAlert("Success", "Room deleted successfully.");
        } else {
            App.showErrorAlert("Error", "Failed to delete room.");
        }
    }

    /**
     * Clears all the fields in the room management form.
     */
    private void clearFields() {
        roomNumberField.clear();
        roomTypeComboBox.getSelectionModel().clearSelection();
        roomPriceField.clear();
        availabilityCheckBox.setSelected(false);
    }

    /**
     * Sets the current staff who is managing the room operations.
     *
     * @param staff the staff member managing the room operations
     */
    public void setCurrentStaff(Staff staff) {
        this.currentStaff = staff;
    }
}
