package com.schmince.game.model;

import com.schmince.SchminceRenderer;
import dopengl.shapes.GLRectangle;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class MudWall extends SObject {
    private volatile int health;

    public MudWall(float sample) {
        this.health = Math.max(1, Math.round(sample * 10f));
    }

    @Override
    public void draw(SchminceRenderer renderer, SBlock block) {
        GLRectangle rect = renderer.getGlib().getRectangle();
        rect.setBounds(block.X - 0.5f, block.Y - 0.5f, 1f, 1f);
        rect.draw(renderer.getVPMatrix(), 0.5f, 0.25f, 0f, 0.5f + (health / 10f) * 0.5f);
    }

    @Override
    public void interact(Player player) {
        if (player.getItem() == ItemType.Pick) {
            health -= 2;
        } else {
            health -= 1;
        }
        if (health < 1) {
            getCurrentBlock().setObject(null);
        }
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public float getPathWeight() {
        return 1 + health;
    }

    @Override
    public boolean blocksLOS() {
        return true;
    }
}
