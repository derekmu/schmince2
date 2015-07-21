package thed;

/**
 * Just a box around a primitive long so you can avoid autoboxing (Eg. in collections).
 *
 * @author Derek Mulvihill - Oct 4, 2013
 */
public class LongContainer {
    public long value;

    public LongContainer(long value) {
        this.value = value;
    }
}
