package lsi.multinet.binpacking;

import java.util.Set;

import lsi.multinet.binpacking.exceptions.BinFullException;
import lsi.multinet.binpacking.exceptions.DuplicateElementException;

public interface Bin  {

    /**
     * Returns the bin's identifier
     *
     * @return the bin's identifier
     */
    String getId();

    /**
     * Checks if an element is already in this bin
     *
     * @param element the element to look for
     * @return true if <code>element</code> is already in this bin
     */
    boolean contains(Element element);

    /**
     * Returns the set of {@link Element}s that are currently in this bin
     *
     * @return the set of {@link Element}s that are currently in this bin
     */
    Set getElements();

    /**
     * Adds an {@link Element} to this bin. Implementations of this method should check for element duplicates and
     * capacity exceedance
     *
     * @param element the {@link Element} to add
     * @throws BinFullException          if the bin's free space is to small to add the element
     * @throws DuplicateElementException this bin already contains <code>element</code>
     */
    void addElement(Element element) throws BinFullException, DuplicateElementException;

    /**
     * Returns the number of elements contained in this bin
     *
     * @return the number of elements contained in this bin
     */
    int getElementCount();

    /**
     * Returns the percentage of space that is already in use
     *
     * @return the percentage used of space
     * @see de.bugbusters.binpacking.model.Size#calculatePercentage(Size)
     */
    double getFillLevel();

    /**
     * Returns the free space
     *
     * @return the free space
     */
    Size getFreeSpace();

    /**
     * Returns the capacity
     *
     * @return the capacity
     */
    Size getCapacity();
}