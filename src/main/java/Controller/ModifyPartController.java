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
 * The ModifyPartController class is responsible for controlling the Modify Part form.
 * It allows users to modify an existing part by updating its information.
 * The class implements the Initializable interface for JavaFX initialization.
 *
 * Logical/Runtime Error: One of the encountered logical errors was related to missing information,
 * where the code did not handle the case when required fields were left empty or contained invalid data.
 * As a result, the code either crashed or failed to populate the necessary information. To address this issue,
 * I implemented error handling that displays an appropriate error message, guiding the user to enter valid information.
 *
 * Future Enhancement: In the future, a confirmation dialog can be added to prompt users to confirm before
 * saving the modifications to a part. This would provide an additional layer of user feedback and prevent
 * accidental changes.
 */
public class ModifyPartController implements Initializable {
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

    private Part part;

    /**
     * Sets the part to be modified.
     *
     * @param part The part to be modified.
     */
    public void setPart(Part part) {
        this.part = part;
        setFormData();
    }

    /**
     * Saves the modified part.
     *
     * @param event The event triggering the save action.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    private void onActionUpdatePart(ActionEvent event) throws IOException {
        try {
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

            int stockValue = Integer.parseInt(StockTxt.getText());

            if (stockValue < min || stockValue > max) {
                showAlert("Invalid Input", "Invalid Stock", "Stock must be between Min and Max values.");
                return;
            }

            if (name.isEmpty()) {
                showAlert("Invalid Input", "Missing Name", "Please enter a name for the part.");
                return;
            }

            if (inHouseRBtn.isSelected()) {
                // Update part type to InHouse
                if (!(part instanceof InHouse)) {
                    // Create a new InHouse part with the same ID
                    InHouse inHousePart = new InHouse(part.getId(), name, price, stock, min, max, Integer.parseInt(machineIDName));

                    int index = Inventory.getAllParts().indexOf(part);
                    Inventory.updatePart(index, inHousePart);

                    part = inHousePart;
                } else {
                    // Update the existing InHouse part
                    InHouse inHousePart = (InHouse) part;
                    inHousePart.setName(name);
                    inHousePart.setPrice(price);
                    inHousePart.setStock(stock);
                    inHousePart.setMin(min);
                    inHousePart.setMax(max);
                    inHousePart.setMachineId(Integer.parseInt(machineIDName));
                }
            } else if (outsourcedRBtn.isSelected()) {
                // Update part type to Outsourced
                if (!(part instanceof Outsourced)) {
                    // Create a new Outsourced part with the same ID
                    Outsourced outsourcedPart = new Outsourced(part.getId(), name, price, stock, min, max, machineIDName);

                    int index = Inventory.getAllParts().indexOf(part);
                    Inventory.updatePart(index, outsourcedPart);

                    part = outsourcedPart;
                } else {
                    // Update the existing Outsourced part
                    Outsourced outsourcedPart = (Outsourced) part;
                    outsourcedPart.setName(name);
                    outsourcedPart.setPrice(price);
                    outsourcedPart.setStock(stock);
                    outsourcedPart.setMin(min);
                    outsourcedPart.setMax(max);
                    outsourcedPart.setCompanyName(machineIDName);
                }
            }



            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("MainForm.fxml"));
            Parent root = loader.load();
            MainFormController mainFormController = loader.getController();
            mainFormController.updatePart(part);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
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
     * Checks if the given value is a valid name (consists of alphabetic characters only).
     *
     * @param value The value to check.
     * @return True if the value is a valid name, false otherwise.
     */
    private boolean isValidName(String value) {
        return value.matches("[a-zA-Z]+");
    }

    /**
     * Shows an alert dialog with the specified title, header, and content.
     *
     * @param title   The title of the alert dialog.
     * @param header  The header text of the alert dialog.
     * @param content The content text of the alert dialog.
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Handles the cancel action by returning to the main form.
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

        setFormData();
    }

    /**
     * Sets the form data with the values of the part being modified.
     */
    private void setFormData() {
        if (part != null) {
            idAuto.setText(Integer.toString(part.getId()));
            NameTxt.setText(part.getName());
            StockTxt.setText(Integer.toString(part.getStock()));
            PriceCostTxt.setText(Double.toString(part.getPrice()));
            MinTxt.setText(Integer.toString(part.getMin()));
            MaxTxt.setText(Integer.toString(part.getMax()));

            if (part instanceof InHouse) {
                inHouseRBtn.setSelected(true);
                outsourcedRBtn.setSelected(false);
                MachineIDLabel.setText("Machine ID");
                MachineIDTxt.setText(Integer.toString(((InHouse) part).getMachineId()));
            } else if (part instanceof Outsourced) {
                inHouseRBtn.setSelected(false);
                outsourcedRBtn.setSelected(true);
                MachineIDLabel.setText("Company Name");
                MachineIDTxt.setText(((Outsourced) part).getCompanyName());
            }
        }
    }

    /**
     * Handles the selection of the In-House radio button.
     *
     * @param event The event triggering the selection.
     */
    private void onInHouseSelected(ActionEvent event) {
        MachineIDLabel.setText("Machine ID");
    }

    /**
     * Handles the selection of the Outsourced radio button.
     *
     * @param event The event triggering the selection.
     */
    private void onOutsourcedSelected(ActionEvent event) {
        MachineIDLabel.setText("Company Name");
    }
}
