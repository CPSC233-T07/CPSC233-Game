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
		
		FXGL.spawn("lightHouse",420,200);
		
		
		FXGL.spawn("bush",977,681);
		FXGL.spawn("bush",817,999);
		FXGL.spawn("rock",870,1064);
		FXGL.spawn("rock",961,926);
		FXGL.spawn("greenHouse",800,1410);
		
		FXGL.spawn("tree",1035,555);
		FXGL.spawn("bush",1045,640);
		FXGL.spawn("tree",1035,655);
		FXGL.spawn("bush",1045,740);
		FXGL.spawn("appleTree",1035,755);
		FXGL.spawn("bush",1045,840);
		FXGL.spawn("tree",1035,855);
		FXGL.spawn("bush",1045,940);
		FXGL.spawn("appleTree",1035,955);
		FXGL.spawn("bush",1045,1040);
		FXGL.spawn("appleTree",1035,1055);
		FXGL.spawn("bush",1045,1140);
		FXGL.spawn("tree",1035,1155);
		FXGL.spawn("bush",1045,1240);
		
		FXGL.spawn("tree",1125,1200);
		FXGL.spawn("bush",1190,1240);
		FXGL.spawn("tree",1225,1200);
		FXGL.spawn("bush",1290,1240);
		FXGL.spawn("bush",1390,1240);
		FXGL.spawn("tree",1325,1200);
		FXGL.spawn("tree",1425,1200);
		
		FXGL.spawn("tree",1235,555);
		FXGL.spawn("bush",1245,640);
		FXGL.spawn("tree",1235,655);
		FXGL.spawn("bush",1245,740);
		FXGL.spawn("appleTree",1235,755);
		FXGL.spawn("bush",1245,840);
		FXGL.spawn("tree",1235,855);
		FXGL.spawn("bush",1245,940);
		FXGL.spawn("appleTree",1235,955);
		FXGL.spawn("bush",1245,1040);
		FXGL.spawn("appleTree",1235,1045);
		
		
		FXGL.spawn("blueHouse", 1370, 670);
		FXGL.spawn("appleTree",1470,670);
		FXGL.spawn("redHouse", 1570, 670);
		FXGL.spawn("appleTree",1670,670);
		FXGL.spawn("redHouse", 1370, 870);
		FXGL.spawn("appleTree",1470,870);
		FXGL.spawn("blueHouse", 1570,870);
		FXGL.spawn("appleTree",1670,870);
		
		
		FXGL.spawn("appleTree",1300,1870);
		FXGL.spawn("bush",1900,1900);
		FXGL.spawn("rock",1500,1870);
		FXGL.spawn("tree",1617,1870);
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
