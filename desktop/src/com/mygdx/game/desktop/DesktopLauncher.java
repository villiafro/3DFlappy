package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.FlappyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Space Infiltrators"; // or whatever you like
		config.width = 1024;  //experiment with
		config.height = 512;  //the window size
		config.x = 80;
		config.y = 80;
		//config.fullscreen = true;

		new LwjglApplication(new FlappyGame(), config);
	}
}
