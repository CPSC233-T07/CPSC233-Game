package ui;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.PauseMenu;
import com.almasb.fxgl.app.scene.SceneFactory;

public class MenuFactory extends SceneFactory{
	@Override
	public PauseMenu newPauseMenu() 
	{
		CustomPauseMenu newMenu = new CustomPauseMenu();
		newMenu.MyPauseMenu();
		return newMenu;
	}
	
}
