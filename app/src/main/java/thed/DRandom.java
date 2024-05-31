package thed;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ThreadLocal for java.util.Random instances.
 *
 * @author Derek Mulvihill - Sep 2, 2013
 */
public class DRandom {
	public static Random get() {
		return ThreadLocalRandom.current();
	}
}
