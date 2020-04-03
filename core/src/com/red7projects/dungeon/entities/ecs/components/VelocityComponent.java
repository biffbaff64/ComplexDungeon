package com.red7projects.dungeon.entities.ecs.components;


import com.red7projects.dungeon.entities.ecs.ashley.core.Component;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Speed;
import com.red7projects.dungeon.types.XYSetF;

public class VelocityComponent implements Component
{
    public Direction direction;                  // Direction of movement.
    public Direction lookingAt;                  // Direction the entity is facing.
    public XYSetF    distance;
    public XYSetF    distanceReset;
    public Speed     speed;                      // Speed of movement
}
