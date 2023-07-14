package Controller;

import Model.*;
import atiles.c482project.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The ModifyProductController class is responsible for controlling the Modify Product form.
 * It allows users to modify an existing product by updating its information and associated parts.
 *
 * Logical/Runtime Error: One of the logical errors that occured is related to name validation.
 * If a user enters an invalid string, it would result in an error. To address this, I have implemented a function
 * to validate the name input using the regular expression pattern '[a-zA-Z]+', which allows a wide variety of valid
 * string inputs.
 *
 * FUTURE ENHANCEMENT: As a future enhancement, a search functionality can be added to allow users to search for
 * available parts while modifying a product. This would enhance the user experience by providing a convenient way
 * to find and add parts without scrolling through a long list.
 */
public class  ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField idAuto;

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
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partIDCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInventoryLevel;

    @FXML
    private TableColumn<Part, Double> partPriceCostUnit;

    @FXML
    private TableView<Part> associatedPartsTableView;

    @FXML
    private TableColumn<Part, Integer> associatedIDCol;

    @FXML
    private TableColumn<Part, String> associatedNameCol;

    @FXML
    private TableColumn<Part, Integer> associatedInventoryCol;

    @FXML
    private TableColumn<Part, Double> associatedPriceCol;

    @FXML
    private TextField searchPart;

    private ObservableList<Part> availableParts;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private Product product;

    /**
     * Sets the product to be modified.
     *
     * @param product The product to be modified.
     */
    public void setProduct(Product product) {
        this.product = product;
        setFormData();
    }

    /**
     * Handles the search for parts.
     *
     * @param event The event triggering the search.
     */
    @FXML
    public void onActionSearchPart(ActionEvent event) {
        String searchText = searchPart.getText().trim();
        if (searchText.isEmpty()) {
            partsTableView.setItems(availableParts);
            return;
        }

        ObservableList<Part> searchResults = FXCollections.observableArrayList();
        try {
            int searchID = Integer.parseInt(searchText);
            Part foundPart = Inventory.lookupPart(searchID);
            if (foundPart != null) {
                searchResults.add(foundPart);
            }
        } catch (NumberFormatException e) {
            // Ignore the exception and proceed to search by name
        }

        if (searchResults.isEmpty()) {
            searchResults = Inventory.lookupPart(searchText);
        }

        if (searchResults.isEmpty()) {
            displayErrorMessage("Part not found,  No parts matching the search criteria were found.");
        } else {
            partsTableView.setItems(searchResults);
        }
    }

    /**
     * Adds a part to the associated parts list.
     *
     * @param event The event triggering the addition of the part.
     */
    public void onActionAddPart(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            if (associatedParts.contains(selectedPart)) {
                displayErrorMessage("Duplicate Part  The selected part is already associated with the product.");
            } else {
                associatedParts.add(selectedPart);
            }
        } else {
            displayErrorMessage("No Part Selected     Please select a part to add to the product.");
        }
    }

    /**
     * Removes an associated part from the product.
     *
     * @param event The event triggering the removal of the part.
     */
    @FXML
    private void onActionRemoveAssociatedPart(ActionEvent event) {
        Part selectedPart = associatedPartsTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm Removal");
            alert.setContentText("Are you sure you want to remove the associated part?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                displayConfirmationMessage("Associated Part has been successfully removed.");
            }
        } else {
            displayErrorMessage("No associated part selected.");
        }
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param message The error message to be displayed.
     */
    private void displayErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation message in an alert dialog.
     *
     * @param message The confirmation message to be displayed.
     */
    private void displayConfirmationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
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

    /**
     * Saves the modified product.
     *
     * @param event The event triggering the save action.
     * @throws IOException If an I/O error occurs.
     */
    @FXML
    public void onActionSaveProduct(ActionEvent event) throws IOException {
        try {
            String name = NameTxt.getText();
            int stock = Integer.parseInt(StockTxt.getText());
            double price = Double.parseDouble(PriceCostTxt.getText());
            int min = Integer.parseInt(MinTxt.getText());
            int max = Integer.parseInt(MaxTxt.getText());

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

            product.setName(name);
            product.setStock(stock);
            product.setPrice(price);
            product.setMin(min);
            product.setMax(max);

            product.getAllAssociatedParts().setAll(associatedParts);


            int index = Inventory.getAllProducts().indexOf(product);
            Inventory.updateProduct(index, product);

            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("MainForm.fxml"));
            Parent root = loader.load();
            MainFormController mainFormController = loader.getController();
            mainFormController.updateProduct(product);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current window
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idAuto.setDisable(true);

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        availableParts = FXCollections.observableArrayList();
        availableParts.addAll(Inventory.getAllParts());

        partsTableView.setItems(availableParts);
        associatedPartsTableView.setItems(associatedParts);
        setFormData();
    }

    private void setFormData() {
        if (product != null) {
            idAuto.setText(Integer.toString(product.getId()));
            NameTxt.setText(product.getName());
            StockTxt.setText(Integer.toString(product.getStock()));
            PriceCostTxt.setText(Double.toString(product.getPrice()));
            MinTxt.setText(Integer.toString(product.getMin()));
            MaxTxt.setText(Integer.toString(product.getMax()));

            associatedParts.addAll(product.getAllAssociatedParts());
        }
    }

}
