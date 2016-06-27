package game;

import java.util.ArrayList;
import java.util.List;

import game.menu.HighscoreMenu;
import game.menu.GraphicNumber;
import game.menu.MainMenu;
import gui.Gui;
import gui.graphics.GraphicEntity;
import gui.graphics.GraphicView;
import gui.inputs.KeyBoardListener;
import gui.inputs.MotionEvent;
import main.Hub;

public class Game extends GraphicView implements KeyBoardListener{
	protected List<Square> squares = new ArrayList<Square>();
	protected List<Square> borders = new ArrayList<Square>();
	protected List<Square> reds = new ArrayList<Square>();
	protected List<Square> blacks = new ArrayList<Square>();
	
	protected Square hero;
	
	protected float bulletSpeed = 0.025f;;
	protected int bulletFrequency = 10;

	private float pointerX = 0.05f;
	private float pointerY = 0.05f;

	protected int tick = 1;
	
	protected GraphicNumber score;
	private boolean endGame = false;
	public Game(){
		addChild(new Square("white",1f));
		
		score = new GraphicNumber();
		score.setX(1f);
		score.setValue(0);
		addChild(score);

		hero = new Square("black",0.075f){
			@Override
			public Action<Square> getOnHitAction(Square hitter){
				return new Action<Square>(){
					@Override
					public void act(Square subject) {
						if("red".equals(subject.getSide())){
							endGame();
						}
					}
				};
			}};
		hero.setX(0.5f-hero.getWidth()/2f);
		addSquare(hero);
			
		Gui.giveOnType(this);
	}
	
	protected void addBorder(float x, float y){
		Square border = new Square("red",1f){
			@Override
			public Action<Square> getOnHitAction(Square action){
				return new Action<Square>(){
					@Override
					public void act(Square subject){
						removeSquare(subject);
					}
				};
			}
		};
		border.move(x,y);
		addSquare(border);
		borders.add(border);
	}

	protected void addSquare(Square square){
		squares.add(square);
		if("red".equals(square.getSide())){
			reds.add(square);
		}
		else if("black".equals(square.getSide())){
			blacks.add(square);
		}
		addChild(square);
	}

	@Override
	public void update(){
		handleInterceptions();
		makeBullets();
		super.update();
	}

	public void removeSquare(Square square){
		removeChild(square);
	}

	@Override
	public void removeChild(GraphicEntity e){
		if(e instanceof Square){
			Square square = (Square)e;
			squares.remove(square);
			if("red".equals(square.getSide())){
				reds.remove(square);
			}
			else if("black".equals(square.getSide())){
				blacks.remove(square);
			}
		}
		super.removeChild(e);
	}

	private void handleInterceptions(){
		List<Action<Square>> onHandle = new ArrayList<Action<Square>>();
		List<Square> subjects = new ArrayList<Square>();
		for(Square black:blacks){
			for(Square red:reds){
				Square p = red;
				Square q = black;
				if(red.getWidth()<black.getWidth()){
					p=black;
					q=red;
				}
				if(p.isWithin(q.getX(), q.getY())||
						p.isWithin(q.getX()+q.getWidth(), q.getY()+q.getHeight())||
						p.isWithin(q.getX(), q.getY()+q.getHeight())||
						p.isWithin(q.getX()+q.getWidth(), q.getY())){
					onHandle.add(black.getOnHitAction(red));
					subjects.add(red);
					//onHandle.add(red.getOnHitAction(black));
				}
			}
		}
		for(int i=0;i<onHandle.size();++i){
			if(!endGame){
				onHandle.get(i).act(subjects.get(i));
			}
		}
	}

	@Override
	public boolean onHover(MotionEvent event){
		pointerX = event.getX();
		pointerY = event.getY();
		return true;
	}

	public void makeBullets(){
		if(tick%bulletFrequency==0){
			Square square = new Square("black",0.01f){
				@Override
				public Action<Square> getOnHitAction(Square subject){
					final Square self = this; 
					return new Action<Square>(){
						@Override
						public void act(Square subject) {
							if(!borders.contains(subject)){
								removeSquare(subject);
								removeSquare(self);
								score.inc();
							}
							else {
								subject.getOnHitAction(subject).act(self);;
							}
						}};
				}
			};

			double angle = Math.atan2(pointerY, 
					pointerX+hero.getWidth()/2f-Math.PI/2*hero.getX());
			float dx = (float) (Math.cos(angle)*0.05f);
			float dy = (float) (Math.sin(angle)*0.05f);
			square.setX(dx+hero.getX()+hero.getWidth()/2f);
			square.setY(dy+hero.getHeight()/2f);
			square.setVelocity(bulletSpeed,new Square(hero));
			addSquare(square);
		}
		++tick;
		if(tick<0)tick=1;
	}

	public void endGame(){
		Gui.removeOnType(this);
		Hub.addLayer.clear();
		Hub.removeLayer.addAll(Hub.drawLayer);
		if(Hub.getValue("endless",Hub.highscores)<=score.getValue()||
			Hub.getValue("endless2",Hub.highscores)<=score.getValue()||
			Hub.getValue("endless3",Hub.highscores)<=score.getValue()){
			Gui.setView(new HighscoreMenu("endless",score.getValue()));
		}
		else {
			Gui.setView(new MainMenu());
		}
	}

	@Override
	public void input(char c, int keycode) {
		if('a'==c){
			hero.move(hero.getX()-0.01f, hero.getY());
			while(hero.getX()<0f){
				hero.move(hero.getX()+0.01f, hero.getY());
			}
		}
		if('d'==c){
			hero.move(hero.getX()+0.01f, hero.getY());
			while(hero.getX()+hero.getWidth()>=1f){
				hero.move(hero.getX()-0.01f, hero.getY());
			}
		}
	}

	@Override
	public boolean continuousKeyboard() {
		return true;
	}
}
