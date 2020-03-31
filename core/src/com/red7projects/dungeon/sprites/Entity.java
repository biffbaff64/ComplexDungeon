package com.red7projects.dungeon.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.red7projects.dungeon.maths.SimpleVec2F;

public class Entity
{
    public SimpleVec2F position;
    public Texture     texture;
    public float       width;
    public float       height;

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(texture, position.x, position.y, width, height);
    }
}
