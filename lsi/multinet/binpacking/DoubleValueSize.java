package lsi.multinet.binpacking;



/**
 * An one dimensional impl of Size for <code>double</code> values,
 * based on code by Sven Kiesewetter.
 *
 * @author Leandro Soares Indrusiak
 * @version 1.0 (York, 09/09/2020)
 * 
 */

public class DoubleValueSize implements Size {

    /**
	 * 
	 */

	private final double value;

    /**
     * Creates a new DoubleValueSize
     *
     * @param value the double value
     */
    public DoubleValueSize(double value) {
        this.value = value;
    }

    /**
     * Compares this DoubleValueSize's double value against another's double value
     *
     * @param otherDoubleValueSize the other DoubleValueSize
     * @return -1 if this double value is smaller than other's double value<br>
     *         0 if this double value is equal to than other's double value<br>
     *         1 if this double value is larger than other's double value
     * @see de.bugbusters.binpacking.model.Size#compareTo(Object)
     */
    public int compareTo(Object otherDoubleValueSize) {
        if (!(otherDoubleValueSize instanceof DoubleValueSize)) {
            throw new ClassCastException("#compare() parameter is not an instance of " + DoubleValueSize.class.getName());
        }
        DoubleValueSize other = (DoubleValueSize) otherDoubleValueSize;
        if (getValue() < other.getValue()) {
            return -1;
        }
        if (getValue() > other.getValue()) {
            return 1;
        }
        return 0;
    }

    /**
     * Checks if this DoubleValueSize's double value is equal to another's double value
     *
     * @param otherDoubleValueSize the other DoubleValueSize
     * @return true if this DoubleValueSize's double value is equal to another's double value
     */
    public boolean equals(Object otherDoubleValueSize) {
        if (this == otherDoubleValueSize) return true;
        if (otherDoubleValueSize == null || getClass() != otherDoubleValueSize.getClass()) return false;

        DoubleValueSize that = (DoubleValueSize) otherDoubleValueSize;

        return value == that.value;
    }



    /**
     * Returns the double value itself
     *
     * @return the double value itself
     */
    public double getValue() {
        return value;
    }

    /**
     * Returns a string representation of the double value
     *
     * @return a string representation of the double value
     */
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Create a new DoubleValueSize with <code>value=0</code>
     *
     * @return a new DoubleValueSize with <code>value=0</code>
     * @see de.bugbusters.binpacking.model.Size#createZeroInstance()
     */
    public Size createZeroInstance() {
        return new DoubleValueSize(0.0);
    }

    /**
     * Adds another DoubleValueSize to this DoubleValueSize by adding the double values
     *
     * @param other the other DoubleValueSize
     * @return a new DoubleValueSize representing the sum
     * @see de.bugbusters.binpacking.model.Size#add(Size)
     */
    public Size add(Size other) {
        if (!(other instanceof DoubleValueSize)) {
            throw new IllegalArgumentException("#add() parameter is not an instance of " + DoubleValueSize.class.getName());
        }

        return new DoubleValueSize(value + ((DoubleValueSize) other).getValue());
    }

    /**
     * Subtracts another DoubleValueSize from this DoubleValueSize by subtracting the double values
     *
     * @param other the other DoubleValueSize
     * @return a new DoubleValueSize representing the difference
     * @see de.bugbusters.binpacking.model.Size#subtract(Size)
     */
    public Size subtract(Size other) {
        if (!(other instanceof DoubleValueSize)) {
            throw new IllegalArgumentException("#subtract() parameter is not an instance of " + DoubleValueSize.class.getName());
        }

        return new DoubleValueSize(value - ((DoubleValueSize) other).getValue());
    }

    /**
     * Returns other's double value divided by this DoubleValueSize's double value
     *
     * @param other the other DoubleValueSize
     * @return the percentage (0.0 to 1.0)
     * @see de.bugbusters.binpacking.model.Size#calculatePercentage(Size)
     */
    public double calculatePercentage(Size other) {
        if (!(other instanceof DoubleValueSize)) {
            throw new IllegalArgumentException("#calculatePercentage() parameter is not an instance of " + DoubleValueSize.class.getName());
        }

        return ((DoubleValueSize) other).getValue() /  value;
    }

}
