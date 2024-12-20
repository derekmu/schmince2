package com.schmince.game.model;

import android.opengl.Matrix;
import com.schmince.SchminceRenderer;

/**
 * @author Derek Mulvihill - Jan 18, 2014
 */
public class Item extends SObject {
	private ItemType itemType;

	public Item(ItemType itemType) {
		this.itemType = itemType;
	}

	@Override
	public void draw(SchminceRenderer renderer, SBlock block, boolean cantSee) {
		if (cantSee) {
			return;
		}
		float[] vpMatrix = renderer.getVPMatrix();
		Matrix.translateM(vpMatrix, 0, block.X, block.Y, 0);
		Matrix.scaleM(vpMatrix, 0, 0.5f, 0.5f, 1f);
		renderer.getGlib().getDrawer(itemType).draw(vpMatrix);
	}

	@Override
	public void interact(Survivor survivor) {
		ItemType tempType = survivor.getItem();
		survivor.setItem(itemType);
		if (tempType != null) {
			itemType = tempType;
		} else {
			getCurrentBlock().setObject(null);
		}
	}

	@Override
	public boolean isInteractable() {
		return true;
	}
}
