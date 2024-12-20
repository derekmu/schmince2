package dgame;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An object to assist in handling user events from different threads than the game update thread.
 *
 * @author Derek Mulvihill - Jan 11, 2014
 */
class UserEventStore<T> {
	private final AtomicBoolean stale = new AtomicBoolean(true);
	private T type;

	public boolean checkStale(T type) {
		if (stale.compareAndSet(true, false)) {
			this.type = type;
			return true;
		}
		return false;
	}

	public boolean isStale() {
		return stale.get();
	}

	public void markStale() {
		stale.set(true);
	}

	public T getType() {
		return type;
	}
}
