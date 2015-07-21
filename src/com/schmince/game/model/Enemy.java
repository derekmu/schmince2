package com.schmince.game.model;

import android.graphics.Point;
import android.opengl.Matrix;
import com.schmince.SchminceRenderer;
import com.schmince.game.GameModel;
import com.schmince.gldraw.GLEnemy;
import com.schmince.gldraw.GLIconType;
import thed.DRandom;
import thed.DTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class Enemy extends SObject {
    private static final int MOVE_MILLI = 2000;
    private final int drawSeed = DRandom.get().nextInt(10000);

    private long lastMoveMilli = DTimer.get().millis() + DRandom.get().nextInt(MOVE_MILLI);

    private volatile boolean dead = false;

    private List<Point> openPath = new ArrayList<Point>();

    public void update(GameModel gameModel) {
        if (this.dead || lastMoveMilli > DTimer.get().millis() - MOVE_MILLI) {
            return;
        }

        lastMoveMilli = DTimer.get().millis();

        if (chasePlayer(gameModel)) {
            return;
        }

        moveRandom(gameModel);
    }

    private boolean chasePlayer(GameModel gameModel) {
        Player chasePlayer = null;
        float mind = 100000000;

        for (int i = 0; i < gameModel.getPlayerCount(); i++) {
            Player player = gameModel.getPlayer(i);
            if (player.getHealth() > 0
                    && gameModel.los().hasLOS(getX(), getY(), player.getX(), player.getY())) {
                int dx = player.getX() - getX();
                int dy = player.getY() - getY();
                float d = (float) Math.sqrt(dx * dx + dy * dy);
                if (d < mind) {
                    mind = d;
                    chasePlayer = player;
                }
            }
        }

        if (chasePlayer != null) {
            if (Math.abs(chasePlayer.getX() - getX()) <= 1
                    && Math.abs(chasePlayer.getY() - getY()) <= 1) {
                gameModel.onAttackPlayer(this, chasePlayer);
                return true;
            } else {
                List<Point> path = gameModel.path().findPath(getX(), getY(), chasePlayer.getX(),
                        chasePlayer.getY());
                if (path.size() >= 2) {
                    Point next = path.get(1); //0 will be the current location
                    if (next != null) {
                        SBlock block = gameModel.getBlock(next.x, next.y);
                        if (block.getObject() == null) {
                            getCurrentBlock().setObject(null);
                            block.setObject(this);
                            gameModel.onEnemyMoved(this);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void moveRandom(GameModel gameModel) {
        openPath.clear();
        int dx = getX() - 1;
        int dy = getY() - 1;
        int cx = 1; //move right
        int cy = 0;
        for (int r = 0; r < 4; r++) {
            for (int i = 0; i < 2; i++) {
                if (gameModel.inBounds(dx, dy) && gameModel.getBlock(dx, dy).getObject() == null) {
                    openPath.add(new Point(dx, dy));
                }
                dx += cx;
                dy += cy;
            }
            if (cx == 1) {
                cy = 1; //move up
                cx = 0;
            } else if (cy == 1) {
                cx = -1; //move left
                cy = 0;
            } else if (cx == -1) {
                cy = -1; //move down
                cx = 0;
            }
        }
        if (openPath.size() > 0) {
            Point next = openPath.get(DRandom.get().nextInt(openPath.size()));
            SBlock block = gameModel.getBlock(next.x, next.y);
            getCurrentBlock().setObject(null);
            block.setObject(this);
            gameModel.onEnemyMoved(this);
            return;
        }
    }

    @Override
    public void draw(SchminceRenderer renderer, SBlock block) {
        float[] vpMatrix = renderer.getVPMatrix();
        Matrix.translateM(vpMatrix, 0, block.X, block.Y, 0);
        Matrix.scaleM(vpMatrix, 0, 0.5f, 0.5f, 1f);
        GLEnemy icon = (GLEnemy) renderer.getGlib().getDrawer(GLIconType.Enemy);
        icon.draw(vpMatrix, dead ? drawSeed : DTimer.get().millis() + drawSeed);
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public void interact(Player player) {
        //nothing for now?	
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
