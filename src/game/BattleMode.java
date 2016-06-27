package game;

import game.menu.HighscoreMenu;
import game.menu.MainMenu;
import gui.Gui;
import main.Hub;

public class BattleMode extends Game{
	
	protected Square enemy;
	protected float enemyPointerX=0.5f-0.0375f;
	protected float enemyPointerY=0.9f;
	
	protected int enemyBulletFrequency = 10;
	protected float enemyBulletSpeed = 0.015f;
	
	public BattleMode(){
		super();
		this.bulletSpeed = 0.015f;
		addBorder(1f,0f);
		addBorder(-1f,0f);
		
		enemy = new Square("red",0.05f){
			@Override
			public void move(float x, float y){
				super.move(x, 1-enemy.getHeight());
			}
			@Override
			public Action<Square> getOnHitAction(Square action){
				return new Action<Square>(){
					@Override
					public void act(Square subject){
						winGame();
					}
				};
			}
		};
		enemy.move(0.5f-enemy.getWidth()/2f,1f-enemy.getHeight());
		addSquare(enemy);
		borders.add(enemy);
	}
	
	public void update(){
		makeEnemyBullets();
		super.update();
	}
	
	public void makeEnemyBullets(){
		if(tick%enemyBulletFrequency==0){
			Square square = new Square("red",0.07f){
				@Override
				public Action<Square> getOnHitAction(Square subject){
					final Square self = this; 
					return new Action<Square>(){
						@Override
						public void act(Square subject) {
						}};
				}
			};
			square.setX(enemyPointerX-0.02f/2f);
			square.setY(enemyPointerY);
			square.setVelocity(enemyBulletSpeed,new Square(enemy));
			addSquare(square);
		}
	}
	
	public void winGame(){
		Gui.removeOnType(this);
		Hub.addLayer.clear();
		Gui.setView(new MainMenu());
	}
}
