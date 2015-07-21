package thed;

import java.util.ArrayList;
import java.util.List;

/**
 * Performance monitor, geared for frame rates.
 *
 * @author Derek Mulvihill - Feb 1, 2014
 */
public class DFrameMonitor {
    private List<Long> frameTimes = new ArrayList<Long>();
    private long duration;

    /**
     * Create a DFPSMonitor that keeps track of time within the given duration.
     */
    public DFrameMonitor(long duration) {
        this.duration = duration;
    }

    /**
     * Add a new time and clean up any times before the duration before the new time.
     */
    public void addFrame(long time) {
        frameTimes.add(time);
        for (int i = 0; i < frameTimes.size(); i++) {
            if (frameTimes.get(i) < time - duration) {
                frameTimes.remove(i);
                i--;
            }
        }
    }

    /**
     * Number of times that were within the duration.
     */
    public long getFrames() {
        return frameTimes.size();
    }
}
