package com.red7projects.dungeon.entities.systems;

import com.badlogic.gdx.math.Vector2;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.physics.Movement;

public class HuntingSystem implements DecisionSystem
{
    public float   speedX;
    public float   speedY;
    public boolean isAdjustingTarget;
    private final App app;

    public HuntingSystem(App app)
    {
        this.app = app;
    }

    @Override
    public void update(GdxSprite gdxSprite, GdxSprite target)
    {
        moveHunter(gdxSprite, target);

        gdxSprite.speed.set(speedX, speedY);
    }

    @Override
    public Vector2 getTargetPosition(final GdxSprite gdxSprite, final GdxSprite target)
    {
        return null;
    }

    @Override
    public void setAdjustedTarget(GdxSprite gdxSprite)
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

    private void moveHunter(GdxSprite gdxSprite, GdxSprite target)
    {
        gdxSprite.speed.setEmpty();

        if (gdxSprite.sprite.getX() < target.sprite.getX())
        {
            gdxSprite.direction.setX(Movement._DIRECTION_RIGHT);
        }

        if (gdxSprite.sprite.getX() > target.sprite.getX())
        {
            gdxSprite.direction.setX(Movement._DIRECTION_LEFT);
        }

        if (gdxSprite.sprite.getY() < target.sprite.getY())
        {
            gdxSprite.direction.setY(Movement._DIRECTION_UP);
        }

        if (gdxSprite.sprite.getY() > target.sprite.getY())
        {
            gdxSprite.direction.setY(Movement._DIRECTION_DOWN);
        }
    }
}
