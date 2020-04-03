package com.red7projects.dungeon.entities.ecs.components;

import com.red7projects.dungeon.entities.ecs.ashley.core.Component;
import com.red7projects.dungeon.maths.SimpleVec2F;

public class TransformComponent implements Component
{
    public SimpleVec2F initXY;
    public int         zPosition;
    public float       frameWidth;
    public float       frameHeight;
    public float       rotateSpeed;
    public float       rotation;
}
