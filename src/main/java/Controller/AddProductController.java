package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
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
 * The AddProductController class is responsible for controlling the Add Product form.
 * It allows users to add a new product to the inventory by providing the necessary information
 * and selecting associated parts.
 * The class implements the Initializable interface for JavaFX initialization.
 *
 * Logical/Runtime Error: A runtime error that arose when using the getNextProductId was that I
 * only had it add 1 to the id when a new product was created.In the presence of existing products,
 * this assumption may lead to undesirable consequences, such as the creation of duplicate IDs and
 * potential disruptions in the overall functionality of the code. Which is why I created a for loop that would
 * check and make sure that the id was not already being used. Then it would still add 1 to an ID being used. Which
 * would create a unique id.
 *
 * Future Enhancement: Implement a confirmation dialog when the user attempts to save the product,
 * asking for confirmation before proceeding. This can help prevent accidental product creation or modification.
 */
public class AddProductController implements Initializable {

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

    /**
     * Gets the next available product ID.
     *
     * @return The next available product ID.
     */
    public static int getNextProductId() {
        int maxId = 0;
        for (Product product : Inventory.getAllProducts()) {
            if (product.getId() > maxId) {
                maxId = product.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Searches for parts based on the search criteria.
     *
     * @param event The event triggering the search action.
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
            displayErrorMessage("Part not found. No parts matching the search criteria were found.");
        } else {
            partsTableView.setItems(searchResults);
        }
    }

    /**
     * Adds the selected part to the associated parts list.
     *
     * @param event The event triggering the add action.
     */
    public void onActionAddPart(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            if (associatedParts.contains(selectedPart)) {
                displayErrorMessage("Duplicate Part. The selected part is already associated with the product.");
            } else {
                associatedParts.add(selectedPart);
            }
        } else {
            displayErrorMessage("No Part Selected. Please select a part to add to the product.");
        }
    }

    /**
     * Removes the selected associated part from the associated parts list.
     *
     * @param event The event triggering the remove action.
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
     * Saves the product and returns to the main form.
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

            Product product = new Product(getNextProductId(), name, price, stock, min, max);
            for (Part part : associatedParts) {
                product.addAssociatedPart(part);
            }

            Inventory.addProduct(product);

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
     * Checks if the given values a valid name (contains only letters).
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
     * Displays an error message in an alert dialog.
     *
     * @param message The error message to display.
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
     * @param message The confirmation message to display.
     */
    private void displayConfirmationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Cancels the add product operation and returns to the main form.
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
    }
}
