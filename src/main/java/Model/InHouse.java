package Model;

public class InHouse extends Part {
    private int machineId;

    /**
     * Constructs an InHouse part with the specified attributes.
     *
     * @param id        The ID of the part.
     * @param name      The name of the part.
     * @param price     The price of the part.
     * @param stock     The stock quantity of the part.
     * @param min       The minimum allowed quantity of the part.
     * @param max       The maximum allowed quantity of the part.
     * @param machineId The machine ID associated with the InHouse part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Retrieves the machine ID associated with the InHouse part.
     *
     * @return The machine ID.
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Sets the machine ID associated with the InHouse part.
     *
     * @param machineId The machine ID to set.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}