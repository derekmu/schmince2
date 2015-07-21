package com.schmince.game.howtoplay;

import android.graphics.Point;
import com.schmince.game.model.ItemType;

/**
 * A How To Play stage used to create a GameModel for for use in the HowToPlay GameState.<br>
 *
 * @author Derek Mulvihill - Mar 15, 2014
 */
public class HTPStage {
    int mapSize;
    Point[] playerLocations;
    Point[] mudLocations;
    Point[] enemyLocations;
    ItemType[] itemTypes;
    Point[] itemLocations;
    Point shipLocation;
    HTPMessage[] messages;

    private HTPStage() {
    }

    public static HTPStage[] buildHTPStages() {
        HTPStage stage1 = new HTPStage();
        stage1.mapSize = 8;
        stage1.shipLocation = null;
        stage1.playerLocations = new Point[]{new Point(3, 3)};
        stage1.mudLocations = new Point[]{new Point(0, 0), new Point(1, 0), new Point(0, 1),
                new Point(7, 4), new Point(6, 4), new Point(5, 4), new Point(5, 3),
                new Point(6, 3), new Point(0, 5), new Point(1, 5), new Point(2, 6), new Point(2, 7)};
        stage1.enemyLocations = new Point[]{};
        stage1.itemTypes = new ItemType[]{};
        stage1.itemLocations = new Point[]{};
        stage1.messages = new HTPMessage[]{ //
                new HTPMessage(true, "Welcome to Schmince!"), //
                new HTPMessage(false, // 
                        "You control a group of ", // 
                        "space tourists who crashed", //
                        "on an unknown planet"), //
                new HTPMessage(false, //
                        "Your goal is to guide the", //
                        "survivors to find the spaceship"), //
                new HTPMessage(false, //
                        "When all of the survivors are", // 
                        "dead or in the space ship", //
                        "you win (or lose)!")};

        HTPStage stage2 = new HTPStage();
        stage2.mapSize = 8;
        stage2.shipLocation = null;
        stage2.playerLocations = new Point[]{new Point(7, 0), new Point(0, 7)};
        stage2.mudLocations = new Point[]{new Point(0, 0), new Point(1, 1), new Point(2, 2),
                new Point(3, 3), new Point(4, 4), new Point(5, 5),
                new Point(6, 6),
                new Point(7, 7),
                //
                new Point(1, 0), new Point(2, 1), new Point(3, 2), new Point(4, 3),
                new Point(5, 4), new Point(6, 5), new Point(7, 6),
                //
                new Point(1, 0), new Point(2, 1), new Point(3, 2), new Point(4, 3),
                new Point(5, 4), new Point(6, 5), new Point(7, 6),};
        stage2.enemyLocations = new Point[]{};
        stage2.itemTypes = new ItemType[]{};
        stage2.itemLocations = new Point[]{};
        stage2.messages = new HTPMessage[]{ //
                new HTPMessage(false, "To guide the survivors", //
                        "tap anywhere on the ground", //
                        "and they will move"), //
                new HTPMessage(false, // 
                        "You can change survivors", //
                        "by tapping on the icons", //
                        "at the bottom of the screen"), //
                new HTPMessage(false, //
                        "Each survivor can keep moving", //
                        "while you control others"), //
                new HTPMessage(false, //
                        "The survivors can dig through", //
                        "mud walls to get to their target")};

        HTPStage stage3 = new HTPStage();
        stage3.mapSize = 8;
        stage3.playerLocations = new Point[]{new Point(0, 4)};
        stage3.mudLocations = new Point[]{new Point(0, 0), new Point(1, 0), new Point(0, 1),
                new Point(7, 4), new Point(6, 4), new Point(5, 4), new Point(5, 3),
                new Point(6, 3), new Point(0, 5), new Point(1, 5), new Point(2, 6), new Point(2, 7)};
        stage3.enemyLocations = new Point[]{new Point(7, 0)};
        stage3.itemTypes = new ItemType[]{};
        stage3.itemLocations = new Point[]{};
        stage3.shipLocation = null;
        stage3.messages = new HTPMessage[]{ //
                new HTPMessage(false, "The world has purple slug", // 
                        "monsters that are hostile"), //
                new HTPMessage(false, // 
                        "If a slug is close to a survivor", //
                        "it will do attack periodically"), //
                new HTPMessage(false, //
                        "The health of a survivor is", //
                        "indicated by how dark", //
                        "their icon is"), //
                new HTPMessage(false, //
                        "If a slug is in vision of a", // 
                        "survivor, the icon will blink")};

        HTPStage stage4 = new HTPStage();
        stage4.mapSize = 8;
        stage4.shipLocation = null;
        stage4.playerLocations = new Point[]{new Point(3, 3)};
        stage4.mudLocations = new Point[]{new Point(0, 0), new Point(1, 0), new Point(0, 1),
                new Point(7, 4), new Point(6, 4), new Point(5, 4), new Point(5, 3),
                new Point(6, 3), new Point(0, 5), new Point(1, 5), new Point(2, 6),
                new Point(2, 7), new Point(2, 5)};
        stage4.enemyLocations = new Point[]{new Point(0, 7)};
        stage4.itemTypes = new ItemType[]{ItemType.Gun, ItemType.Armor, ItemType.Boots,
                ItemType.Flare};
        stage4.itemLocations = new Point[]{new Point(0, 3), new Point(4, 0), new Point(4, 7),
                new Point(4, 3)};
        stage4.messages = new HTPMessage[]{ //
                new HTPMessage(false, "When the ship crashed", // 
                        "it ejected items that", //
                        "can help you"), //
                new HTPMessage(false, // 
                        "Tap on an item and", //
                        "the survivor will", // 
                        "move to pick it up"), //
                new HTPMessage(false, // 
                        "Each survivor can hold", //
                        "only one item at a time"), //
                new HTPMessage(false, //
                        "Some items can be used (gun)", //
                        "Some are passive (armor)"), //
                new HTPMessage(false, //
                        "When an item is picked up", //
                        "it shows at the top right", //
                        "of the screen"), //
                new HTPMessage(false, //
                        "To use an item", //
                        "tap on it at the top right") //
        };

        HTPStage stage5 = new HTPStage();
        stage5.mapSize = 15;
        stage5.shipLocation = new Point(7, 7);
        stage5.playerLocations = new Point[]{new Point(0, 14), new Point(7, 14),
                new Point(14, 14)};
        stage5.mudLocations = new Point[]{new Point(0, 1), new Point(0, 3), new Point(0, 5),
                new Point(0, 13), new Point(1, 1), new Point(1, 3), new Point(1, 6),
                new Point(1, 11), new Point(2, 2), new Point(2, 3), new Point(2, 4),
                new Point(2, 7), new Point(2, 10), new Point(2, 11), new Point(2, 12),
                new Point(3, 0), new Point(3, 3), new Point(3, 5), new Point(3, 9),
                new Point(3, 10), new Point(3, 11), new Point(3, 12), new Point(3, 13),
                new Point(4, 2), new Point(4, 12), new Point(5, 0), new Point(5, 3),
                new Point(5, 11), new Point(5, 14), new Point(6, 2), new Point(6, 11),
                new Point(7, 2), new Point(7, 12), new Point(8, 0), new Point(8, 3),
                new Point(8, 12), new Point(9, 2), new Point(9, 11), new Point(9, 13),
                new Point(9, 14), new Point(10, 1), new Point(11, 0), new Point(11, 2),
                new Point(11, 8), new Point(11, 9), new Point(11, 10), new Point(11, 11),
                new Point(11, 12), new Point(12, 0), new Point(12, 2), new Point(12, 3),
                new Point(12, 8), new Point(12, 10), new Point(12, 11), new Point(13, 1),
                new Point(13, 9), new Point(14, 4), new Point(14, 5), new Point(14, 9),
                new Point(14, 11)};
        stage5.enemyLocations = new Point[]{new Point(0, 0), new Point(7, 0), new Point(14, 0)};
        stage5.itemTypes = new ItemType[]{ItemType.Gun, ItemType.Armor, ItemType.Boots,
                ItemType.Flare, ItemType.MedicKit};
        stage5.itemLocations = new Point[]{new Point(1, 14), new Point(8, 14), new Point(13, 14),
                new Point(1, 7), new Point(13, 7)};
        stage5.messages = new HTPMessage[]{ //
                new HTPMessage(false, "The space ship has a dark", // 
                        "exterior and a door to ", //
                        "get on board"), //
                new HTPMessage(false, // 
                        "Get everyone inside to win"), //
                new HTPMessage(true, // 
                        "This is the end", //
                        "of the tutorial", //
                        "Thanks for playing!"), //
        };

        return new HTPStage[]{stage1, stage2, stage3, stage4, stage5};
    }

    public int getMapSize() {
        return mapSize;
    }

    public Point[] getPlayerLocations() {
        return playerLocations;
    }

    public Point[] getMudLocations() {
        return mudLocations;
    }

    public Point[] getEnemyLocations() {
        return enemyLocations;
    }

    public ItemType[] getItemTypes() {
        return itemTypes;
    }

    public Point[] getItemLocations() {
        return itemLocations;
    }

    public Point getShipLocation() {
        return shipLocation;
    }

    public HTPMessage[] getMessages() {
        return messages;
    }
}
