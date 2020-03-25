package ui;

import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;



public class MapFactory implements EntityFactory{
	
	@Spawns("wall")
	public Entity newWall(SpawnData data) {
		return Entities.builder()
				.from(data)
				.bbox(new HitBox(BoundingShape.box(data.getX(), data.getY())))
				.with(new PhysicsComponent())
				.build();
	}
	@Spawns("wall2")
	public Entity newWall2(SpawnData data) {
		return Entities.builder()
				.from(data)
				.bbox(new HitBox(BoundingShape.box(114, 635)))
				.with(new PhysicsComponent())
				.build();
	}

	}
		
	



