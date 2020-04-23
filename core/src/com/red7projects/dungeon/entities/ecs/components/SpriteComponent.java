package com.red7projects.dungeon.entities.ecs.components;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.red7projects.dungeon.entities.ecs.ashley.core.Component;
import com.red7projects.dungeon.game.Actions;

public class SpriteComponent implements Component
{
    public Sprite  sprite;
    public int     spriteNumber;
    public Actions spriteAction;
}
