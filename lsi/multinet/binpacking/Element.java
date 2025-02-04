package lsi.multinet.binpacking;

public interface Element  {

    /**
     * Determines the identifier of this element
     *
     * @return the identifier of this element
     */
    String getId();

    /**
     * Determines the size of this element
     *
     * @return the size of this element
     */
    Size getSize();

    /**
     * Checks if this element fits into a given amount of free space. Implementations of this method may do special
     * checks not only <code>this.getSize().compareTo(otherSize) >= 0</code> as some sorts of sizes may need those checks
     *
     * @param freeSpace the free space to check against
     * @return true, if this element fits into <code>freeSpace</code>
     */
    boolean fitsInto(Size freeSpace);

}