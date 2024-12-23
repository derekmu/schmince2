package com.schmince.game;

import com.schmince.game.howtoplay.HowToPlayMessage;
import com.schmince.game.model.ItemType;
import com.schmince.game.model.sprites.Sprite;

import java.util.List;

/**
 * Collection of methods that are thread safe to call on the GameModel.
 *
 * @author Derek Mulvihill - Jan 17, 2014
 */
public interface GameModelInterface {
	void forBlock(int xs, int ys, int xe, int ye, ForSBlock forBlock);

	LOSFinder los();

	int getSurvivorCount();

	int getSelectedSurvivorIndex();

	int getSurvivorX(int i);

	int getSurvivorY(int i);

	boolean isLocating();

	ItemType getItem();

	boolean isSurivorAlert(int i);

	int getSurvivorHealth(int i);

	HowToPlayMessage getHowToPlayMessage();

	void getSprites(List<Sprite> sprites);

	void predrawObjects();

	boolean isVisible(int fromX, int fromY, int x, int y);
}
