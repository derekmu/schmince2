package dgame;

import thed.DTimer;
import thed.PauseProvider;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract super class for the game controller.
 *
 * @author Derek Mulvihill - Jan 11, 2014
 */
public abstract class BaseGame<T> implements PauseProvider {
    private UserEventStore<T> userEvent = new UserEventStore<T>();
    private volatile Thread gameThread = null;

    private BaseActivity activity;
    private volatile long pauseOffset = 0;
    private volatile long pauseStart = 0;
    private volatile int pauseCount = 0;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public BaseGame(BaseActivity activity) {
        this.activity = activity;
    }

    public final void start() {
        if (gameThread == null) {
            gameThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    DTimer.get().setPauseProvider(BaseGame.this);
                    BaseGame.this.gameLoop();
                }
            });
            gameThread.start();
        } else {
            throw new RuntimeException("Game is already started");
        }
    }

    private void gameLoop() {
        while (true) {
            if (!(running.get() && !activity.isDestroyed())) {
                break;
            }
            DTimer.get().update();
            updateUserEventStore();
            if ((pauseStart != 0)) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex); //does this really ever happen?
                }
                continue;
            }
            if (DTimer.get().change() == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex); //does this really ever happen?
                }
                continue;
            }
            gameStep();
        }
    }

    public abstract void gameStep();

    public final boolean newEvent(T type) {
        return userEvent.checkStale(type);
    }

    /**
     * Since the userEvent is updated on a separate thread, this has to be called to take action on to the user events.
     */
    private synchronized void updateUserEventStore() {
        if (userEvent.isStale()) {
            return;
        }
        handleUserEvent(userEvent.getType());
        userEvent.markStale();
    }

    public abstract void handleUserEvent(T type);

    /**
     * Callback to notify when the game is paused.
     */
    public final synchronized void onPause() {
        if (pauseStart == 0) {
            pauseStart = DTimer.currentMilliValue();
        }
        pauseCount++;
    }

    /**
     * Callback to notify when the game resumes from a pause.
     */
    public final synchronized void onResume() {
        if (pauseStart != 0) {
            if (pauseCount > 0) {
                pauseCount--;
            }
            if (pauseCount == 0) {
                pauseOffset += DTimer.currentMilliValue() - pauseStart;
                pauseStart = 0;
            }
        }
    }

    /**
     * Callback for the android activity to notify when the application is destroyed
     */
    public final void onDestroy() {
        running.set(false);
    }

    @Override
    public final long getPauseOffset() {
        if (pauseStart != 0) {
            return pauseOffset + DTimer.currentMilliValue() - pauseStart;
        } else {
            return pauseOffset;
        }
    }
}
