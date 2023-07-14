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

import static Model.Inventory.getAllParts;
import static Model.Inventory.lookupPart;

/**
 * The MainFormController class is responsible for controlling the main form of the application.
 * It displays the lists of parts and products and provides various functionality such as searching,
 * adding, modifying, and deleting parts and products.
 * The class implements the Initializable interface for JavaFX initialization.
 *
 * Logical/Runtime Error:A potential logical runtime error that could have occurred is the failure to update
 * the displayed table view after modifying a part or product. However, this error has been fixed.In the
 * MainFormController, the methods updatePart(Part part) and updateProduct(Product product) are implemented to update
 * the respective items in the table view when a modification is made. These methods locate the index of the modified
 * item in the table view's data list and replace it with the updated item. By doing so, the table view is correctly
 * synchronized with the modified data, ensuring that any changes made to a part or product are
 * immediately reflected in the UI.
 *
 * Future Enhancement: A potential future enhancement could be the addition of pagination or infinite scrolling for
 * the parts and products tables. This would be beneficial when dealing with a large number of items and would
 * improve performance by loading only a subset of the items at a time.
 */
public class MainFormController implements Initializable {

    Stage stage;
    Parent scene;

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
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> productIDCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Integer> productInventoryLevel;

    @FXML
    private TableColumn<Product, Double> productPriceCostUnit;

    @FXML
    private TextField searchPart;

    @FXML
    private TextField searchProduct;

    /**
     * Updates the specified part in the parts table view.
     *
     * @param part The part to update.
     */
    public void updatePart(Part part) {
        int index = partsTableView.getItems().indexOf(part);
        if (index >= 0) {
            partsTableView.getItems().set(index, part);
        }
    }

    /**
     * Updates the specified product in the products table view.
     *
     * @param product The product to update.
     */
    public void updateProduct(Product product) {
        int index = productsTableView.getItems().indexOf(product);
        if (index >= 0) {
            productsTableView.getItems().set(index, product);
        }
    }

    /**
     * Handles the action event for modifying a part.
     *
     * @param event The action event.
     * @throws IOException If an I/O exception occurs during the operation.
     */
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("ModifyPartForm.fxml"));
            Parent root = loader.load();

            ModifyPartController modifyPartController = loader.getController();
            modifyPartController.setPart(selectedPart);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        } else {
            displayErrorMessage("No part selected");
        }
    }

    /**
     * Handles the action event for adding a part.
     *
     * @param event The action event.
     * @throws IOException If an I/O exception occurs during the operation.
     */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(MainApplication.class.getResource("AddPartForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Handles the action event for adding a product.
     *
     * @param event The action event.
     * @throws IOException If an I/O exception occurs during the operation.
     */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(MainApplication.class.getResource("AddProductForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Handles the action event for modifying a product.
     *
     * @param event The action event.
     * @throws IOException If an I/O exception occurs during the operation.
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("ModifyProductForm.fxml"));
            Parent root = loader.load();

            ModifyProductController modifyProductController = loader.getController();
            modifyProductController.setProduct(selectedProduct);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        } else {
            displayErrorMessage("No product selected");
        }
    }

    /**
     * Handles the action event for deleting a part.
     *
     * @param event The action event.
     * @throws IOException If an I/O exception occurs during the operation.
     */
    @FXML
    void onActionDeletePart(ActionEvent event) throws IOException {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to delete the part");

            Optional<ButtonType> result = alert.showAndWait();
            boolean deleted = Inventory.deletePart(selectedPart);
            if (result.isPresent() && result.get() == ButtonType.OK && deleted) {
                partsTableView.getItems().remove(selectedPart);
                displayConfirmationMessage("Part has been successfully removed");

            } else {
                displayErrorMessage("Failed to delete part");
            }
        } else {
            displayErrorMessage("No part selected");
        }
    }

    /**
     * Handles the action event for deleting a product.
     *
     * @param event The action event.
     * @throws IOException If an I/O exception occurs during the operation.
     */
    @FXML
    void onActionDeleteProduct(ActionEvent event) throws IOException {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to delete the product");

            Optional<ButtonType> result = alert.showAndWait();
            boolean deleted = Inventory.deleteProduct(selectedProduct);
            if (result.isPresent() && result.get() == ButtonType.OK && deleted) {
                productsTableView.getItems().remove(selectedProduct);
                displayConfirmationMessage("Product has been successfully removed");

            } else {
                displayErrorMessage("Failed to delete product");
            }
        } else {
            displayErrorMessage("No product selected");
        }
    }

    /**
     * Handles the action event for exiting the application.
     *
     * @param event The action event.
     */
    @FXML
    void onActionExit(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Handles the action event for searching a part.
     *
     * @param event The action event.
     */
    @FXML
    public void onActionSearchPart(ActionEvent event) {
        String searchText = searchPart.getText().trim();
        if (searchText.isEmpty()) {
            partsTableView.setItems(Inventory.getAllParts());
            return;
        }

        try {
            int searchID = Integer.parseInt(searchText);
            Part foundPart = Inventory.lookupPart(searchID);
            ObservableList<Part> searchIDResults = FXCollections.observableArrayList();
            if(foundPart != null) {
                searchIDResults.add(foundPart);
                partsTableView.setItems(searchIDResults);
                return;
            }
        } catch (NumberFormatException e) {
            // Ignore the exception and proceed to search by name
        }

        ObservableList<Part> searchResults = Inventory.lookupPart(searchText);
        if (searchResults.isEmpty()) {
            displayErrorMessage("Part not found");
        } else {
            partsTableView.setItems(searchResults);
        }
    }

    /**
     * Handles the action event for searching a product.
     *
     * @param event The action event.
     */
    @FXML
    public void onActionSearchProduct(ActionEvent event) {
        String searchText = searchProduct.getText().trim();
        if (searchText.isEmpty()) {
            productsTableView.setItems(Inventory.getAllProducts());
            return;
        }

        try {
            int searchID = Integer.parseInt(searchText);
            Product foundProduct = Inventory.lookupProduct(searchID);
            ObservableList<Product> searchIDResults = FXCollections.observableArrayList();
            if (foundProduct != null) {
                searchIDResults.add(foundProduct);
                productsTableView.setItems(searchIDResults);
                return;
            }
        } catch (NumberFormatException e) {
            // Ignore the exception and proceed to search by name
        }

        ObservableList<Product> searchResults = Inventory.lookupProduct(searchText);
        if (searchResults.isEmpty()) {
            displayErrorMessage("Product not found");
        } else {
            productsTableView.setItems(searchResults);
        }
    }

    private void displayErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void displayConfirmationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCostUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        ObservableList<Part> partsList = FXCollections.observableArrayList();
        partsList.addAll(getAllParts());

        ObservableList<Product> productList = FXCollections.observableArrayList();
        productList.addAll(Inventory.getAllProducts());

        partsTableView.setItems(partsList);
        productsTableView.setItems(productList);
    }
}