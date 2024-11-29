package com.hotelreservation.view;

import com.hotelreservation.App;
import com.hotelreservation.model.Booking;
import com.hotelreservation.model.Guest;
import com.hotelreservation.model.Room;
import com.hotelreservation.model.Staff;
import com.hotelreservation.viewmodel.NewReservationViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Controller class for managing the new reservation view.
 * Handles the search for available rooms, creating a new reservation, and managing the guest's details.
 */
public class NewReservationViewController {

    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    @FXML
    private TableView<Room> availableRoomsTable;

    @FXML
    private TextField guestNameField;

    @FXML
    private TextField guestContactField;

    @FXML
    private TextField voucherNumberField;

    @FXML
    private TextArea specialPreferenceArea;

    private NewReservationViewModel viewModel;
    private Staff currentStaff;

    /**
     * Initializes the NewReservationViewController, sets up the available room types and available rooms.
     */
    public NewReservationViewController() {
        viewModel = new NewReservationViewModel();
    }

    /**
     * Initializes the room type ComboBox and binds available rooms table with data from the view model.
     */
    @FXML
    private void initialize() {
        roomTypeComboBox.setItems(viewModel.getRoomTypes());
        availableRoomsTable.setItems(viewModel.getAvailableRooms());
        // Set up table columns for room details (room number, type, price, etc.)
    }

    /**
     * Handles the search for available rooms based on selected room type, check-in, and check-out dates.
     * Shows an error alert if any of the required fields are missing or invalid.
     */
    @FXML
    private void handleSearchRooms() {
        String roomType = roomTypeComboBox.getValue();
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();

        if (roomType == null || checkInDate == null || checkOutDate == null) {
            App.showErrorAlert("Error", "Please fill in all fields.");
            return;
        }

        if (checkOutDate.isBefore(checkInDate)) {
            App.showErrorAlert("Error", "Check-out date must be after check-in date.");
            return;
        }

        viewModel.searchAvailableRooms(roomType, checkInDate, checkOutDate);
    }

    /**
     * Handles the creation of a reservation once a room is selected, and guest details are entered.
     * Shows an error alert if any required information is missing.
     */
    @FXML
    private void handleMakeReservation() {
        Room selectedRoom = availableRoomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            App.showErrorAlert("Error", "Please select a room.");
            return;
        }

        String guestName = guestNameField.getText();
        String guestContact = guestContactField.getText();
        String voucherNumber = voucherNumberField.getText();
        String specialPreference = specialPreferenceArea.getText();

        if (guestName.isEmpty() || guestContact.isEmpty()) {
            App.showErrorAlert("Error", "Please enter guest information.");
            return;
        }

        Guest guest = new Guest(null, guestName, "", "", "", guestContact);
        Booking booking = viewModel.createBooking(selectedRoom, guest, checkInDatePicker.getValue(),
                checkOutDatePicker.getValue(), voucherNumber, specialPreference);

        if (booking != null) {
            App.showInfoAlert("Success", "Reservation created successfully.");
            closeWindow();
        } else {
            App.showErrorAlert("Error", "Failed to create reservation.");
        }
    }

    /**
     * Closes the current reservation window.
     */
    private void closeWindow() {
        Stage stage = (Stage) roomTypeComboBox.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the current staff member who is handling the reservation.
     *
     * @param staff the staff member handling the reservation
     */
    public void setCurrentStaff(Staff staff) {
        this.currentStaff = staff;
    }
}
