package application;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;

import audio.MusicPlayer;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.AudioType;
import com.almasb.fxgl.audio.Music;

import entities.PlayerAnimationComponent;
import entities.Direction;
import entities.EntityType;
import entities.GameEntityFactory;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import ui.MenuFactory;

/*
 * Initialization of all basic components of the game using the FXGL library.
 */

public class GameApp extends GameApplication {

	private Entity player;
	
	private Entity enemy;
	private Entity map;
	
	testbattle l = new testbattle();
		
	public static final int MAP_WIDTH = 20*32;
	public static final int MAP_HEIGHT = 20*32;
	
	private boolean battle;
	
	MusicPlayer a = new MusicPlayer();
/*
 * initialize basic settings.
 */
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(MAP_WIDTH);
		settings.setHeight(MAP_HEIGHT);
		settings.setTitle("FriendMaker2077");
		settings.setVersion("0.1");
		
		battle = false;
		
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
		
	    a.playAsynchronousLooped("src\\music\\soundtrack.wav");
		
		
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
		}, KeyCode.D );
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
			protected void onCollisionBegin(Entity player, Entity Froggy) {	//New collision Detection between player and froggy
				System.out.println("Colliding With Froggy");
				FXGL.play("sound.wav");
				FXGL.play("move.wav");
				startCollision();
				battle = true;
				//Start battle
				
			}
			
		});

		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BARRIER) {
			@Override
			protected void onCollisionBegin(Entity player, Entity Barrier) { //New collision Detection between player and barrier.
				System.out.println("Colliding With Immoveable Object");
				System.out.println(player.getHeight());
				FXGL.play("bump.wav");
				startCollision();
				
			}
			
		});
		
	}
	
	/*
	 * Starts the collision detection when the physics engine senses a collision
	 */
	private void startCollision() {
		
		Direction direction = PlayerAnimationComponent.getDirection();
		System.out.println(PlayerAnimationComponent.validDirections);
		switch(direction) {
		case DOWN:
			PlayerAnimationComponent.validDirections.remove(Direction.DOWN); //Make the player not able to move down.
			player.translateY(-7); //Move the player up 7 pixels to prevent moving through wall
			break;
		case UP:
			PlayerAnimationComponent.validDirections.remove(Direction.UP); //Make the player not able to move up.
			player.translateY(+7); //Move the player down 7 pixels to prevent moving through wall
			break;
		case LEFT:
			PlayerAnimationComponent.validDirections.remove(Direction.LEFT); //Make the player not able to move left.
			player.translateX(+7); //Move the player right 7 pixels to prevent moving through wall
			break;
		case RIGHT:
			PlayerAnimationComponent.validDirections.remove(Direction.RIGHT); //Make the player not able to move right.
			player.translateX(-7); //Move the player left 7 pixels to prevent moving through wall
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
	
	@Override
	protected void onUpdate(double TPF) {
		if(battle) {
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
