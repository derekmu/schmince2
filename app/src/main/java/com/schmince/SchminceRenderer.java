package com.schmince;

import android.content.Context;
import android.opengl.GLES20;
import com.schmince.game.ForSBlock;
import com.schmince.game.GameModelInterface;
import com.schmince.game.SchminceGame;
import com.schmince.game.model.SBlock;
import com.schmince.game.model.SBlockType;
import com.schmince.game.model.SObject;
import com.schmince.game.model.sprites.Sprite;
import dgui.BaseGUI;
import dgui.GUIRenderer;
import dopengl.DRenderer;
import dopengl.TriangleSetBatch;
import dopengl.shapes.GLTriangleUniform;
import thed.DColor;
import thed.DTimer;
import thed.GLColor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.List;

/**
 * Open GL renderer for the Schmince game.
 *
 * @author Derek Mulvihill - Jan 9, 2014
 */
public class SchminceRenderer extends DRenderer {
	private GUIRenderer guiRenderer = new GUIRenderer();

	private float cameraX = 50f;
	private float cameraY = 50f;
	private int playerX;
	private int playerY;

	private SchminceGame game;
	private GameModelInterface model;

	private List<Sprite> sprites = new ArrayList<>();

	private BlockDrawer blockDrawer = new BlockDrawer();
	private ObjectDrawer objectDrawer = new ObjectDrawer();
	private float[] vertices = new float[9];

	public SchminceRenderer(Context context, SchminceGame game, BaseGUI gui) {
		super(context);
		this.game = game;
		guiRenderer.setGUI(gui);
		guiRenderer.setGLib(glib);
		guiRenderer.setMatrix(matrix);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//enable face culling feature
        /*GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);*/

		//enable alpha blending
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		//enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL); //default is GL_LESS

		//Draw background (sets background color for future calls to GLES20.glClear)
		GLES20.glClearColor(0.05f, 0.05f, 0.05f, 1f);
		GLES20.glClearDepthf(1f);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		DTimer.get().update();

		// Redraw background color (the last call to GLES20.glClearColor sets the color that will be used (Eg. in onSurfaceCreated))
		// also clear the depth buffer
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		switch (game.getGameState()) {
			case HowToPlay:
			case InGame:
			case StartScreen:
			case GameCreate:
			case GameStarting:
			case GameOver:
				drawGame();
				break;
		}

		guiRenderer.draw(getScreenWidth(), getScreenHeight());
	}

	private void drawGame() {
		model = game.getModelInterface();

		updateCamera();
		matrix.updateVPMatrix(cameraX, cameraY);

		float[] bottomleft = matrix.orthoToWorld(0, 0);
		float[] topright = matrix.orthoToWorld(getScreenWidth(), getScreenHeight());
		int xs = (int) bottomleft[0];
		int ys = (int) bottomleft[1];
		int xe = (int) topright[0] + 1;
		int ye = (int) topright[1] + 1;
		model.forBlock(xs, ys, xe, ye, blockDrawer);
		blockDrawer.after();

		model.forBlock(xs, ys, xe, ye, objectDrawer);

		model.getSprites(sprites);
		for (Sprite sprite : sprites) {
			sprite.drawSprite(this);
		}
		for (int i = sprites.size() - 1; i >= 0; i--) {
			Sprite sprite = sprites.get(i);
			if (sprite.isExpired()) {
				sprites.remove(i);
			}
		}

		if (model.isLocating()) {
			drawRadar();
		}
	}

	private void drawRadar() {
		float px = cameraX;
		float py = cameraY;

		for (int i = 0; i < model.getPlayerCount(); i++) {
			if (i == model.getSelectedPlayerIndex()) {
				continue;
			}
			float dx = model.getPlayerX(i) - px;
			float dy = model.getPlayerY(i) - py;
			float h = (float) Math.sqrt(dx * dx + dy * dy);

			float outer = 5f;
			float xx = dx / h * outer;
			float yy = dy / h * outer;

			vertices[0] = px + xx;
			vertices[1] = py + yy;

			xx = dx / h * 2;
			yy = dy / h * 2;
			float xxx = dx / h * 0.25f;
			float yyy = dy / h * 0.25f;
			vertices[3] = px + xx - yyy;
			vertices[4] = py + yy + xxx;
			vertices[6] = px + xx + yyy;
			vertices[7] = py + yy - xxx;

			GLTriangleUniform tri = getGlib().getTriangleUniform();
			tri.setVertices(vertices);

			DColor color = C.PLAYER_COLORS[i];
			tri.draw(getVPMatrix(), color.Red, color.Green, color.Blue, 0.25f);
		}
	}

