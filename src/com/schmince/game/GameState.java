package com.schmince.game;

/**
 * @author Derek Mulvihill - Jan 15, 2014
 */
public enum GameState {
    /**
     * Title screen for the user to select to start a new game, exit, etc
     */
    StartScreen(false),
    /**
     * User is selecting options for a new game
     */
    GameCreate(false),
    /**
     * Loading screen between GameCreate and InGame
     */
    GameStarting(false),
    /**
     * User is playing the game
     */
    InGame(true),
    /**
     * User finished playing game, and win or lose
     */
    GameOver(false),
    /**
     * Use is doing the tutorial on how to play the game
     */
    HowToPlay(true),
    //
    ;

    private boolean losDraw;

    private GameState(boolean losDraw) {
        this.losDraw = losDraw;
    }

    public boolean useLOSDraw() {
        return losDraw;
    }
}
