package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import atiles.c482project.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The AddPartController class is responsible for controlling the Add Part form.
 * It allows users to add a new part to the inventory by providing the necessary information.
 * The class implements the Initializable interface for JavaFX initialization.
 *
 * Logical/Runtime Error: Radio Button Validation: The code could not handle scenarios where neither the
 * In-House nor Outsourced radio button would be selected. Which could lead to an inconsistent state where no type of
 * part is specified. I implemented a try-catch method to ensure that if one of the radio buttons is not clicked,
 * a pop-up message is displayed, informing the user about the missing information and requesting them to enter valid data.
 *
 * Future Enhancement: Implement real-time validation as the user types in the input fields, displaying
 * immediate feedback on whether the entered data is valid or not. This would enhance the user experience and
 * reduce the chances of submitting incorrect data.
 */
public class AddPartController implements Initializable {

    Stage stage;
    Parent scene;


    @FXML
    private TextField idAuto;

    @FXML
    private RadioButton inHouseRBtn;

    @FXML
    private RadioButton outsourcedRBtn;

    @FXML
    private TextField NameTxt;

    @FXML
    private TextField StockTxt;

    @FXML
    private TextField PriceCostTxt;

    @FXML
    private TextField MaxTxt;

    @FXML
    private TextField MinTxt;

    @FXML
    private Label MachineIDLabel;

    @FXML
    private TextField MachineIDTxt;

    private ToggleGroup toggleGroup;

    /**
     * Gets the next available part ID.
     *
     * @return The next available part ID.
     */
    public static int getNextPartId() {
        int maxPartId = 0;
        for (Part part : Inventory.getAllParts()) {
            if (part.getId() > maxPartId) {
                maxPartId = part.getId();
            }
        }
        return maxPartId + 1;
    }

    /**
     * Saves the part and returns to the main form.
     *
     * @param event The event triggering the save action.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    private void onActionSavePart(ActionEvent event) throws IOException {
        try {
            // Get data from form
            String name = NameTxt.getText();
            int stock = Integer.parseInt(StockTxt.getText());
            double price = Double.parseDouble(PriceCostTxt.getText());
            int min = Integer.parseInt(MinTxt.getText());
            int max = Integer.parseInt(MaxTxt.getText());
            String machineIDName = MachineIDTxt.getText();

            if (!inHouseRBtn.isSelected() && !outsourcedRBtn.isSelected()) {
                showAlert("Invalid Input", "No Selection", "Please select either In-House or Outsourced.");
                return;
            }
            if (!isInteger(StockTxt.getText())) {
                showAlert("Invalid Input", "Invalid Stock", "Stock must be a number.");
                return;
            }
            if (!isDouble(PriceCostTxt.getText())) {
                showAlert("Invalid Input", "Invalid Price", "Price must be a numeric value.");
                return;
            }
            if (!isInteger(MinTxt.getText())) {
                showAlert("Invalid Input", "Invalid Min", "Min must be a number.");
                return;
            }
            if (!isInteger(MaxTxt.getText())) {
                showAlert("Invalid Input", "Invalid Max", "Max must be a number.");
                return;
            }
            if (inHouseRBtn.isSelected() && !isInteger(machineIDName)) {
                showAlert("Invalid Input", "Invalid Machine ID", "Machine ID must be a number.");
                return;
            }
            if (!isValidName(name)) {
                showAlert("Invalid Input", "Invalid Name", "Name must be a string value.");
                return;
            }

            // Validate additional conditions
            int stockValue = Integer.parseInt(StockTxt.getText());

            if (stockValue < min || stockValue > max) {
                showAlert("Invalid Input", "Invalid Stock", "Stock must be between Min and Max values.");
                return;
            }

            // Validate the data
            if (name.isEmpty()) {
                showAlert("Invalid Input", "Missing Name", "Please enter a name for the part.");
                return;
            }

            // Save the data to the storage location
            Part part;
            if (inHouseRBtn.isSelected()) {
                int machineID = Integer.parseInt(machineIDName);
                part = new InHouse(getNextPartId(), name, price, stock, min, max, machineID);
            }
            else {
                String companyName = MachineIDTxt.getText();
                part = new Outsourced(getNextPartId(), name, price, stock, min, max, companyName);
            }

            Inventory.addPart(part);

            // Return to the main form
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(MainApplication.class.getResource("MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Missing Information", "Please enter valid information.");
        }
    }

    /**
     * Checks if the given value is an integer.
     *
     * @param value The value to check.
     * @return True if the value is an integer, false otherwise.
     */
    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given value is a double.
     *
     * @param value The value to check.
     * @return True if the value is a double, false otherwise.
     */
    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given value is a valid name (contains only letters).
     *
     * @param value The value to check.
     * @return True if the value is a valid name, false otherwise.
     */
    private boolean isValidName(String value) {
        return value.matches("[a-zA-Z]+");
    }

    /**
     * Shows an alert with the specified title, header, and content.
     *
     * @param title   The title of the alert.
     * @param header  The header text of the alert.
     * @param content The content text of the alert.
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Cancels the add part operation and returns to the main form.
     *
     * @param event The event triggering the cancel action.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(MainApplication.class.getResource("MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleGroup = new ToggleGroup();
        inHouseRBtn.setToggleGroup(toggleGroup);
        outsourcedRBtn.setToggleGroup(toggleGroup);

        inHouseRBtn.setOnAction(this::onInHouseSelected);
        outsourcedRBtn.setOnAction(this::onOutsourcedSelected);
        idAuto.setDisable(true);
    }

    /**
     * Handles the event when the In-House radio button is selected.
     *
     * @param event The eventtriggering the selection.
     */
    private void onInHouseSelected(ActionEvent event) {
        MachineIDLabel.setText("Machine ID");
    }

    /**
     * Handles the event when the Outsourced radio button is selected.
     *
     * @param event The event triggering the selection.
     */
    private void onOutsourcedSelected(ActionEvent event) {
        MachineIDLabel.setText("Company Name");
    }
}
