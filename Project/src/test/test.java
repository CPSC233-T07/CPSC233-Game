package test;

import com.almasb.fxgl.app.GameApplication;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.cutscene.Cutscene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.scene.SubScene;

import application.GameApp;
import entities.EntityType;
import entities.GameEntityFactory;
import entities.PlayerAnimationComponent;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;

public class test extends GameApplication {
	public enum EntityType {
	    PLAYER, COIN
	}
	Level level1;
	Level level2;
	
	private Entity player;
	
    private Entity square;
    private Entity newMap;

	
	private Entity map;
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(600);
        settings.setHeight(600);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");
        //settings.setMenuEnabled(true);
        //settings.setEnabledMenuItems(EnumSet.allOf(MenuItem.class));

    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
            	player.getComponent(PlayerAnimationComponent.class).moveRight();
                FXGL.getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
            	player.getComponent(PlayerAnimationComponent.class).moveLeft();
                FXGL.getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
            	player.getComponent(PlayerAnimationComponent.class).moveUp();
            	FXGL.getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
            	player.getComponent(PlayerAnimationComponent.class).moveDown();
                FXGL.getGameState().increment("pixelsMoved", +5);
            }
        }, KeyCode.S);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }
    
    
    @Override
    protected void initGame() { 
    	
    	player = FXGL.entityBuilder().at(GameApp.MAP_WIDTH/2, GameApp.MAP_HEIGHT/2)
				.type(EntityType.PLAYER)
				.with(new PlayerAnimationComponent("player.png"))
				.viewWithBBox("bbox.png")
				.with(new CollidableComponent(true))
				.with(new IrremovableComponent())
				.build();
    	  	
    	newMap = FXGL.entityBuilder() 
        		.at(330,70)
				.view("Room.png") //map is 300 by 300
				.with(new IrremovableComponent())
				.build();
    	
    	FXGL.getGameWorld().addEntityFactory(new GameEntityFactory());
    	ArrayList<Entity> e = new ArrayList<Entity>();
    	
    	map = FXGL.entityBuilder() 			//Initialize the game map
				.view("EmptyMap.png")
				.build();
    
        
        square =  FXGL.entityBuilder()
                .type(EntityType.COIN)
                .at(500, 200)
                .viewWithBBox(new Rectangle(25,25,Color.BLUE))
                .with(new CollidableComponent(true))
                .build();
       
        e.add(newMap);
        e.add(map);
        e.add(player);
        e.add(square);
        
        level1 = new Level(600, 600, e);
        
        FXGL.getGameWorld().setLevel(level1);
        FXGL.getGameScene().getViewport().bindToEntity(player, player.getX(), player.getY());
       
    }
    
    
    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
            	ArrayList<String> s = new ArrayList<String>();
            	s.add("monkey:uh oh");
            	s.add("player:what?");
            	s.add("monkey:STINKYYYYYY");
            	
               //FXGL.getCutsceneService().startCutscene(new Cutscene(s));

                ArrayList<Entity> e = new ArrayList<Entity>();  //LEVEL SWITCHING STARTS HERE

//                BackgroundImage bi = new BackgroundImage(new Image("file:C:\\Users\\Liana\\Desktop\\Room.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, null);
//                Background b = new Background(bi);
//                FXGL.getGameScene().getRoot().setBackground(b);
                
                //FXGL.getGameScene().setBackgroundRepeat(new Image("file:C:\\Users\\Liana\\Desktop\\Room.png"));
                //e.add(newMap);

                e.add(FXGL.entityBuilder()
            	        .type(EntityType.COIN)
            	        .at(200, 200)
            	        .viewWithBBox(new Rectangle(25,25,Color.GREEN))
            	        .with(new CollidableComponent(true))
            	        .build());
               
               level2 = new Level(600, 600, e); //Check out this class
                
               FXGL.getGameWorld().setLevel(level2); // ENDS  HERE
         
                
            }
        });
    }
    
    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100

        textPixels.textProperty().bind(FXGL.getGameState().intProperty("pixelsMoved").asString());

        FXGL.getGameScene().addUINode(textPixels); // add to the scene graph
    }

    public static void main(String[] args) {
        launch(args);
    }
}