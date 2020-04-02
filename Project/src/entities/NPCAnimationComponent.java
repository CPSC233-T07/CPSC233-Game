package entities;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class NPCAnimationComponent extends Component{
	
	private AnimatedTexture texture;
	private AnimationChannel animStopU,animStopV,animStopH,animWalkUp,animWalkV,animWalkH;
	public static int moveSpeed = 2;
	private static final int ANIM_SPEED = 1;
	
	private final int MAX = 200;
	private final int MIN = 0;
	
	private int walkedX = MIN;
	private int walkedY = MIN;
	
	Direction direction = Direction.RIGHT;
	
	public NPCAnimationComponent(String spriteSheetName) {
		animStopU=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),9,9);
		animStopV=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),1,1);
		animStopH=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),5,5);
		
		
		animWalkUp=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),8,11);
		animWalkV=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),0,3);
		animWalkH=new AnimationChannel(FXGL.image(spriteSheetName),16,32,32,Duration.seconds(ANIM_SPEED),4,7);
		
		texture = new AnimatedTexture(animWalkH);
		
	}
	
	@Override
	public void onAdded() {
		entity.getTransformComponent().setScaleOrigin(new Point2D(16,21));
		entity.getViewComponent().addChild(texture);
	}
	@Override
	public void onUpdate(double tpf) {
		switch(direction) {
		case DOWN:
			walkedY += moveSpeed;
			entity.translateY(moveSpeed);
			break;
		case UP:
			walkedY -= moveSpeed;
			entity.translateY(-moveSpeed);
			break;
		case LEFT:
			walkedX -= moveSpeed;
			entity.translateX(-moveSpeed);
			break;
		case RIGHT:
			walkedX += moveSpeed;
			entity.translateX(moveSpeed);
			break;
		}
		
		if(walkedX >= MAX && direction.equals(Direction.RIGHT)) {
			direction = Direction.DOWN;
			texture.loopAnimationChannel(animWalkV);
		}else if(walkedY >= MAX && direction.equals(Direction.DOWN)) {
			direction = Direction.LEFT;
			texture.loopAnimationChannel(animWalkH);
			entity.setScaleX(-1);
		}else if(walkedX <= MIN && direction.equals(Direction.LEFT)) {
			direction = Direction.UP;
			texture.loopAnimationChannel(animWalkUp);
		}else if(walkedY <= MIN && direction.equals(Direction.UP)) {
			direction = Direction.RIGHT;
			texture.loopAnimationChannel(animWalkH);
			entity.setScaleX(1);
		}
	}
}
