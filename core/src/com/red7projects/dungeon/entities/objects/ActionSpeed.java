package com.red7projects.dungeon.entities.objects;

import com.red7projects.dungeon.game.Actions;

public class ActionSpeed
{
    public Actions action;
    public float   speed;

    public ActionSpeed(Actions _action, float _speed)
    {
        this.action = _action;
        this.speed = _speed;
    }
}
