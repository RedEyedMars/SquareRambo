package game;

import game.menu.StoryAction;
import main.Hub;

public class WinnableEndlessMode extends EndlessMode{

	private int killLimit;
	public WinnableEndlessMode(int killLimit) {
		super();
		this.killLimit = killLimit;
		
	}
	@Override
	public int maxReds(){
		if(score.getValue()<=killLimit-(super.maxReds())){
			return super.maxReds();
		}
		else {
			return -1;
		}
	}
	@Override
	public void update(){
		if(score.getValue()>=killLimit){
			Hub.addLayer.clear();
			winGame();
		}
		else super.update();
	}
	protected void winGame(){
		
	}
}
