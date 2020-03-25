package entities;


import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;



public class AnimationComponent extends Component{
	
	private int speedX=0;
	private int speedY=0;
	
	private boolean moving = false;
	private enum Direction {RIGHT, LEFT, DOWN, UP};
	private Direction direction = Direction.DOWN;
	
	private AnimatedTexture texture;
	private AnimationChannel animStopU,animStopV,animStopL,animStopH,animWalkUp,animWalkV,animWalkLeft
	,animWalkH;
	
	public AnimationComponent() {
		
		//Importing sprite sheet, defining frames for each animated direction
		animStopU=new AnimationChannel(FXGL.image("CharacterSprite.png"),16,32,32,Duration.seconds(1),9,9);
		animStopV=new AnimationChannel(FXGL.image("CharacterSprite.png"),16,32,32,Duration.seconds(1),1,1);
		animStopL=new AnimationChannel(FXGL.image("CharacterSprite.png"),16,32,32,Duration.seconds(1),12,12);
		animStopH=new AnimationChannel(FXGL.image("CharacterSprite.png"),16,32,32,Duration.seconds(1),5,5);
		
		
		animWalkUp=new AnimationChannel(FXGL.image("CharacterSprite.png"),16,32,32,Duration.seconds(1),8,11);
		animWalkV=new AnimationChannel(FXGL.image("CharacterSprite.png"),16,32,32,Duration.seconds(1),0,3);
		animWalkLeft=new AnimationChannel(FXGL.image("CharacterSprite.png"),16,32,32,Duration.seconds(1),12,15);
		animWalkH=new AnimationChannel(FXGL.image("CharacterSprite.png"),16,32,32,Duration.seconds(1),4,7);
		
		texture = new AnimatedTexture(animStopV);
	}
	

	@Override
	public void onAdded() {
		entity.getTransformComponent().setScaleOrigin(new Point2D(16,21));
		entity.getViewComponent().addChild(texture);
	}
	@Override
	public void onUpdate(double tpf) {
		
        entity.translateX(speedX * tpf);
        entity.translateY(speedY * tpf);
        
        System.out.println("X: "+ speedX +", Y: "+speedY);
        
        if (speedX != 0) {
        	if(texture.getAnimationChannel() != animWalkH) {
        		texture.loopAnimationChannel(animWalkH);
        	}
            speedX = (int) (speedX * 0.1);
            moving = true;

            if (FXGLMath.abs(speedX) < 1) {
                speedX =  0;
                texture.loopAnimationChannel(animStopH);
                moving = false;
            }
        }else if (speedY != 0) {
        	 if (speedY != 0) {
        		 if(texture.getAnimationChannel() != animWalkV) {
             		texture.loopAnimationChannel(animWalkV);
             	}
                 speedY = (int) (speedY * 0.1);
                 moving  = true;

                 if (FXGLMath.abs(speedY) < 1) {
                     speedY = 0;
                     texture.loopAnimationChannel(animStopV);
                     moving  = false;
                 }
        	 }
        }
//        System.out.println(moving);
//        System.out.println(direction);
//        if(moving) {
//			switch (direction) {
//			case DOWN:
//				texture.loopAnimationChannel(animWalkDown);
//			case UP:
//				texture.loopAnimationChannel(animWalkUp);
//			case LEFT:
//				texture.loopAnimationChannel(animWalkLeft);
//			case RIGHT:
//				texture.loopAnimationChannel(animWalkRight);
//			}
//        }else {
//        	switch (direction) {
//			case DOWN:
//				texture.loopAnimationChannel(animStopD );
//			case UP:
//				texture.loopAnimationChannel(animStopU);
//			case LEFT:
//				texture.loopAnimationChannel(animStopL);
//			case RIGHT:
//				texture.loopAnimationChannel(animStopR);
//			}
//        }
//        
	}
	
	public void moveUp() {
		speedY = -100;
		speedX = 0;
		direction = Direction.UP;
	}
	public void moveDown() {
		speedY = 100;
		speedX = 0;
		direction = Direction.DOWN;
	}
	public void moveLeft() {
		speedX = -100;
		speedY = 0;
		getEntity().setScaleX(-1);
		direction = Direction.LEFT;
	}
	public void moveRight() {
		speedX = 100;
		speedY = 0;	
		getEntity().setScaleX(1);
		direction = Direction.RIGHT;
	}
	
	
	
	
	

}
