package thed;

/**
 * Dumb interface to provide info when the program pauses.<br>
 * Intended to be used with DTimer - set the offset on the timer.
 *
 * @author Derek Mulvihill
 */
public interface PauseProvider {
	long getPauseOffset();
}
