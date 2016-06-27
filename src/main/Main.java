package main;

import game.EndlessMode;
import game.menu.MainMenu;
import game.menu.StoryScene;
import gui.Gui;
import gui.graphics.GraphicView;
import storage.Storage;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Gui gui = null;
		gui = new Gui();
	}

	public static void setup(){
		loadHighscores();
		MainMenu menu = new MainMenu();
		Gui.setView(menu);
	}
	
	public static void loadHighscores(){
		StoryScene.setupScenes();
		Storage.load("./data/save.file");
	}
}
