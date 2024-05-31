package com.schmince.game.howtoplay;

/**
 * A message for how to play the game.
 *
 * @author Derek Mulvihill - Mar 15, 2014
 */
public class HTPMessage {
	private boolean isTitle;
	private String[] messages;

	public HTPMessage(boolean title, String... messages) {
		this.isTitle = title;
		this.messages = messages;
	}

	public boolean isTitle() {
		return isTitle;
	}

	public String[] getMessages() {
		return messages;
	}
}
