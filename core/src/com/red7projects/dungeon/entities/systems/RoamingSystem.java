package com.red7projects.dungeon.entities.systems;

import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;

public class RoamingSystem implements DecisionSystem
{
    private final App app;

    public RoamingSystem(App app)
    {
        this.app = app;
    }

    @Override
    public void update(final GdxSprite gdxSprite, final GdxSprite target)
    {
    }

    @Override
    public Vector2 getTargetPosition(final GdxSprite gdxSprite, final GdxSprite target)
    {
        return null;
    }

    @Override
    public void setAdjustedTarget(final GdxSprite gdxSprite)
    {
    }

    @Override
    public void faceTarget(final float targetX, final float targetY, final GdxSprite gdxSprite)
    {

    }

    @Override
    public Vector2 getTargetVector(final float targetX, final float targetY, final GdxSprite gdxSprite)
    {
        return null;
    }

    @Override
    public void calculateMove(final Vector2 vector2, final GdxSprite gdxSprite)
    {

    }

    @Override
    public float checkXMovement(final float _xMove, final GdxSprite gdxSprite)
    {
        return 0;
    }

    @Override
    public float checkYMovement(final float _yMove, final GdxSprite gdxSprite)
    {
        return 0;
    }

    @Override
    public float distanceRemaining(final GdxSprite parentSprite, final Vector2 destination)
    {
        return 0;
    }
}
