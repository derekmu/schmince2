package com.schmince.game;

/**
 * Event types from user interactions.
 *
 * @author Derek Mulvihill - Jan 11, 2014
 */
public enum UserEventType {
	/**
	 * user selected to start a new game from the start screen
	 */
	NewGame(GameState.StartScreen),
	/**
	 * user selected to do a tutorial on how to play from the start screen
	 */
	HowToPlay(GameState.StartScreen),
	/**
	 * user wants to quit the tutorial and go back to the start screen
	 */
	CancelHowToPlay(GameState.HowToPlay),
	/**
	 * user wants to go to the next message or stage on the tutorial
	 */
	NextHTP(GameState.HowToPlay),
	/**
	 * user selected to control a different character in game
	 */
	SelectPlayer(GameState.InGame, GameState.HowToPlay),
	/**
	 * user selected to use the item of the currently controlled character in game
	 */
	UseItem(GameState.InGame, GameState.HowToPlay),
	/**
	 * user tapped in the world somewhere in game or in the tutorial
	 */
	Tap(GameState.InGame, GameState.HowToPlay),
	/**
	 * user selected cancel on the game create screen
	 */
	CancelGameCreate(GameState.GameCreate),
	/**
	 * user selected to start the game on the game create screen
	 */
	CreateGame(GameState.GameCreate),
	/**
	 * user selected to close the game over screen and go back to the start screen
	 */
	EndGameOver(GameState.GameOver),
	//
	;

	private final GameState[] gameState;

	UserEventType(GameState... gameState) {
		this.gameState = gameState;
	}

	public boolean isValidState(GameState state) {
		for (GameState value : gameState) {
			if (value == state) {
				return true;
			}
		}
		return false;
	}
}
