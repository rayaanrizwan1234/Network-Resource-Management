package lsi.multinet.binpacking;


/* 
 * 
 * Interface to be implemented by classes representing bin and element sizes, 
 * based on code by Sven Kiesewetter.
 * 
 */

public interface Size extends Comparable {

    /**
     * Creates a new instance of Size that is equivalent to zero
     *
     * @return a new instance that is equivalent to zero
     */
    Size createZeroInstance();

    /**
     * Size addition
     *
     * @param other the other {@link Size}
     * @return a new {@link Size} representing the sum of this {@link Size} plus the other {@link Size}
     */
    Size add(Size other);

    /**
     * Size subtraction
     *
     * @param other the other {@link Size}
     * @return a new {@link Size} representing the difference between this {@link Size} minus the other {@link Size}
     */
    Size subtract(Size other);

    /**
     * Calculates the following percentage: how much percent is this {@link Size} in relation to the other {@link Size}.
     * For example: this {@link Size} = 10, other {@link Size} = 4.556, result = 0.4556
     *
     * @param other the other {@link Size}
     * @return the percentage (0.0 to 1.0)
     */
    double calculatePercentage(Size other);

    /**
     * The implementation of this strongly affects the efficiency of the bin packing algorithms.
     *
     * @param otherSize another size
     * @return -1 if this size is smaller than <code>otherSize</code><br>
     *         0 if this size is equal to <code>otherSize</code><br>
     *         1 if this size is larger than <code>otherSize</code>
     * @see Comparable#compareTo(Object)
     */
    int compareTo(Object otherSize);
}