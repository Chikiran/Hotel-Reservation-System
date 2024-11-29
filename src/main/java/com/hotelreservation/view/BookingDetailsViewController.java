package com.hotelreservation.view;

import com.hotelreservation.App;
import com.hotelreservation.model.Booking;
import com.hotelreservation.model.Staff;
import com.hotelreservation.viewmodel.BookingDetailsViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for managing the booking details view.
 * Handles user interactions for viewing and editing booking details, including the visibility of buttons based on staff roles.
 */
public class BookingDetailsViewController {

    @FXML
    private Label bookingIdLabel;
    @FXML
    private Label roomNumberLabel;
    @FXML
    private Label guestNameLabel;
    @FXML
    private Label checkInDateLabel;
    @FXML
    private Label checkOutDateLabel;
    @FXML
    private TextField specialPreferenceField;
    @FXML
    private Label paymentStatusLabel;
    @FXML
    private Label bookingStatusLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;

    private BookingDetailsViewModel viewModel;
    private Staff currentStaff;

    /**
     * Sets the booking to be displayed in the view and binds the data to UI components.
     *
     * @param booking the booking to display
     */
    public void setBooking(Booking booking) {
        viewModel = new BookingDetailsViewModel(booking);
        bindData();
    }

    /**
     * Sets the current staff member and adjusts the visibility of the edit button based on their position.
     *
     * @param staff the current staff member
     */
    public void setCurrentStaff(Staff staff) {
        this.currentStaff = staff;
        editButton.setVisible(currentStaff.getPosition().equals("Manager"));
    }

    /**
     * Binds the booking data to the UI components.
     */
    private void bindData() {
        bookingIdLabel.textProperty().bind(viewModel.bookingIdProperty());
        roomNumberLabel.textProperty().bind(viewModel.roomNumberProperty());
        guestNameLabel.textProperty().bind(viewModel.guestNameProperty());
        checkInDateLabel.textProperty().bind(viewModel.checkInDateProperty());
        checkOutDateLabel.textProperty().bind(viewModel.checkOutDateProperty());
        specialPreferenceField.textProperty().bindBidirectional(viewModel.specialPreferenceProperty());
        paymentStatusLabel.textProperty().bind(viewModel.paymentStatusProperty());
        bookingStatusLabel.textProperty().bind(viewModel.bookingStatusProperty());
    }

    /**
     * Makes the special preference field editable and shows the save button.
     */
    @FXML
    private void handleEdit() {
        specialPreferenceField.setEditable(true);
        saveButton.setVisible(true);
    }

    /**
     * Saves the changes made to the booking details and provides feedback to the user.
     * If the changes are successfully saved, it hides the save button and displays a success message.
     * If saving fails, it displays an error message.
     */
    @FXML
    private void handleSave() {
        if (viewModel.saveChanges()) {
            specialPreferenceField.setEditable(false);
            saveButton.setVisible(false);
            App.showInfoAlert("Success", "Booking details updated successfully.");
        } else {
            App.showErrorAlert("Error", "Failed to update booking details.");
        }
    }

    /**
     * Closes the current booking details view window.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) bookingIdLabel.getScene().getWindow();
        stage.close();
    }
}
