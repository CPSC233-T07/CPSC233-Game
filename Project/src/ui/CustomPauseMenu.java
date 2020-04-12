package ui;

import java.util.ArrayList;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.PauseMenu;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.profile.DataFile;

import application.GameApp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/*
 * A custom pause menu when pausing the game using "esc"
 */


public class CustomPauseMenu extends PauseMenu{

	private Animation<?> animation;
	
	private static final double APP_WIDTH = FXGL.getAppWidth();	
	private static final double APP_HEIGHT = FXGL.getAppHeight();
	
	private static final double WINDOW_SIZE = 300;
	
	private static final double BUTTON_WIDTH = 125;
	private static final double BUTTON_HEIGHT = 40;

	private boolean dataSaved = false;
	
	public double playerX = 0;
	public double playerY = 0;
	
    public void MyPauseMenu() {
        getContentRoot().setTranslateX((APP_WIDTH / 2.0) - WINDOW_SIZE);
        getContentRoot().setTranslateY((APP_HEIGHT / 2.0) - WINDOW_SIZE);
	
        //Background Setup
        Rectangle bg = new Rectangle(WINDOW_SIZE, WINDOW_SIZE);
        bg.setFill(Color.DARKSEAGREEN);
        bg.setTranslateX((APP_WIDTH / 2.0) - (WINDOW_SIZE / 2.0));
        bg.setTranslateY((APP_HEIGHT / 2.0) - (WINDOW_SIZE / 2.0));
        bg.setStrokeWidth(2.5);
        bg.setStroke(Color.SEASHELL);
        
        //Buttons
        Button btnExit = new Button("EXIT");
        btnExit.setTranslateX((APP_WIDTH / 2.0) - (BUTTON_WIDTH / 2));
        btnExit.setTranslateY((APP_HEIGHT / 2) + BUTTON_HEIGHT * 1.25);
        btnExit.setPrefHeight(BUTTON_HEIGHT);
        btnExit.setPrefWidth(BUTTON_WIDTH);
        btnExit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				FXGL.getGameController().exit();
				
			}
        	
        });
	
        
        Button btnResume = new Button("RESUME");
        btnResume.setTranslateX((APP_WIDTH / 2.0) - (BUTTON_WIDTH / 2));
        btnResume.setTranslateY((APP_HEIGHT / 2) - BUTTON_HEIGHT * 2.5);
        btnResume.setPrefHeight(BUTTON_HEIGHT);
        btnResume.setPrefWidth(BUTTON_WIDTH);
        btnResume.setOnMouseClicked(e -> requestHide());
        
        
        
        Button btnSave = new Button("SAVE");
        btnSave.setTranslateX((APP_WIDTH / 2.0) - (BUTTON_WIDTH / 2));
        btnSave.setTranslateY((APP_HEIGHT / 2) - BUTTON_HEIGHT * 1.25);
        btnSave.setPrefHeight(BUTTON_HEIGHT);
        btnSave.setPrefWidth(BUTTON_WIDTH);
        btnSave.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				dataSaved = true;
				ArrayList<Entity> entities = FXGL.getGameWorld().getEntities();
				Entity player = entities.get(3);
				
				playerX = player.getX();
				playerY = player.getY();
				btnSave.setOnMouseClicked(e -> requestHide());			
			}
        	
        });
        
        
        
        Button btnLoad = new Button("LOAD");
        btnLoad.setTranslateX((APP_WIDTH / 2.0) - (BUTTON_WIDTH / 2));
        btnLoad.setTranslateY(APP_HEIGHT / 2);
        btnLoad.setPrefHeight(BUTTON_HEIGHT);
        btnLoad.setPrefWidth(BUTTON_WIDTH);
        btnLoad.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if(dataSaved){
					ArrayList<Entity> entities = FXGL.getGameWorld().getEntities();
					Entity player = entities.get(3);
					player.setX(playerX);
					player.setY(playerY);
					btnLoad.setOnMouseClicked(e -> requestHide());
				}
				
			}
        	
        });
       
        getContentRoot().getChildren().addAll(bg, btnExit, btnSave, btnResume, btnLoad);

        animation = FXGL.animationBuilder()
                .duration(Duration.seconds(0.66))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .scale(getContentRoot())
                .from(new Point2D(0, 0))
                .to(new Point2D(1, 1))
                .build();
        
    }
    
    @Override
    public void onCreate() {
        animation.setOnFinished(EmptyRunnable.INSTANCE);
        animation.start();
    }

    @Override
    protected void onUpdate(double tpf) {
        animation.onUpdate(tpf);
    }

    @Override
    protected void onHide() {
        if (animation.isAnimating())
            return;

        animation.setOnFinished(() -> FXGL.getSceneService().popSubScene());
        animation.startReverse();
    }

}

