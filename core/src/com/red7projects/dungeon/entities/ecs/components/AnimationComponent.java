package com.red7projects.dungeon.entities.ecs.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.entities.ecs.ashley.core.Component;

public class AnimationComponent implements Component
{
    public float                    elapsedTime;
    public Animation<TextureRegion> animation;
    public TextureRegion[]          animFrames;
}
