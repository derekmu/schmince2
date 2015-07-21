package com.schmince.gui;

import dgui.GUIItem;
import texample.GLTextCache;

import java.util.List;

/**
 * @author Derek Mulvihill - Oct 15, 2013
 */
public interface GUIModule {
    void update(int screenWidth, int screenHeight, GLTextCache textCache);

    List<GUIItem> getGUI();
}
