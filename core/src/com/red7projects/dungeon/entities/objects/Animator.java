package com.red7projects.dungeon.entities.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ArrayMap;

public interface Animator
{
    ArrayMap<String, Animation> animations       = null;
    float                       time             = 0;
    Animation                   currentAnimation = null;

    void upsate(float deltaTime);

    TextureAtlas.AtlasRegion getCurrentFrame();

    String getAnimation();

    void setAnimation(String name);

    void setPlaybackScale(float scale);

    void play();

    void pause();

    void resume();

    void stop();
}
