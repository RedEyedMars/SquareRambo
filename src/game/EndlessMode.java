package game;

import java.util.ArrayList;
import java.util.List;

import game.menu.GraphicNumber;
import game.menu.GraphicWord;
import game.menu.MainMenu;
import gui.Gui;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.inputs.KeyBoardListener;
import gui.inputs.MotionEvent;
import main.Hub;

public class EndlessMode extends Game{

	protected float enemySpeed = -0.005f;
	public EndlessMode(){
		super();
		this.bulletSpeed = 0.015f;
		addBorder(1f,0f);
		addBorder(-1f,0f);
		addBorder(0f,1f);
	}
	
	public void update(){
		
		generateNewSquare();		
		super.update();
	}
	protected void generateNewSquare() {
		if(reds.size()-3<maxReds()){
			Square square = new Square("red",0.075f);
			double angle = (Math.random()+hero.getX())*Math.PI*2/4f;
			square.setX((float) (Math.cos(angle)*1.5f)+hero.getX());
			square.setY((float) (Math.sin(angle)*1.5f));
			square.setVelocity(enemySpeed,hero);
			addSquare(square);

		}
	}
	
	protected int maxReds(){
		return score.getValue()/50+10;
	}


}
