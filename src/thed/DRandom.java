package thed;

import java.util.Random;

/**
 * ThreadLocal for java.util.Random instances.
 *
 * @author Derek Mulvihill - Sep 2, 2013
 */
public class DRandom {
    private static ThreadLocal<Random> random = new ThreadLocal<Random>() {
        protected Random initialValue() {
            return new Random();
        }
    };

    public static Random get() {
        return random.get();
    }
}
