package entities;

import java.util.ArrayList;
import java.util.Arrays;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

/*
 * Initializes the walking for the player and animates the sprite.
 */

public class PlayerAnimationComponent extends Component{
	
	private static int speedX=0;
	private static int speedY=0;
	public static int moveSpeed = 200;
	private static final int ANIM_SPEED = 1;
	
	public static ArrayList<Direction> validDirections = new ArrayList<Direction>();
	

	private static Direction direction = Direction.DOWN;
	
	private AnimatedTexture texture;
	private AnimationChannel animStopU,animStopD,animStopH,animWalkUp,animWalkD,animWalkH;
	
	public PlayerAnimationComponent(String spriteSheetName) {
		validDirections = resetValidDirections();
		//Importing sprite sheet, defining frames for each animated direction
		animStopU=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),9,9);
		animStopD=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),1,1);
		animStopH=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),5,5);
		
		
		animWalkUp=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),8,11);
		animWalkD=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),0,3);
		animWalkH=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),4,7);
		
		texture = new AnimatedTexture(animStopD);
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
        
        if (speedX != 0) {
        	if(texture.getAnimationChannel() != animWalkH) {
        		texture.loopAnimationChannel(animWalkH);
        	}
            speedX = (int) (speedX * 0.1);

            if (FXGLMath.abs(speedX) < 1) {
                speedX =  0;
                texture.loopAnimationChannel(animStopH);
            }
        }else if (speedY != 0) {
        	 if (speedY > 0) {
        		 if(texture.getAnimationChannel() != animWalkD) {
             		texture.loopAnimationChannel(animWalkD);
             		direction = Direction.DOWN; 
             	}
        	 }	 
        	else if(speedY < 0) {
        		if(texture.getAnimationChannel() != animWalkUp) {
                  texture.loopAnimationChannel(animWalkUp);
                  direction = Direction.UP;
                 } 
        	}
                 speedY = (int) (speedY * 0.1);

                 if (FXGLMath.abs(speedY) < 1) {
                     speedY = 0;
                     if (direction == Direction.DOWN)
                     {
                    	 texture.loopAnimationChannel(animStopD);
                     }
                     else
                     {
                    	 texture.loopAnimationChannel(animStopU);
                     }
                 }
        	 
        }
        
   
	}
	
	public void moveUp() {
		if(validDirections.contains(Direction.UP)) {
			speedY = -moveSpeed;
			speedX = 0;
			direction = Direction.UP;
			validDirections = resetValidDirections();
		}

	}
	public void moveDown() {
		if(validDirections.contains(Direction.DOWN)) {
			speedY = moveSpeed;
			speedX = 0;
			direction = Direction.DOWN;
			validDirections = resetValidDirections();
		}

	}
	public void moveLeft() {
		if(validDirections.contains(Direction.LEFT)) {
			speedX = -moveSpeed;
			speedY = 0;
			getEntity().setScaleX(-1);
			direction = Direction.LEFT;
			validDirections = resetValidDirections();
		}

	}
	public void moveRight() {
		if(validDirections.contains(Direction.RIGHT)) {
			speedX = moveSpeed;
			speedY = 0;	
			getEntity().setScaleX(1);
			direction = Direction.RIGHT;
			validDirections = resetValidDirections();
		}

	}
	
	public static Direction getDirection() {
		return direction;
	}

	

	private ArrayList<Direction> resetValidDirections() {
		ArrayList<Direction> d = new ArrayList<Direction>();
		
		d.add(Direction.UP);
		d.add(Direction.DOWN);
		d.add(Direction.RIGHT);
		d.add(Direction.LEFT);
		
		return d;
	}
	
	

}
