package application;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

/*
 * TODO: Here I will put everyones tasks, and helpful links to documentation that each person may need, If anyone needs to know we are using FXGL version 0.5.4
 * Mackenzie:
 * 			Task - import an animated sprite into the game and polish that
 * 			Helpful link - https://github.com/AlmasB/FXGL/wiki/Adding-Sprite-Animations
 * Liana:
 * 			Task - implement a menu for the game on start-up
 * 			Note - This is deep in to the tutorial so idk if you will need any extra things, you shouldn't, but just letting you know
 * 			Helpful link - https://github.com/AlmasB/FXGL/wiki/Customizing-Menus
 * Adam:
 * 			Task - Set up a music player to play music in the background as the game plays, work together with Ben to switch the music when the level changes.
 * 			Helpful links - (You may need to know this in case any errors pop up) https://www.tutorialspoint.com/java/java_multithreading.htm
 * 						  - (This tutorial gives a basic idea but I already implemented it here) https://github.com/AlmasB/FXGL/wiki/Adding-Images-and-Sounds
 * 						  - There is no tutorial for adding music so I believe in you.
 * Ben:
 * 			Task - Set up rudimentary levels (that switch at the event of pressing a button) 
 * 			Helpful link - https://github.com/AlmasB/FXGL/wiki/Building-Levels
 * 
 * I will be doing misc things around the project and learning along with the tutorials and I hope you all do the same. Feel free to ask me or anyone else if you get stuck
 * on something, I'm willing to bet anyone here will be happy to help, including myself. 
 * 
 * Here is the link to the tutorials I encourage all of you to read up on everything here, we're probably going to need it all.
 * https://github.com/AlmasB/FXGL/wiki/FXGL-0.5.4
 * 
 * REMEMBER TO PULL THE PROJECT BEFORE EDITING AND TO PUSH THE PROJECT AFTER EDITING!!!
 * 
 * Text the discord if there are any questions, And good luck to us all, this project is gonna be dope!
 * 
 *  
 */

public class GameMain extends GameApplication {

	private Entity player;
	
/*
 * initialize basic settings.
 */
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(800);
		settings.setHeight(600);
		settings.setTitle("Basic Game App");
		settings.setVersion("0.1");
	}

	
	/*
	 * Initialize the player entity.
	 */
	@Override
	protected void initGame() {
		player = Entities.builder().at(300, 300)
				.viewFromTexture("brick.png")
				.buildAndAttach(getGameWorld());
	}

	/*
	 * Initializes the input of the game
	 */
	@Override
	protected void initInput() {
		
		//Create a new input object and add actions to it adding an action is an implicit method overriding.
		Input input = getInput();
		
		input.addAction(new UserAction("Move Right") {
			@Override
			protected void onAction() {
				player.translateX(5);
				getGameState().increment("pixelsMoved", +5);
			}
		}, KeyCode.D);
		input.addAction(new UserAction("Move Left") {
			@Override
			protected void onAction() {
				player.translateX(-5);
				getGameState().increment("pixelsMoved", -5);
			}
		}, KeyCode.A);
		input.addAction(new UserAction("Move Up") {
			@Override
			protected void onAction() {
				player.translateY(-5);
				getGameState().increment("pixelsMoved", -5);
			}
		}, KeyCode.W);
		input.addAction(new UserAction("Move Down") {
			@Override
			protected void onAction() {
				player.translateY(5);
				getGameState().increment("pixelsMoved", +5);
			}
		}, KeyCode.S);
		
		input.addAction(new UserAction("Play Sound") {
		    @Override
		    protected void onActionBegin() {
		        getAudioPlayer().playSound("sound.wav");
		    }
		}, KeyCode.F); 
		
	}
	/*
	 * Initialize the UI of the game, not much here for now, lets change that.
	 */
	@Override
	protected void initUI() {
		Text textPixels = new Text();
		Texture brickTexture = getAssetLoader().loadTexture("brick.png");
		
		textPixels.setTranslateX(50);
		textPixels.setTranslateY(100);
		
		brickTexture.setTranslateX(50);
		brickTexture.setTranslateY(450);
		
		getGameScene().addUINode(textPixels);
		getGameScene().addUINode(brickTexture);
		textPixels.textProperty().bind(getGameState().intProperty("pixelsMoved").asString());
	}
	
	protected void initGameVars(Map<String, Object> vars) {
		vars.put("pixelsMoved",0);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
