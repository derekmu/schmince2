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
    public void forBlock(int xs, int ys, int xe, int ye, ForSBlock forBlock);

    public LOSFinder los();

    public int getPlayerCount();

    public int getSelectedPlayerIndex();

    public int getPlayerX(int i);

    public int getPlayerY(int i);

    public boolean isLocating();

    public boolean isFlared();

    public ItemType getItem();

    public boolean isPlayerAlert(int i);

    public int getPlayerHealth(int i);

    public HTPMessage getHTPMessage();

    public void getSprites(List<Sprite> sprites);
}
