package application;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.event.EventBus;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.PauseMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.texture.Texture;
import java.util.ArrayList;


import entities.PlayerAnimationComponent;
import entities.Direction;
import entities.EntityType;
import entities.GameEntityFactory;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import ui.MenuFactory;


public class GameApp extends GameApplication {

	private Entity player;
	
	private Entity enemy;
	private Entity map;
		
	public static final int MAP_WIDTH = 20*32;
	public static final int MAP_HEIGHT = 20*32;
		
/*
 * initialize basic settings.
 */
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(MAP_WIDTH);
		settings.setHeight(MAP_HEIGHT);
		settings.setTitle("FriendMaker2077");
		settings.setVersion("0.1");
		
		settings.setSceneFactory(new MenuFactory());
	}

	
	/*
	 * Initialize the player entity.
	 */
	@Override
	protected void initGame() {
		
		map = FXGL.entityBuilder()
				.view("EmptyMap.png")
				.buildAndAttach();
		FXGL.getGameWorld().addEntityFactory(new GameEntityFactory());
		
//		player=FXGL.entityBuilder().at(MAP_WIDTH/2, MAP_HEIGHT/2)
//				.type(EntityType.PLAYER)
//				.with(new AnimationComponent("CharacterSprite.png"))
//				.with(new CollidableComponent(true))
//				.buildAndAttach();
		
				
//		enemy=FXGL.entityBuilder().at(350,350)
//				.type(EntityType.FROGGY)
//				.with(new AnimationComponent("froggySprite.png"))
//				.with(new CollidableComponent(true))
//				.buildAndAttach();
		
		player = FXGL.spawn("player");
		//enemy = FXGL.spawn("enemy");
		FXGL.spawn("blueHouse", 400, 400);
		

		FXGL.getGameScene().getViewport().bindToEntity(player, player.getX(), player.getY()); //This should let the "camera" follow the player
	}

	/*
	 * Initializes the input of the game
	 */
	@Override
	protected void initInput() {
		
		//Create a new input object and add actions to it adding an action is an implicit method overriding.
		Input input = FXGL.getInput();
		
		input.addAction(new UserAction("Move Right") {
			@Override
			protected void onAction() {
				player.getComponent(PlayerAnimationComponent.class).moveRight();
			}
		}, KeyCode.D);
		input.addAction(new UserAction("Move Left") {
			@Override
			protected void onAction() {
				player.getComponent(PlayerAnimationComponent.class).moveLeft();
			}
		}, KeyCode.A);
		input.addAction(new UserAction("Move Up") {
			@Override
			protected void onAction() {
				player.getComponent(PlayerAnimationComponent.class).moveUp();
			}
		}, KeyCode.W);
		input.addAction(new UserAction("Move Down") {
			@Override
			protected void onAction() {
				player.getComponent(PlayerAnimationComponent.class).moveDown();
			}
		}, KeyCode.S);
		
		input.addAction(new UserAction("Play Sound") {
		    @Override
		    protected void onActionBegin() {
		        FXGL.play("sound.wav");
		    }
		}, KeyCode.F); 
		
	}
	
	@Override
	protected void initPhysics() {
		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.FROGGY) {
			@Override
			protected void onCollisionBegin(Entity player, Entity Froggy) {	
				System.out.println("Colliding With Froggy");
				startCollision();
			}
			
		});
		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BARRIER) {
			@Override
			protected void onCollisionBegin(Entity player, Entity Barrier) {
				System.out.println("Colliding With Immoveable Object");
				System.out.println(player.getHeight());
				startCollision();
				
			}
			
		});
		
	}
	
	/*s
	 * Initialize the UI of the game, not much here for now, lets change that.d
	 */
	
	@Override
	protected void initUI() {
		Text textPixels = new Text();
		Texture brickTexture = FXGL.getAssetLoader().loadTexture("brick.png");
		
		textPixels.setTranslateX(50);
		textPixels.setTranslateY(100);
		
		brickTexture.setTranslateX(50);
		brickTexture.setTranslateY(450);

	}
	
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("pixelsMoved",0);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private void startCollision() {
		Direction direction = PlayerAnimationComponent.getDirection();
		System.out.println(PlayerAnimationComponent.validDirections);
		switch(direction) {
		case DOWN:
			PlayerAnimationComponent.validDirections.remove(Direction.DOWN);
			player.translateY(-5);
			break;
		case UP:
			PlayerAnimationComponent.validDirections.remove(Direction.UP);
			player.translateY(+5);
			break;
		case LEFT:
			PlayerAnimationComponent.validDirections.remove(Direction.LEFT);
			player.translateX(+5);
			break;
		case RIGHT:
			PlayerAnimationComponent.validDirections.remove(Direction.RIGHT);
			player.translateX(-5);
			break;
		}
		System.out.println(PlayerAnimationComponent.validDirections);
	}
}
