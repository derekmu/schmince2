package com.schmince.game;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import com.schmince.C;
import com.schmince.game.howtoplay.HTPStage;
import com.schmince.game.model.Enemy;
import com.schmince.game.model.Player;
import com.schmince.game.model.SBlockType;
import dgame.BaseActivity;
import dgame.BaseGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Schmince game controller.
 *
 * @author Derek Mulvihill - Jan 9, 2014
 */
public class SchminceGame extends BaseGame<UserEventType> {
    private volatile GameState gameState = GameState.StartScreen;
    private final EventValues eventValues = new EventValues();

    private volatile GameModel model = new GameModel(C.MAX_PLAYER_COUNT, C.MAX_MAP_SIZE, 10, 20);
    private HTPStage[] htpStages = HTPStage.buildHTPStages();
    private int htpStageIndex = 0;

    private int gameStartPlayerCount;
    private int gameStartMapSize;
    private int gameStartItemPer;
    private int gameStartEnemyPer;

    private volatile int deadCount;
    private volatile int survivorCount;

    private Context context;

    public SchminceGame(BaseActivity activity) {
        super(activity);
        this.context = activity;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getDeadCount() {
        return deadCount;
    }

    public int getSurvivorCount() {
        return survivorCount;
    }

    @Override
    public void gameStep() {
        switch (getGameState()) {
            case HowToPlay:
            case InGame: {
                updateModel();
                break;
            }
            case GameStarting: {
                model = new GameModel(gameStartPlayerCount, gameStartMapSize, gameStartEnemyPer,
                        gameStartItemPer);
                gameState = GameState.InGame;
                break;
            }
            case GameCreate:
            case StartScreen:
            case GameOver:
            default:
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex); //does this really ever happen?
                }
                break;
        }
    }

    private void updateModel() {
        int deadPlayers = 0;
        int safePlayers = 0;

        for (int pi = 0; pi < model.getPlayerCount(); pi++) {
            Player player = model.getPlayer(pi);
            if (player.getHealth() <= 0) {
                deadPlayers++;
            } else if (player.getCurrentBlock().BlockType == SBlockType.ShipFloor) {
                safePlayers++;
            }
            player.update(model);
        }

        if (deadPlayers + safePlayers == model.getPlayerCount()) {
            this.deadCount = deadPlayers;
            this.survivorCount = safePlayers;
            if (gameState == GameState.InGame) {
                gameState = GameState.GameOver;
            }
        }

        List<Enemy> enemies = model.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update(model);
        }

        List<SoundEvent> soundEvents = model.getSoundEvents();
        for (int i = 0; i < soundEvents.size(); i++) {
            SoundEvent event = soundEvents.get(i);
            try {
                AssetFileDescriptor afd = context.getAssets().openFd(event.EventType.getFileName());
                MediaPlayer player = new MediaPlayer();
                player.setOnCompletionListener(ONCOMPLETELISTENER);
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                player.setVolume(event.LeftVolume, event.RightVolume);
                player.prepare();
                player.start();
                sounds.add(player);
            } catch (Exception ex) {
                Log.e("com.schmince", "Error playing sound", ex);
            }
        }
        soundEvents.clear();
    }

    private List<MediaPlayer> sounds = new ArrayList<MediaPlayer>();

    private final BasicOnCompletionListener ONCOMPLETELISTENER = new BasicOnCompletionListener();

    private class BasicOnCompletionListener implements OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.stop();
            mp.reset();
            mp.release();
            sounds.remove(mp);
        }
    }

    @Override
    public synchronized void handleUserEvent(UserEventType type) {
        if (!type.isValidState(gameState)) {
            return;
        }
        switch (type) {
            case NewGame: {
                gameState = GameState.GameCreate;
                break;
            }
            case HowToPlay: {
                gameState = GameState.HowToPlay;
                htpStageIndex = 0;
                model = new GameModel(htpStages[htpStageIndex], 0);
                break;
            }
            case CancelHowToPlay: {
                gameState = GameState.StartScreen;
                break;
            }
            case NextHTP: {
                if (!model.nextHTPMessage()) {
                    if (htpStageIndex + 1 >= htpStages.length) {
                        gameState = GameState.StartScreen;
                    } else {
                        htpStageIndex++;
                        model = new GameModel(htpStages[htpStageIndex], 0);
                    }
                }
                break;
            }
            case SelectPlayer: {
                model.setSelectedPlayer(eventValues.playerIndex);
                break;
            }
            case UseItem: {
                model.useItem();
                break;
            }
            case Tap: {
                model.worldSelected(eventValues.worldX, eventValues.worldY);
                break;
            }
            case CancelGameCreate: {
                gameState = GameState.StartScreen;
                break;
            }
            case CreateGame: {
                gameStartPlayerCount = eventValues.playerCount;
                gameStartMapSize = eventValues.mapSize;
                gameStartItemPer = eventValues.itemPer;
                gameStartEnemyPer = eventValues.enemyPer;
                gameState = GameState.GameStarting;
                break;
            }
            case EndGameOver: {
                gameState = GameState.StartScreen;
                break;
            }
        }
    }

    public synchronized void onUseItem() {
        newEvent(UserEventType.UseItem);
    }

    public synchronized void onSelectPlayer(int playerIndex) {
        if (newEvent(UserEventType.SelectPlayer)) {
            eventValues.playerIndex = playerIndex;
        }
    }

    public synchronized void onNewGame() {
        newEvent(UserEventType.NewGame);
    }

    public synchronized void onHowToPlay() {
        newEvent(UserEventType.HowToPlay);
    }

    public synchronized void onNextHTP() {
        newEvent(UserEventType.NextHTP);
    }

    public synchronized void onCancelHowToPlay() {
        newEvent(UserEventType.CancelHowToPlay);
    }

    public synchronized void onTap(float worldX, float worldY) {
        if (newEvent(UserEventType.Tap)) {
            eventValues.worldX = worldX;
            eventValues.worldY = worldY;
        }
    }

    public synchronized void onCancelGameCreate() {
        newEvent(UserEventType.CancelGameCreate);
    }

    public synchronized void onEndGameOver() {
        newEvent(UserEventType.EndGameOver);
    }

    public synchronized void onCreateGame(int playerCount, int mapSize, int itemPer, int enemyPer) {
        if (newEvent(UserEventType.CreateGame)) {
            eventValues.playerCount = playerCount;
            eventValues.mapSize = mapSize;
            eventValues.itemPer = itemPer;
            eventValues.enemyPer = enemyPer;
        }
    }

    /**
     * Storage of values for user events.
     * All members should be volatile.
     */
    private class EventValues {
        volatile int mapSize;
        volatile int playerCount;
        volatile int itemPer;
        volatile int enemyPer;

        volatile int playerIndex;
        volatile float worldX;
        volatile float worldY;
    }

    public GameModelInterface getModelInterface() {
        return model;
    }
}
