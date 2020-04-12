package ui;
import com.almasb.fxgl.app.scene.PauseMenu;
import com.almasb.fxgl.app.scene.SceneFactory;


/*
 * Menu factory to return the pause menu to the main game.
 */

public class MenuFactory extends SceneFactory{
	@Override
	public PauseMenu newPauseMenu() 
	{
		CustomPauseMenu newMenu = new CustomPauseMenu();
		newMenu.MyPauseMenu();
		return newMenu;
	}

	
}
