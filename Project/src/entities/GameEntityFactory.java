package entities;

import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsUnitConverter;
import com.almasb.fxgl.physics.box2d.collision.shapes.Shape;

import application.GameApp;
import javafx.geometry.Dimension2D;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;


/*
 * This class defines everything that is used in FXGL.spawn()
 * implements entity factory to spawn in entities to the game window
 * and returns said entities if needed for variables
 */
public class GameEntityFactory implements EntityFactory{
	
	
	@Spawns("player")
	public Entity newPlayer(SpawnData data) {
		Entity player=FXGL.entityBuilder().at(GameApp.MAP_WIDTH/2, GameApp.MAP_HEIGHT/2)
				.type(EntityType.PLAYER)
				.with(new PlayerAnimationComponent("CharacterSprite.png"))
				.viewWithBBox("bbox.png")
				.with(new CollidableComponent(true))
				.build();
		return player;
	}
	@Spawns("enemy")
	public Entity newEnemy(SpawnData data) {
		Entity enemy=FXGL.entityBuilder().at(data.getX(),data.getY())
				.type(EntityType.FROGGY)
				.with(new NPCAnimationComponent("froggysprite.png"))
				.with(new CollidableComponent(true))
				.viewWithBBox("bbox.png")
				.build();
		return enemy;
	}
	
	@Spawns("blueHouse")
	public Entity newblueHouse(SpawnData data) {
		Entity blueHouse = FXGL.entityBuilder().at(data.getX(), data.getY())
				.type(EntityType.BARRIER)
				.with(new BarrierComponent())
				.with(new CollidableComponent(true))
				.viewWithBBox("blueHouse.png")
				.build();
		return blueHouse;
		
	}

	}
		
	



