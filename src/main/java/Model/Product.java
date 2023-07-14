package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructs a Product with the specified attributes.
     *
     * @param id    The ID of the product.
     * @param name  The name of the product.
     * @param price The price of the product.
     * @param stock The stock quantity of the product.
     * @param min   The minimum allowed quantity of the product.
     * @param max   The maximum allowed quantity of the product.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Retrieves the ID of the product.
     *
     * @return The product ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the product.
     *
     * @param id The product ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return The product name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name The product name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the price of the product.
     *
     * @return The product price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price The product price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Retrieves the stock quantity of the product.
     *
     * @return The product stock quantity.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock quantity of the product.
     *
     * @param stock The product stock quantity to set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Retrieves the minimum allowed quantity of the product.
     *
     * @return The product minimum allowed quantity.
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the minimum allowed quantity of the product.
     *
     * @param min The product minimum allowed quantity to set.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Retrieves the maximum allowed quantity of the product.
     *
     * @return The product maximum allowed quantity.
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the maximum allowed quantity of the product.
     *
     * @param max The product maximum allowed quantity to set.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Adds an associated part to the product.
     *
     * @param part The part to add.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes an associated part from the product.
     *
     * @param selectedAssociatedPart The associated part to delete.
     * @return True if the associated part is successfully removed, false otherwise.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Retrieves all associated parts of the product.
     *
     * @return An ObservableList of all associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}