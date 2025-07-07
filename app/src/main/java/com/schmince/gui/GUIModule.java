package com.schmince.gui;

import androidx.core.graphics.Insets;
import dgui.GUIItem;
import texample.GLTextCache;

import java.util.List;

/**
 * @author Derek Mulvihill - Oct 15, 2013
 */
public interface GUIModule {
    void update(int screenWidth, int screenHeight, Insets insets, GLTextCache textCache);

    List<GUIItem> getGUI();
}
