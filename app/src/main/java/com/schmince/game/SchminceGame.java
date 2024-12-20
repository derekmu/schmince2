package com.schmince.game;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import com.schmince.C;
import com.schmince.game.howtoplay.HowToPlayStage;
import com.schmince.game.model.Enemy;
import com.schmince.game.model.SBlockType;
import com.schmince.game.model.Survivor;
import dgame.BaseActivity;
import dgame.BaseGame;

import java.util.List;

/**
 * Schmince game controller.
 *
 * @author Derek Mulvihill - Jan 9, 2014
 */
public class SchminceGame extends BaseGame<UserEventType> {
	private final EventValues eventValues = new EventValues();
	private final BasicOnCompletionListener ONCOMPLETELISTENER = new BasicOnCompletionListener();
	private GameState gameState = GameState.StartScreen;
	private GameModel model = new GameModel(C.MAX_SURVIVOR_COUNT, 100, 10, 20);
	private HowToPlayStage[] howToPlayStages = HowToPlayStage.buildHowToPlayStages();
	private int howToPlayStageIndex = 0;
	private int gameStartSurvivorCount;
	private int gameStartMapSize;
	private int gameStartItems;
	private int gameStartEnemies;
	private int deadCount;
	private int survivorCount;
	private Context context;

	public SchminceGame(BaseActivity activity) {
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
				model = new GameModel(gameStartSurvivorCount, gameStartMapSize, gameStartEnemies, gameStartItems);
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
		int deadSurvivors = 0;
		int safeSurvivors = 0;

		for (int pi = 0; pi < model.getSurvivorCount(); pi++) {
			Survivor survivor = model.getSurvivor(pi);
			if (survivor.getHealth() <= 0) {
				deadSurvivors++;
			} else if (survivor.getCurrentBlock().BlockType == SBlockType.ShipFloor) {
				safeSurvivors++;
			}
			survivor.update(model);
		}

		if (deadSurvivors + safeSurvivors == model.getSurvivorCount()) {
			this.deadCount = deadSurvivors;
			this.survivorCount = safeSurvivors;
			if (gameState == GameState.InGame) {
				gameState = GameState.GameOver;
			}
		}

		List<Enemy> enemies = model.getEnemies();
		for (Enemy enemy : enemies) {
			enemy.update(model);
		}

		List<SoundEvent> soundEvents = model.getSoundEvents();
		for (SoundEvent event : soundEvents) {
			try (AssetFileDescriptor afd = context.getAssets().openFd(event.EventType.getFileName())) {
				MediaPlayer player = new MediaPlayer();
				player.setOnCompletionListener(ONCOMPLETELISTENER);
				player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				player.setVolume(event.LeftVolume, event.RightVolume);
				player.prepare();
				player.start();
			} catch (Exception ex) {
				Log.e("com.schmince", "Error playing sound", ex);
			}
		}
		soundEvents.clear();
	}

	@Override
	public void handleUserEvent(UserEventType type) {
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
				howToPlayStageIndex = 0;
				model = new GameModel(howToPlayStages[howToPlayStageIndex], 0);
				break;
			}
			case CancelHowToPlay:
			case CancelGameCreate:
			case EndGameOver: {
				gameState = GameState.StartScreen;
				break;
			}
			case NextHowToPlay: {
				if (!model.nextHowToPlayMessage()) {
					if (howToPlayStageIndex + 1 >= howToPlayStages.length) {
						gameState = GameState.StartScreen;
					} else {
						howToPlayStageIndex++;
						model = new GameModel(howToPlayStages[howToPlayStageIndex], 0);
					}
				}
				break;
			}
			case SelectSurvivor: {
				model.setSelectedSurvivorIndex(eventValues.survivorIndex);
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
			case CreateGame: {
				gameStartSurvivorCount = eventValues.survivorCount;
				gameStartMapSize = eventValues.mapSize;
				gameStartItems = eventValues.itemPer;
				gameStartEnemies = eventValues.enemyPer;
				gameState = GameState.GameStarting;
				break;
			}
		}
	}

	public synchronized void onUseItem() {
		newEvent(UserEventType.UseItem);
	}

	public synchronized void onSelectSurvivor(int survivorindex) {
		eventValues.survivorIndex = survivorindex;
		handleUserEvent(UserEventType.SelectSurvivor);
	}

	public synchronized void onNewGame() {
		newEvent(UserEventType.NewGame);
	}

	public synchronized void onHowToPlay() {
		newEvent(UserEventType.HowToPlay);
	}

	public synchronized void onNextHowToPlay() {
		newEvent(UserEventType.NextHowToPlay);
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

	public synchronized void onCreateGame(int survivorCount, int mapSize, int itemPer, int enemyPer) {
		if (newEvent(UserEventType.CreateGame)) {
			eventValues.survivorCount = survivorCount;
			eventValues.mapSize = mapSize;
			eventValues.itemPer = itemPer;
			eventValues.enemyPer = enemyPer;
		}
	}

	public GameModelInterface getModelInterface() {
		return model;
	}

	private static class BasicOnCompletionListener implements OnCompletionListener {
		@Override
		public void onCompletion(MediaPlayer mp) {
			mp.stop();
			mp.reset();
			mp.release();
		}
	}

	/**
	 * Storage of values for user events.
	 * All members should be volatile.
	 */
	private static class EventValues {
		volatile int mapSize;
		volatile int survivorCount;
		volatile int itemPer;
		volatile int enemyPer;

		volatile int survivorIndex;
		volatile float worldX;
		volatile float worldY;
	}
}
