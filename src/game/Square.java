package game;

import gui.graphics.GraphicEntity;

public class Square extends GraphicEntity{

	private float speed = 0f;
	private Square targetSquare;
	private String side;
	public Square(String affiliation, float scale) {
		super(affiliation);
		this.adjust(scale, scale);
		this.side = affiliation;
	}

	public Square(Square square) {
		this(square.getSide(), square.getWidth());
		this.move(square.getX(),square.getY());
	}

	public void move(float x, float y){
		this.setX(x);
		this.setY(y);
	}

	public void setVelocity(float v, Square targetSquare) {
		this.speed = v;
		this.targetSquare = targetSquare;
	}

	@Override
	public void update(){
		if(this.targetSquare!=null){
			float angle = (float)Math.atan2(
					targetSquare.getY()-this.getY()+targetSquare.getHeight()/2f-this.getHeight()/2f,
					targetSquare.getX()-this.getX()+targetSquare.getWidth()/2f-this.getWidth()/2f);
			float ypush = (float)Math.sin(angle);
			float xpush = (float)Math.cos(angle);
			move(this.entity.getX()-xpush*speed,
					(this.getY())-ypush*speed);
			
			//targetSquare = null;
		}
	}


	public void setSide(String string) {
		this.side = string;
	}
	public String getSide() {
		return side;
	}

	public Action<Square> getOnHitAction(Square black) {
		return new Action<Square>(){
			@Override
			public void act(Square subject) {				
			}};
	}

	public void increaseSpeed(float f) {
		this.speed+=f;
	}

	public float getSpeed() {
		return speed;
	}

}

