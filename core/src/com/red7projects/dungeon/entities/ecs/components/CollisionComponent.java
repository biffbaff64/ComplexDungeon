package com.red7projects.dungeon.entities.ecs.components;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.red7projects.dungeon.entities.ecs.ashley.core.Component;
import com.red7projects.dungeon.entities.objects.CollisionListener;
import com.red7projects.dungeon.physics.aabb.AABB;
import com.red7projects.dungeon.physics.aabb.CollisionObject;

public class CollisionComponent implements Component
{
    public float             rightEdge;             //
    public float             topEdge;               //
    public AABB              aabb;                  // The AABB box collision handler;
    public CollisionObject   collisionObject;       // The AABB collision rectangle and data.
    public CollisionListener collisionCallback;

    // IF Box2D is used...
    public Body    b2dBody;                         // The Box2D physics body
    public BodyDef bodyDef;                         // The Box2D body definition
}
