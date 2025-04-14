package com.schmince.gui.startscreen;

import android.content.Intent;
import android.net.Uri;
import com.schmince.game.SchminceGame;
import dgui.button.Button;

/**
 * @author Derek Mulvihill - Apr 14, 2015
 */
public class PrivacyButton extends Button {
    private SchminceGame game;

    public PrivacyButton() {
        super("Privacy Policy");
    }

    @Override
    public void doAction() {
        String url = "https://reiterable.com/landing/privacy-schmince.html";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        this.game.context.startActivity(intent);
    }

    public void setGame(SchminceGame game) {
        this.game = game;
    }
}
