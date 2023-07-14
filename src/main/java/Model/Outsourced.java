package Model;

public class Outsourced extends Part {
    private String companyName;

    /**
     * Constructs an Outsourced part with the specified attributes.
     *
     * @param id           The ID of the part.
     * @param name         The name of the part.
     * @param price        The price of the part.
     * @param stock        The stock quantity of the part.
     * @param min          The minimum allowed quantity of the part.
     * @param max          The maximum allowed quantity of the part.
     * @param companyName The name of the company associated with the Outsourced part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Retrieves the name of the company associated with the Outsourced part.
     *
     * @return The company name.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the name of the company associated with the Outsourced part.
     *
     * @param companyName The company name to set.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}