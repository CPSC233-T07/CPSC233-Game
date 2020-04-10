package application;

import java.util.ArrayList;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;

import audio.MusicPlayer;
import battle.Battle;
import battle.InvalidMoveFormatException;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.AudioType;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.cutscene.Cutscene;
import com.almasb.fxgl.cutscene.dialogue.DialogueGraph;

import entities.PlayerAnimationComponent;
import entities.Spawner;
import entities.Direction;
import entities.EntityType;
import entities.GameEntityFactory;
import entities.NPCAnimationComponent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
	
	private ArrayList<String> froggyDialogue = new ArrayList<String>();
	
	private boolean battle;
	boolean battleStarted;
	
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
		battleStarted = false;
		
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
		
		FXGL.loopBGM("soundtrack.wav");
		
		FXGL.getGameWorld().addEntityFactory(new GameEntityFactory());		//Initialize the entity factory to spawn in the entities
		
		player = FXGL.spawn("player");		//spawn in the player
		enemy = FXGL.spawn("enemy",600,600);
		
		spawnBarriers(); //@Mckenzie, when making the map use this function to place the pieces where you want.
		
		froggyDialogue.add("Bad Boy Froggy : Are you running into me punk?");
		froggyDialogue.add("Bad Boy Froggy : You better get ready to fight then");
		froggyDialogue.add("Bad Boy Froggy : ITS ON PUNK");

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
				FXGL.play("move.wav");
				
                FXGL.getCutsceneService().startCutscene(new Cutscene(froggyDialogue));
                
                
                NPCAnimationComponent.moveSpeed = -NPCAnimationComponent.moveSpeed;
				
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
		
		
		Spawner.spawnNature();
		Spawner.spawnBuildings();
		
	}
	
	@Override
	protected void onUpdate(double TPF) {
		if(!battleStarted && battle) {
			beginBattle();
			battleStarted = true;
		}
	}
	
	private void beginBattle() {
		ArrayList<Entity> battleEntities = new ArrayList<Entity>();
		Level battle = new Level(600,600,battleEntities);
		
		FXGL.getGameWorld().setLevel(battle);
		
		String[] playerMoves = {"Kiss+5", "Hit-5", "Talk Soothingly+10", "Talk Moistly-10"};
		String[] enemyMoves = {"Threaten-5", "Intimidate-7", "Bad Boy Vibes-10", "Scoff-2"};
		
		try {
			Battle b = new Battle(player, enemy, 100, 100, playerMoves, enemyMoves);
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMoveFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		VBox moves = new VBox();
		Image button = new Image("file:src\\assets\\textures\\button.png");
		Image buttonPushed = new Image("file:src\\assets\\textures\\buttonPushed.png");
		
		for(String move : playerMoves) {
			ImageView iv = new ImageView(button);
			iv.setOnMousePressed((MouseEvent e) -> {
				iv.setImage(buttonPushed);
				System.out.println("pressed");
			});
			
			iv.setOnMouseReleased((MouseEvent e) -> {
				iv.setImage(button);
				System.out.println("Released");
			});
			
			System.out.println("Created");

//			if(move.indexOf('-') != -1) {
//				iv.setOnMousePressed((MouseEvent e) -> {
//					iv.setImage(buttonPushed);
//					System.out.println("pressed");
//				});
//				
//				iv.setOnMouseReleased((MouseEvent e) -> {
//					iv.setImage(button);
//					System.out.println("Released");
//				});
//			}else if(move.indexOf('+') != -1) {
//				iv.setOnMousePressed((MouseEvent e) -> {
//					iv.setImage(buttonPushed);
//					System.out.println("pressed");
//				});
//				
//				iv.setOnMouseReleased((MouseEvent e) -> {
//					iv.setImage(button);
//					System.out.println("Released");
//				});
//			}
			
			moves.getChildren().add(iv);
			
		}
		moves.setLayoutX(300);
		moves.setLayoutY(300);
		FXGL.addUINode(moves);
		
		FXGL.getCutsceneService().onExit();
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