	private void updateCamera() {
		int selectedPlayer = model.getSelectedPlayerIndex();
		playerX = model.getPlayerX(selectedPlayer);
		playerY = model.getPlayerY(selectedPlayer);
		float dx = playerX - cameraX;
		float dy = playerY - cameraY;
		if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
			cameraX = playerX;
			cameraY = playerY;
		} else {
			cameraX += dx * DTimer.get().change() / 1000f;
			cameraY += dy * DTimer.get().change() / 1000f;
		}
	}

	@Override
	public float getMinZ() {
		return 8f;
	}

	@Override
	public float getMaxZ() {
		return 8f;
	}

	@Override
	public float getCameraX() {
		return cameraX;
	}

	@Override
	public float getCameraY() {
		return cameraY;
	}

	private class BlockDrawer implements ForSBlock {
		private final GLColor BLACK = new GLColor(0f, 0f, 0f, 1f);
		private final GLColor SHIP_FLOOR_LIGHT = new GLColor(0.4f, 0.4f, 0.4f, 1f);
		private final GLColor SHIP_FLOOR_DARK = new GLColor(0.35f, 0.35f, 0.35f, 1f);
		private final GLColor BASE_TILE_COLOR = new GLColor(C.BASE_TILE_COLOR, C.BASE_TILE_COLOR,
				C.BASE_TILE_COLOR, 1f);
		private final float ONE_SIXTH = 1f / 6f;
		private TriangleSetBatch triangleSetBatch = new TriangleSetBatch(SchminceRenderer.this);

		@Override
		public void forBlock(SBlock block) {
			//GLRectangle rect = glib.getRectangle();
			if (game.getGameState().useLOSDraw() && !model.isFlared()
					&& !model.los().hasLOS(playerX, playerY, block.X, block.Y)) {
				triangleSetBatch.batchRect(block.X - 0.5f, block.Y - 0.5f, 1f, 1f, BLACK);
				//rect.setBounds(block.X - 0.5f, block.Y - 0.5f, 1f, 1f);
				//rect.draw(getVPMatrix(), 0f, 0f, 0f, 1f);
			} else {
				if (block.BlockType == SBlockType.ShipFloor) {
					for (int i = 0; i < 6; i++) {
						triangleSetBatch.batchRect(block.X - 0.5f + i * ONE_SIXTH, block.Y - 0.5f,
								ONE_SIXTH, 1f, i % 2 == 1 ? SHIP_FLOOR_LIGHT : SHIP_FLOOR_DARK);
					}

					//rect.setBounds(block.X - 0.5f, block.Y - 0.5f, 1f, 1f);
					//rect.draw(getVPMatrix(), 0.5f, 0.5f, 0.5f, 1f);

				} else {
					triangleSetBatch.batchRect(block.X - 0.5f, block.Y - 0.5f, 1f, 1f,
							BASE_TILE_COLOR);
					//rect.setBounds(block.X - 0.5f, block.Y - 0.5f, 1f, 1f);
					//rect.draw(getVPMatrix(), C.BASE_TILE_COLOR, C.BASE_TILE_COLOR,C.BASE_TILE_COLOR, 1f);
				}
			}
		}

		public void after() {
			triangleSetBatch.finishTriangleBatch();
		}
	}

	private class ObjectDrawer implements ForSBlock {
		@Override
		public void forBlock(SBlock block) {
			SObject object = block.getObject();
			if (object != null) {
				if (game.getGameState().useLOSDraw() && !model.isFlared() && !model.los().hasLOS(playerX, playerY, block.X, block.Y)) {
				} else {
					object.draw(SchminceRenderer.this, block);
				}
			}
		}
	}
}