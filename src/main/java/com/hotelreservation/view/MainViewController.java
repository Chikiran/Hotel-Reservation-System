package com.hotelreservation.view;

import com.hotelreservation.App;
import com.hotelreservation.model.Booking;
import com.hotelreservation.model.Room;
import com.hotelreservation.model.Staff;
import com.hotelreservation.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Controller class for managing the main view.
 * Handles the display and management of bookings, room reservations, and other functionalities depending on staff roles.
 */
public class MainViewController {

    private static final Logger logger = LoggerFactory.getLogger(MainViewController.class);

    @FXML
    private TableView<Booking> reservationsTable;

    @FXML
    private TableColumn<Booking, String> roomNumberColumn;

    @FXML
    private TableColumn<Booking, String> roomStateColumn;

    @FXML
    private TableColumn<Booking, String> roomTypeColumn;

    @FXML
    private TableColumn<Booking, String> guestNameColumn;

    @FXML
    private TableColumn<Booking, String> voucherNumberColumn;

    @FXML
    private TableColumn<Booking, String> inDateColumn;

    @FXML
    private TableColumn<Booking, String> outDateColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button newReservationButton;

    @FXML
    private Button manageRoomsButton;

    private MainViewModel mainViewModel;
    private Staff currentStaff;

    /**
     * Initializes the main view, sets up the table columns, and binds data from the view model.
     * It also adds listeners for selecting bookings and searching bookings.
     */
    public MainViewController() {
        mainViewModel = new MainViewModel();
    }

    /**
     * Initializes the UI components, binds data to the reservations table, and loads existing bookings.
     * Also sets up search and selection functionalities for bookings.
     */
    @FXML
    private void initialize() {
        logger.info("Initializing MainViewController");

        // Initialize the reservations table columns
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        roomStateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomId")); // You may need to adjust this if room type is stored separately
        guestNameColumn.setCellValueFactory(cellData -> cellData.getValue().getGuest().fullNameProperty());
        voucherNumberColumn.setCellValueFactory(new PropertyValueFactory<>("voucherNumber"));
        inDateColumn.setCellValueFactory(new PropertyValueFactory<>("inDate"));
        outDateColumn.setCellValueFactory(new PropertyValueFactory<>("outDate"));

        // Bind data to table from viewmodel
        reservationsTable.setItems(mainViewModel.getBookings());

        // Handle selection of bookings in the table
        reservationsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showBookingDetails(newSelection);
            }
        });

        // Listener for the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("Searching bookings with term: {}", newValue);
            mainViewModel.searchBookings(newValue);
        });

        // Load the bookings from the viewmodel
        mainViewModel.loadBookings();
    }

    /**
     * Opens a new reservation window.
     */
    @FXML
    private void handleNewReservation() {
        logger.info("Opening new reservation window");
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("new-reservation-view.fxml"));
            Parent root = loader.load();
            NewReservationViewController controller = loader.getController();
            controller.setCurrentStaff(currentStaff);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("New Reservation");
            stage.show();
        } catch (IOException e) {
            logger.error("Failed to open new reservation window", e);
            App.showErrorAlert("Error", "Failed to open new reservation window.");
        }
    }

    /**
     * Opens a room management window for managing rooms.
     */
    @FXML
    private void handleManageRooms() {
        logger.info("Opening room management window");
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("room-management-view.fxml"));
            Parent root = loader.load();
            RoomManagementViewController controller = loader.getController();
            controller.setCurrentStaff(currentStaff);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Room Management");
            stage.show();
        } catch (IOException e) {
            logger.error("Failed to open room management window", e);
            App.showErrorAlert("Error", "Failed to open room management window.");
        }
    }

    /**
     * Displays detailed information for the selected booking.
     *
     * @param booking the booking for which details will be shown
     */
    private void showBookingDetails(Booking booking) {
        logger.info("Showing booking details for booking ID: {}", booking.getBookingId());
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("booking-details-view.fxml"));
            Parent root = loader.load();
            BookingDetailsViewController controller = loader.getController();
            controller.setBooking(booking);
            controller.setCurrentStaff(currentStaff);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Booking Details");
            stage.show();
        } catch (IOException e) {
            logger.error("Failed to open booking details window", e);
            App.showErrorAlert("Error", "Failed to open booking details window.");
        }
    }

    /**
     * Sets the current staff and updates the UI based on their role.
     *
     * @param staff the staff member whose role determines the UI visibility
     */
    public void setCurrentStaff(Staff staff) {
        logger.info("Setting current staff: {}", staff.getStaffId());
        this.currentStaff = staff;
        updateUIBasedOnRole();
    }

    /**
     * Updates the visibility of the UI components based on the staff's role.
     * Only managers can manage rooms, while all staff can make new reservations.
     */
    private void updateUIBasedOnRole() {
        boolean isManager = "Manager".equalsIgnoreCase(currentStaff.getPosition());
        newReservationButton.setVisible(true); // All staff can make reservations
        manageRoomsButton.setVisible(isManager); // Only managers can manage rooms
        logger.info("UI updated based on staff role. Is manager: {}", isManager);
    }
}
