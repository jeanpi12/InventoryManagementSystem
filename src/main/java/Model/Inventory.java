package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a new part to the inventory.
     *
     * @param newPart The part to add.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a new product to the inventory.
     *
     * @param newProduct The product to add.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Looks up a part in the inventory by its ID.
     *
     * @param partID The ID of the part to look up.
     * @return The found Part object, or null if not found.
     */
    public static Part lookupPart(int partID) {
        for (Part part : allParts) {
            if (part.getId() == partID) {
                return part;
            }
        }
        return null;
    }

    /**
     * Looks up a product in the inventory by its ID.
     *
     * @param productID The ID of the product to look up.
     * @return The found Product object, or null if not found.
     */
    public static Product lookupProduct(int productID) {
        for (Product product : allProducts) {
            if (product.getId() == productID) {
                return product;
            }
        }
        return null;
    }

    /**
     * Searches for parts that have a partial name match.
     *
     * @param partName The partial name of the parts to search.
     * @return A list of parts with matching names, or an empty list if no matches found.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> matchingParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                matchingParts.add(part);
            }
        }
        return matchingParts;
    }

    /**
     * Searches for products that have a partial name match.
     *
     * @param productName The partial name of the products to search.
     * @return A list of products with matching names, or an empty list if no matches found.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> matchingProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                matchingProducts.add(product);
            }
        }
        return matchingProducts;
    }

    /**
     * Updates a part at the specified index in the inventory.
     *
     * @param index        The index of the part to update.
     * @param selectedPart The updated part object.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates a product at the specified index in the inventory.
     *
     * @param index           The index of the product to update.
     * @param selectedProduct The updated product object.
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Deletes a part from the inventory.
     *
     * @param selectedPart The part to delete.
     * @return True if the part is successfully deleted, false otherwise.
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * Deletes a product from the inventory.
     *
     * @param selectedProduct The product to delete.
     * @return True if the product is successfully deleted, false otherwise.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * Returns all parts in the inventory.
     *
     * @return An observable list of all parts.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Returns all products in the inventory.
     *
     * @return An observable list of all products.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
