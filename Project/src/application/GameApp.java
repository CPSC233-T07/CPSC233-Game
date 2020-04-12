package application;

import java.util.ArrayList;
import java.util.EnumSet;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.core.serialization.Bundle;

import battle.Battle;
import battle.InvalidMoveFormatException;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ui.MenuFactory;

/*
 * Initialization of all basic components of the game using the FXGL library.
 */

public class GameApp extends GameApplication {

	private Entity player;
	
	private Entity enemy;
	private Entity map;
	
	private Level roomLevel;
	private Level mainLevel;
	
	private Battle b;
	private Entity battleMap;
	boolean battleIsFinished = false;
	
	private VBox moves;
	private StackPane enemyFPPane;
	private Text enemyFP;
	private StackPane playerFPPane;
	private Text playerFP;
	private StackPane statusPane;
	private Text status;
	
	boolean isInRoom = false;
	
	int index = 0;
	
	public static final int MAP_WIDTH = 20*32;
	public static final int MAP_HEIGHT = 20*32;
	
	
	private ArrayList<String> froggyDialogue = new ArrayList<String>();
	
	private boolean battle;
	boolean battleStarted;
	
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
		//settings.setMenuEnabled(true);
        settings.setEnabledMenuItems(EnumSet.allOf(MenuItem.class));
		
	}

	
	/*
	 * Initialize the game.
	 */
	@Override
	protected void initGame() {
		
		FXGL.getGameWorld().addEntityFactory(new GameEntityFactory());		//Initialize the entity factory to spawn in the entities
		
		
		
		FXGL.loopBGM("soundtrack.wav");
		
		Entity room1 = FXGL.entityBuilder() 		//Creates the map for the interior of the house, and layers it underneath the large, main map
				.view("Room.png")
				.at(670,1275)				
				.with(new IrremovableComponent())
				.buildAndAttach();
		
		Entity room2 = FXGL.entityBuilder() 		//Creates the map for the interior of the house, and layers it underneath the large, main map
				.view("Room.png")
				.at(2256,2260)				
				.with(new IrremovableComponent())
				.buildAndAttach();
			
		map = FXGL.entityBuilder() 			//Initialize the game map
				.view("EmptyMap.png")
				.buildAndAttach();

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
				if(!battle)
					player.getComponent(PlayerAnimationComponent.class).moveRight();
			}
		}, KeyCode.D );
		input.addAction(new UserAction("Move Left") {
			@Override
			protected void onAction() {
				if(!battle)
					player.getComponent(PlayerAnimationComponent.class).moveLeft();
			}
		}, KeyCode.A);
		input.addAction(new UserAction("Move Up") {
			@Override
			protected void onAction() {
				if(!battle)
					player.getComponent(PlayerAnimationComponent.class).moveUp();
			}
		}, KeyCode.W);
		input.addAction(new UserAction("Move Down") {
			@Override
			protected void onAction() {
				if(!battle)
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
				FXGL.play("move.wav");
				
                FXGL.getCutsceneService().startCutscene(new Cutscene(froggyDialogue));
                
                
                NPCAnimationComponent.moveSpeed = 0;
				
				startCollision();
				battle = true;
				//Start battle
				
			}
			
		});

		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BARRIER) {
			@Override
			protected void onCollisionBegin(Entity player, Entity Barrier) { //New collision Detection between player and barrier.
				
				FXGL.play("bump.wav");
				startCollision();
				
			}
			
		});
		
		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.HOUSE) {
			@Override
			protected void onCollisionBegin(Entity player, Entity Barrier) { //New collision Detection between player and house.
				
				
				Direction playerDirection = PlayerAnimationComponent.getDirection();
				if(playerDirection == Direction.UP)
				{
					enterHouse();					//If the player is facing upwards and colliding with a house, the player will enter the house. For now, its been set up to only work on green houses.
				}
				else
				{
					FXGL.play("bump.wav");
					startCollision();
				}
				
			}
			
		});
	}
	/*
	 * Let's the player enter the house, switching the map to the house interior
	 */
	private void enterHouse() {
		ArrayList<Entity> entitiesToAdd = new ArrayList<Entity>(); // List of enities other than the map and the player that will be in the new "level"
		
		roomLevel = new Level(0,0,entitiesToAdd); //no entities are added, so it only keeps the irremovable objects (the player and the room interiors)
		FXGL.getGameWorld().setLevel(roomLevel);
		isInRoom = true;
	}
	
	
	
	/*
	 * Starts the collision detection when the physics engine senses a collision
	 */
	private void startCollision() {
		
		Direction direction = PlayerAnimationComponent.getDirection();
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
}
	
	/*
	 * Spawns every entity of the barrier type.
	 */
	private void spawnBarriers() {
		
		
		Spawner.spawnNature();
		Spawner.spawnBuildings();
		
	}
	
	
	/*
	 * Basic update function that updates the game every tick
	 */
	@Override
	protected void onUpdate(double TPF) {
		
		if(!battleStarted && battle) {
			beginBattle();
			battleStarted = true;
		}
		
		if(battleIsFinished && battleStarted && battle) {
			battle = false;
			finishBattle();
		}
		
		if(battleStarted && battle) {
			updateBattle();
		}
	}
	
	
	/*
	 * Begins the battle when the player collides with the enemy
	 */
	private void beginBattle() {		
		
		String[] playerMoves = {"Kiss+5", "Hit-5", "Talk Soothingly+10", "Talk Moistly-10"};  //Initialize the player and the enemy move-set
		String[] enemyMoves = {"Threaten-5", "Intimidate-7", "Bad Boy Vibes-10", "Scoff-2"};
		
		try {
			b = new Battle(100, 100, playerMoves, enemyMoves);
		} catch (InvalidMoveFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		VBox moves = new VBox(); //Start setting up the UI for the battle
		Image button = new Image("file:src\\assets\\textures\\button.png");
		Image buttonPushed = new Image("file:src\\assets\\textures\\buttonPushed.png");
		
		for(String move : playerMoves) { // Set up the move buttons
			
			StackPane buttons = new StackPane();
			ImageView iv = new ImageView(button);
			Text t = new Text();
			
			
			buttons.setOnMousePressed((MouseEvent e) -> {
				iv.setImage(buttonPushed);
			});
			
			buttons.setOnMouseReleased((MouseEvent e) -> {
				iv.setImage(button);
				b.playerUseMove(move);
			});
			
			buttons.getChildren().add(iv);

			if(move.indexOf('-') != -1) {
				t.setText(move.substring(0,move.indexOf('-')));
			}else if(move.indexOf('+') != -1) {
				t.setText(move.substring(0,move.indexOf('+')));
			}
			
			buttons.getChildren().add(t);
			
			moves.getChildren().add(buttons);
			
			
		}
		moves.setLayoutX(100);
		moves.setLayoutY(500);
		this.moves = moves;
		FXGL.addUINode(moves);
		
		
		StackPane enemyFPPane = new StackPane(); //set up the "health bars" UI nodes
		ImageView enemyFPBar = new ImageView(new Image("file:src\\assets\\textures\\healthBar.png"));
		Text enemyFP = new Text(b.getEnemyFP());
		
		this.enemyFP = enemyFP;
		this.enemyFPPane = enemyFPPane;
		
		enemyFPPane.getChildren().add(enemyFPBar);
		enemyFPPane.getChildren().add(enemyFP);
		
		
		StackPane playerFPPane = new StackPane();
		ImageView playerFPBar = new ImageView(new Image("file:src\\assets\\textures\\healthBar.png"));
		Text playerFP = new Text(b.getPlayerFP());
		
		this.playerFP = playerFP;
		this.playerFPPane = playerFPPane;
		
		playerFPPane.getChildren().add(playerFPBar);
		playerFPPane.getChildren().add(playerFP);
		
		enemyFPPane.setLayoutX(100);
		enemyFPPane.setLayoutY(100);
		
		playerFPPane.setLayoutX(100);
		playerFPPane.setLayoutY(400);
		
		FXGL.addUINode(enemyFPPane);
		FXGL.addUINode(playerFPPane);
		
		
		StackPane statusPane = new StackPane(); //Set up the status box UI node
		ImageView statusBox = new ImageView(new Image("file:src\\assets\\textures\\TextBox.png"));
		Text status = new Text(b.getStatus());
		
		statusPane.getChildren().add(statusBox);
		statusPane.getChildren().add(status);
		statusPane.setLayoutX(300);
		statusPane.setLayoutY(500);
		
		this.statusPane = statusPane;
		this.status = status;
		
		FXGL.addUINode(statusPane);
		
		

		
		
	}
	
	
	/*
	 * Update the battle, healthbars and status text
	 */
	private void updateBattle(){
		battleIsFinished = b.isFinished();
		this.status.setText(b.getStatus());
		this.playerFP.setText(b.getPlayerFP());
		this.enemyFP.setText(b.getEnemyFP());
	}
	
	/*
	 * Called when the battle finishes, cleans up the UI, removes unnessassary components and starts an end-of-battle cutscene
	 */
	private void finishBattle() {
		ArrayList<String> endStatus = new ArrayList<String>();
		FXGL.removeUINode(statusPane);
		FXGL.removeUINode(enemyFPPane);
		FXGL.removeUINode(playerFPPane);
		FXGL.removeUINode(moves);
		
		enemy.removeFromWorld();
		
		if(b.getWinner().equals("PlayerF")) {
			endStatus.add("Player : I won the battle!");
			endStatus.add("Player : The enemy is now my friend!");
			endStatus.add("Player : Yay!");
		}else if(b.getWinner().equals("PlayerU")) {
			endStatus.add("Player : I won the battle!");
			endStatus.add("Player : But I made the enemy sad...");
			endStatus.add("Player : They are going to go home and cry now...");
		}else {
			endStatus.add("Player : How unfortunate...");
			endStatus.add("Player : I lost...");
			endStatus.add("Player : but mom said I can do anything!?");
		}
		
		FXGL.getCutsceneService().startCutscene(new Cutscene(endStatus));
		

		

	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
