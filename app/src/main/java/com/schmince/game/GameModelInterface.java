package com.schmince.game;

import com.schmince.game.howtoplay.HTPMessage;
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

	int getPlayerCount();

	int getSelectedPlayerIndex();

	int getPlayerX(int i);

	int getPlayerY(int i);

	boolean isLocating();

	boolean isFlared();

	ItemType getItem();

	boolean isPlayerAlert(int i);

	int getPlayerHealth(int i);

	HTPMessage getHTPMessage();

	void getSprites(List<Sprite> sprites);
}
