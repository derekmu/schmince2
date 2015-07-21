package thed;

/**
 * ThreadLocal timer for things that need to update in steps.
 *
 * @author Derek Mulvihill - Sep 2, 2013
 */
public class DTimer {
    /**
     * Nanos per millisecond.
     */
    public static final long NPM = 1000000;
    private static ThreadLocal<DTimer> timer = new ThreadLocal<DTimer>() {
        protected DTimer initialValue() {
            return new DTimer();
        }
    };
    private PauseProvider pauser;

    public static DTimer get() {
        return timer.get();
    }

    private long current;
    private long last;

    private DTimer() {
        update();
    }

    public void update() {
        if (pauser != null) {
            long offset = pauser.getPauseOffset();
            last = current;
            current = currentMilliValue() - offset;
        } else {
            last = current;
            current = currentMilliValue();
        }
    }

    public long millis() {
        return current;
    }

    public long change() {
        return current - last;
    }

    public void setPauseProvider(PauseProvider pauser) {
        this.pauser = pauser;
    }

    /**
     * Returns System.nanoTime() converted to milliseconds.
     */
    public static long currentMilliValue() {
        return System.nanoTime() / DTimer.NPM;
    }
}
