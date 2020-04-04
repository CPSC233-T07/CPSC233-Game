package application;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.app.GameSettings;
import entities.PlayerAnimationComponent;
import entities.Direction;
import entities.EntityType;
import entities.GameEntityFactory;
import javafx.scene.input.KeyCode;
import ui.MenuFactory;

/*
 * Initialization of all basic components of the game using the FXGL library.
 */

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
	 * Initialize the game.
	 */
	@Override
	protected void initGame() {
		
		map = FXGL.entityBuilder() 			//Initialize the game map
				.view("EmptyMap.png")
				.buildAndAttach();
	        FXGL.play("soundtrack.wav");
		
		
		FXGL.getGameWorld().addEntityFactory(new GameEntityFactory());		//Initialize the entity factory to spawn in the entities
		
		player = FXGL.spawn("player");		//spawn in the player
		enemy = FXGL.spawn("enemy",600,600);
		
		spawnBarriers(); //@Mckenzie, when making the map use this function to place the pieces where you want.
		
				

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
	
	/*
	 * Starts the collision detection when the physics engine senses a collision
	 */
	private void startCollision() {
		FXGL.play("bump.wav");
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
	
	/*
	 * Spawns every entity of the barrier type.
	 */
	private void spawnBarriers() {
		FXGL.spawn("blueHouse", 400, 400);
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
